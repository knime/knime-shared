/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Jul 4, 2023 (leonard.woerteler): created
 */
package org.knime.core.node.workflow.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata;
import org.knime.core.util.Pair;
import org.knime.core.util.workflowalizer.WorkflowSetMeta.Link;
import org.knime.core.util.xml.NoExternalEntityResolver;
import org.knime.x29.metainfo.KNIMEMetaInfoDocument;
import org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo;
import org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element;

/**
 * Parser and serializer for the legacy {@code workflowset.meta} format.
 * This goes beyond the Workflowalizer functionality, as the Workflowalizer only reads selected information.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 *
 * @since 6.0
 */
public final class WorkflowSetMetaParser {

    // Copied from org.knime.workbench.ui.workflow.metadata.MetaInfoFile
    private static final String NO_TITLE_PLACEHOLDER_TEXT = "There has been no title set for this workflow's metadata.";

    // Copied from org.knime.workbench.ui.workflow.metadata.MetaInfoFile
    private static final String NO_DESCRIPTION_PLACEHOLDER_TEXT =
        "There has been no description set for this workflow's metadata.";

    private static final Pattern FULL_CREATION_PATTERN =
            Pattern.compile("^(\\d+)/(\\d+)/(\\d+)" // just the date
                + "(?:/(\\d+):(\\d+):(\\d+))?"      // time of day (optional)
                + "(?: ([+-]?)(\\d+):(\\d+))?$");   // time zone offset (optional)

    private static final DateTimeFormatter SERVER_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    private static final Pattern LINE_BREAK_REGEX = Pattern.compile("\\r?\\n");

    private static final Predicate<String> TAGGED_LINE_PRED =
            Pattern.compile("^(TAGS?|BLOG|URL|VIDEO):", Pattern.CASE_INSENSITIVE).asPredicate();

    private static final XmlOptions READ_OPTIONS = new XmlOptions();
    private static final XmlOptions WRITE_OPTIONS = new XmlOptions();
    static {
        READ_OPTIONS.disallowDocTypeDeclaration();
        READ_OPTIONS.setEntityResolver(NoExternalEntityResolver.getInstance());
        READ_OPTIONS.setLoadSubstituteNamespaces(Map.of("", "http://www.knime.org/2.9/metainfo"));

        WRITE_OPTIONS.setUseDefaultNamespace();
        WRITE_OPTIONS.setSavePrettyPrint();
        WRITE_OPTIONS.setSavePrettyPrintIndent(4);
        WRITE_OPTIONS.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }

    /**
     * Metadata contents as required for writing the legacy {@code workflowset.meta} format.
     */
    public static final class MetadataContents {
        private final String m_author;
        private final String m_description;
        private final OffsetDateTime m_creationDate;
        private final OffsetDateTime m_lastEdited;
        private final OffsetDateTime m_lastUploaded;
        private final List<String> m_tags;
        private final List<Link> m_links;

        /**
         * Creates metadata with the given contents, all fields may be {@code null}.
         *
         * @param author
         * @param description
         * @param tags
         * @param links
         * @param creationDate
         * @param lastEdited
         * @param lastUploaded
         */
        public MetadataContents(final String author, final String description, final List<String> tags,
                final List<Link> links, final OffsetDateTime creationDate, final OffsetDateTime lastEdited,
                final OffsetDateTime lastUploaded) {
            m_author = author;
            m_description = description;
            m_tags = tags == null ? List.of() : tags;
            m_links = links == null ? List.of() : links;
            m_creationDate = creationDate;
            m_lastEdited = lastEdited;
            m_lastUploaded = lastUploaded;
        }

        /**
         * Creates metadata contents from a new {@link NodeContainerMetadata metadata XML file}.
         *
         * @param metadata contents of the new XML file
         * @return converted metadata
         */
        public static MetadataContents from(final NodeContainerMetadata metadata) {
            final var links = Optional.ofNullable(metadata.getLinks().getLinkList()).orElse(List.of());
            return new MetadataContents(metadata.getAuthor(), metadata.getDescription(),
                new ArrayList<>(Optional.ofNullable(metadata.getTags().getTagList()).orElse(List.of())),
                links.stream().map(link -> new Link(link.getHref(), link.getStringValue())) //
                        .collect(Collectors.toList()),
                toOffsetDateTime(metadata.getCreated()), toOffsetDateTime(metadata.getLastModified()), null);
        }

        private static OffsetDateTime toOffsetDateTime(final Calendar calendar) {
            return calendar == null ? null : ZonedDateTime.ofInstant(calendar.toInstant(),
                Optional.ofNullable(calendar.getTimeZone()).map(TimeZone::toZoneId).orElse(ZoneId.systemDefault())) //
                .toOffsetDateTime();
        }

        /**
         * @return the author
         */
        public Optional<String> getAuthor() {
            return Optional.ofNullable(m_author);
        }

        /**
         * @return the description
         */
        public Optional<String> getDescription() {
            return Optional.ofNullable(m_description);
        }

        /**
         * @return the creation date
         */
        public Optional<OffsetDateTime> getCreationDate() {
            return Optional.ofNullable(m_creationDate);
        }

        /**
         * @return the lastEdited
         */
        public Optional<OffsetDateTime> getLastEdited() {
            return Optional.ofNullable(m_lastEdited);
        }

        /**
         * @return the lastUploaded
         */
        public Optional<OffsetDateTime> getLastUploaded() {
            return Optional.ofNullable(m_lastUploaded);
        }

        /**
         * @return the tags
         */
        public List<String> getTags() {
            return m_tags;
        }

        /**
         * @return the links
         */
        public List<Link> getLinks() {
            return m_links;
        }

        @Override
        public String toString() {
            final var descOneLine = m_description == null ? ""
                : LINE_BREAK_REGEX.matcher(m_description).replaceAll(" ").trim();
            final var descShort = descOneLine.length() > 40 ? (descOneLine.substring(0, 37) + "...") : descOneLine;
            return "WorkflowSetMeta[author='" + m_author + "', description='" + descShort + "', tags=" + m_tags
                    + ", links=" + m_links + ", creationDate=" + m_creationDate + ", lastEdited=" + m_lastEdited
                    + ", lastUploaded=" + m_lastUploaded + "]";
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != MetadataContents.class) {
                return false;
            }
            final var other = (MetadataContents)obj;
            return Objects.equals(m_author, other.m_author)
                    && Objects.equals(m_description, other.m_description)
                    && Objects.equals(m_creationDate, other.m_creationDate)
                    && Objects.equals(m_lastEdited, other.m_lastEdited)
                    && Objects.equals(m_lastUploaded, other.m_lastUploaded)
                    && Objects.equals(m_links, other.m_links)
                    && Objects.equals(m_tags, other.m_tags);
        }

        @Override
        public int hashCode() {
            return Objects.hash(m_author, m_description, m_creationDate, m_lastEdited, m_lastUploaded, m_links, m_tags);
        }

        /**
         * Writes these metadata to the given file.
         *
         * @param xmlFile output file
         * @throws IOException
         */
        public void toXML(final Path xmlFile) throws IOException {
            try (final var outStream = Files.newOutputStream(xmlFile)) {
                WorkflowSetMetaParser.write(this, outStream);
            }
        }
    }

    private WorkflowSetMetaParser() {
    }

    /**
     * Parses the {@code workflowset.meta} file from the given input stream.
     *
     * @param inStream input stream providing the XML contents
     * @return parsed metadata contents
     * @throws XmlException
     * @throws IOException
     */
    public static MetadataContents parse(final InputStream inStream) throws XmlException, IOException {
        final var metaInfo = KNIMEMetaInfoDocument.Factory.parse(inStream, READ_OPTIONS).getKNIMEMetaInfo();
        final var fields = metaInfo.getElementList().stream() //
                .collect(Collectors.groupingBy(Element::getName, Collectors.mapping(Element::getStringValue,
                    Collectors.filtering(StringUtils::isNotBlank, Collectors.toList()))));
        final var author = fields.getOrDefault("Author", List.of()).stream().findFirst().orElse(null);
        final var comments = fields.getOrDefault("Comments", List.of()).stream().findFirst().orElse(null);
        final var tags = new ArrayList<String>();
        final var links = new ArrayList<Link>();
        final var titleAndDescription = parseCommentsField(comments, tags, links);
        final var title = titleAndDescription.getFirst();
        final var description = titleAndDescription.getSecond();
        final var combinedDescription = title != null && description != null ? (title + "\n\n" + description)
            : StringUtils.firstNonBlank(title, description);

        return new MetadataContents(author, combinedDescription, tags, links,
            parseCreationDate(fields.getOrDefault("Creation Date", List.of()).stream().findFirst().orElse(null)),
            parseServerDate(fields.getOrDefault("Last Edited", List.of()).stream().findFirst().orElse(null)),
            parseServerDate(fields.getOrDefault("Last Uploaded", List.of()).stream().findFirst().orElse(null)));
    }

    /**
     * Writes the given metadata to the given output stream as XML.
     *
     * @param contents
     * @param outStream
     * @throws IOException
     */
    public static void write(final MetadataContents contents, final OutputStream outStream) throws IOException {
        final var metaInfoDocument = KNIMEMetaInfoDocument.Factory.newInstance();
        final var metaInfo = metaInfoDocument.addNewKNIMEMetaInfo();
        try (final var cursor = metaInfo.newCursor()) {
            cursor.insertComment("\n    This file is auto-generated and is overwritten each time the workflow is saved."
                + "\n    The metadata have moved to the `workflow-metadata.xml` file.\n");
        }
        contents.getAuthor().ifPresent(author -> addElement(metaInfo, "Author", "text", author));
        contents.getCreationDate().ifPresent(created -> addElement(metaInfo, "Creation Date", "date",
            formatCreated(created)));
        addElement(metaInfo, "Comments", "multiline", formatDescription(contents));
        contents.getLastUploaded().ifPresent(uploaded -> addElement(metaInfo, "Last Uploaded", "text",
            SERVER_DATE_FORMATTER.format(uploaded)));
        contents.getLastEdited().ifPresent(edited -> addElement(metaInfo, "Last Edited", "text",
            SERVER_DATE_FORMATTER.format(edited)));
        metaInfo.setNrOfElements(BigInteger.valueOf(metaInfo.sizeOfElementArray()));
        metaInfoDocument.save(outStream, WRITE_OPTIONS);
    }

    private static String formatCreated(final OffsetDateTime created) {
        final var offset = created.getOffset().getTotalSeconds();
        final var sign = offset >= 0 ? "+" : "-";
        final var absOffset = Math.abs(offset);
        final var offsetMinutes = (int) Math.round(absOffset / 60.0);
        final var offsetHours = (int) Math.round(offsetMinutes / 60.0);
        return String.format("%d/%d/%d/%02d:%02d:%02d %s%02d:%02d",
            created.getDayOfMonth(),
            created.getMonthValue() - 1, // this is weird but consistent with the existing files
            created.getYear(),
            created.getHour(), created.getMinute(), created.getSecond(),
            sign, offsetHours, offsetMinutes % 60);
    }

    private static String formatDescription(final MetadataContents contents) {
        final var sb = new StringBuilder();
        sb.append(NO_TITLE_PLACEHOLDER_TEXT).append("\n\n");
        sb.append(contents.getDescription().orElse(NO_DESCRIPTION_PLACEHOLDER_TEXT));
        final var tags = contents.getTags();
        final var links = contents.getLinks();
        if (!(tags.isEmpty() && links.isEmpty())) {
            if (!links.isEmpty()) {
                sb.append("\n");
                links.forEach(link -> {
                    sb.append("\nURL: ");
                    if (!StringUtils.isBlank(link.getText())) {
                        sb.append(link.getText()).append(" ");
                    }
                    sb.append(link.getUrl());
                });
            }
            if (!tags.isEmpty()) {
                sb.append("\n\nTAGS: ").append(StringUtils.join(tags.toArray(String[]::new), ','));
            }
        }
        return sb.toString();
    }

    private static void addElement(final KNIMEMetaInfo metaInfo, final String name, final String form,
            final String value) {
        final var authorElement = metaInfo.addNewElement();
        authorElement.setForm(form);
        authorElement.setName(name);
        authorElement.setReadOnly(name.equals("Last Edited") || name.equals("Last Uploaded"));
        authorElement.setStringValue(value);
    }

    private static OffsetDateTime parseServerDate(final String serverDate) {
        try {
            return serverDate == null ? null : OffsetDateTime.parse(serverDate, SERVER_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static OffsetDateTime parseCreationDate(final String creationDate) {
        if (creationDate == null) {
            return null;
        }

        final var matcher = FULL_CREATION_PATTERN.matcher(creationDate);
        if (!matcher.matches()) {
            return null;
        }

        final var day = Math.min(Math.max(1, Integer.parseInt(matcher.group(1))), 31);
        final var month = Math.min(Math.max(1, Integer.parseInt(matcher.group(2)) + 1), 12);
        final var year = Math.min(Math.max(Year.MIN_VALUE, Integer.parseInt(matcher.group(3))), Year.MAX_VALUE);

        var hour = 0;
        var minute = 0;
        var second = 0;
        if (!StringUtils.isEmpty(matcher.group(4))) {
            hour = Math.min(Math.max(0, Integer.parseInt(matcher.group(4))), 23);
            minute = Math.min(Math.max(0, Integer.parseInt(matcher.group(5))), 59);
            second = Math.min(Math.max(0, Integer.parseInt(matcher.group(6))), 59);
        }

        var zone = ZoneId.systemDefault();
        if (!StringUtils.isEmpty(matcher.group(8))) {
            int sign = Optional.ofNullable(matcher.group(7)).filter(str -> str.equals("-")).isPresent() ? -1 : 1;
            int hourOffset = Math.min(Math.max(0, Integer.parseInt(matcher.group(8))), 23);
            int minuteOffset = Math.min(Math.max(0, Integer.parseInt(matcher.group(9))), 59);
            zone = ZoneOffset.ofTotalSeconds(sign * 60 * (60 * hourOffset + minuteOffset));
        }
        try {
            return LocalDateTime.of(year, month, day, hour, minute, second).atZone(zone).toOffsetDateTime();
        } catch (DateTimeException e) { // NOSONAR
            return null;
        }
    }

    private static Pair<String, String> parseCommentsField(final String comments, final List<String> tags,
            final List<Link> links) {
        if (comments == null) {
            return Pair.create(null, null);
        }

        String[] lines = LINE_BREAK_REGEX.split(comments);
        if (isOldStyle(lines)) {
            return parseComplexCommentField(tags, links, lines);
        } else {
            return Pair.create(null, StringUtils.joinWith("\n", (Object[])lines));
        }
    }

    private static Pair<String, String> parseComplexCommentField(final List<String> tags,
            final List<Link> links, final String[] lines) {
        final var posAndTitle = parseTitle(lines);

        final var posAndDescription = parseDescription(lines, posAndTitle.getFirst());

        parseLinksAndTags(lines, posAndDescription.getFirst(), links, tags);

        return Pair.create(posAndTitle.getSecond(), posAndDescription.getSecond());
    }

    private static Pair<Integer, String> parseTitle(final String[] lines) {
        final var builder = new StringBuilder(lines[0].trim());
        var pos = 1;
        while (pos < lines.length && !lines[pos].isBlank()) {
            String line = lines[pos];
            builder.append('\n').append(line);
            pos++;
        }
        String title = Optional.of(builder.toString().trim()) //
            .filter(t -> !t.isEmpty() && !NO_TITLE_PLACEHOLDER_TEXT.equals(t)) //
            .orElse(null);
        return Pair.create(pos, title);
    }

    private static Pair<Integer,String> parseDescription(final String[] lines, int pos) {
        final var builder = new StringBuilder();
        while (pos < lines.length && lines[pos].isBlank()) {
            pos++;
        }
        while (pos < lines.length && !TAGGED_LINE_PRED.test(lines[pos])) {
            String line2 = lines[pos];
            builder.append(line2).append('\n');
            pos++;
        }
        final var description = Optional.of(builder.toString().trim()) //
                .filter(t -> !t.isEmpty() && !NO_DESCRIPTION_PLACEHOLDER_TEXT.equals(t)) //
                .orElse(null);
        return Pair.create(pos, description);
    }

    private static void parseLinksAndTags(final String[] lines, final int initPos,
        final List<Link> links, final List<String> tags) {
        var pos = initPos;
        // every iteration of the loop starts with a tag like `LINK:` or `TAG:`
        while (pos < lines.length) {
            final var line = lines[pos];
            final var colonPos = line.indexOf(':');
            final var tag = line.substring(0, colonPos).toUpperCase(Locale.US);
            final var builder = new StringBuilder(line.substring(colonPos + 1).trim());
            pos++;

            while (pos < lines.length && !TAGGED_LINE_PRED.test(lines[pos])) {
                builder.append(' ').append(lines[pos].trim());
                pos++;
            }

            final var rest = builder.toString().trim();
            if (rest.isEmpty()) {
                continue;
            }

            if (tag.equals("TAG") || tag.equals("TAGS")) {
                tags.addAll(Arrays.stream(rest.split(",")).map(String::trim).collect(Collectors.toList()));
            } else {
                // we ignore improperly formatted links (no `http:` or `https:` URL)
                final int linkStart = Math.max(rest.lastIndexOf("http:"), rest.lastIndexOf("https:"));
                if (linkStart >= 0) {
                    links.add(new Link(rest.substring(linkStart).trim(), rest.substring(0, linkStart).trim()));
                }
            }
        }
    }

    private static boolean isOldStyle(final String[] lines) {
        boolean isOldStyle = (lines.length > 2);
        if (isOldStyle) {
            isOldStyle = false;
            for (var i = 1; i < (lines.length - 1); i++) {
                if ((lines[i].trim().length() == 0) && (lines[i + 1].trim().length() > 0)) {
                    isOldStyle = true;
                    break;
                }
            }
        }
        return isOldStyle;
    }

    /**
     * Builder for metadata contents.
     * @return the builder
     */
    public static ContentsBuilder builder() {
        return new ContentsBuilder();
    }

    /**
     * Builder for metadata contents.
     */
    public static final class ContentsBuilder {

        private String m_author;
        private String m_description;
        private OffsetDateTime m_creationDate;
        private OffsetDateTime m_lastEdited;
        private OffsetDateTime m_lastUploaded;
        private final List<Link> m_links = new ArrayList<>();
        private final List<String> m_tags = new ArrayList<>();

        /**
         * @param author author name
         * @return this builder
         */
        public ContentsBuilder withAuthor(final String author) {
            m_author = author;
            return this;
        }

        /**
         * @param description
         * @return this builder
         */
        public ContentsBuilder withDescription(final String description) {
            m_description = description;
            return this;
        }

        /**
         * @param creationDate
         * @return this builder
         */
        public ContentsBuilder withCreationDate(final OffsetDateTime creationDate) {
            m_creationDate = creationDate;
            return this;
        }

        /**
         * @param lastEdited
         * @return this builder
         */
        public ContentsBuilder withLastEdited(final OffsetDateTime lastEdited) {
            m_lastEdited = lastEdited;
            return this;
        }

        /**
         * @param lastUploaded
         * @return this builder
         */
        public ContentsBuilder withLastUploaded(final OffsetDateTime lastUploaded) {
            m_lastUploaded = lastUploaded;
            return this;
        }

        /**
         * @param tag tag to add
         * @return this builder
         */
        public ContentsBuilder addTag(final String tag) {
            m_tags.add(tag);
            return this;
        }

        /**
         * Removes all previously added tags.
         * @return this builder
         */
        public ContentsBuilder clearTags() {
            m_tags.clear();
            return this;
        }

        /**
         * @param url URL of the link to be added, not {@code null}
         * @param title title text, may be {@code null}
         * @return this builder
         */
        public ContentsBuilder addLink(final String url, final String title) {
            m_links.add(new Link(url, title));
            return this;
        }

        /**
         * Removes all previously added links.
         * @return this builder
         */
        public ContentsBuilder clearLinks() {
            m_links.clear();
            return this;
        }

        /**
         * Builds a new {@link MetadataContents} instance.
         * @return newly created instance
         */
        public MetadataContents build() {
            return new MetadataContents(m_author, m_description, new ArrayList<>(m_tags), new ArrayList<>(m_links),
                m_creationDate, m_lastEdited, m_lastUploaded);
        }
    }
}
