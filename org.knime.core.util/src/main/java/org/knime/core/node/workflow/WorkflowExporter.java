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
 *   May 31, 2024 (leonard.woerteler): created
 */
package org.knime.core.node.workflow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.zip.UnrecognizedExtraField;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.function.FailableDoubleConsumer;
import org.apache.commons.lang3.function.FailableLongConsumer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.annotation.Owning;
import org.knime.core.node.util.CheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exporter for saving workflow items (workflows, components, metanodes, templates or data files)
 * as compressed archive files. Supports resource collection, exclusion policies, size limits and progress monitoring.
 *
 * @param <E> exception type thrown by the export progress updater when operations are cancelled
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 6.5
 */
@SuppressWarnings("ClassCanBeRecord")
public final class WorkflowExporter<E extends Exception> {

    /**
     * ID of the ZIP extra field used to mark the first execution data entry in exported workflow archives.
     * If present it enables consumers to stop reading early when only workflow metadata is needed (see AP-25597).
     *
     * @since 6.11
     */
    public static final int MARKER_HEADER_ID = 0xDADA;

    private static final String MODEL_PREFIX = "model_";

    private static final String LOCK_FILE = ".knimeLock";

    private static final String METAINFO_FILE = "workflowset.meta";

    private static final String SAVED_WITH_DATA_FILE = ".savedWithData";

    private static final String WORKFLOW_FILE = "workflow.knime";

    private static final String DROP_DIR_NAME = "drop";

    private static final String INTERN_FILE_DIR = "internal";

    private static final String FILESTORE_FOLDER_PREFIX = "filestore";

    private static final String INTERNAL_TABLE_FOLDER_PREFIX = "internalTables";

    private static final String PORT_FOLDER_PREFIX = "port_";

    private static final String WORKFLOW_DATA_DIR = "data";

    /**
     * Exports workflow resources to a compressed archive with size limit enforcement and cancellation support.
     *
     * @param localItems the resources to include in the export archive
     * @param tempFile the target file path where the compressed archive will be written
     * @param uploadLimit the maximum allowed compressed size in bytes (enforced during compression)
     * @param cancelChecker supplier that returns {@code true} when the operation should be cancelled
     * @param exception supplier that creates the exception to throw on cancellation or limit exceeded
     * @return the actual compressed size in bytes
     * @throws IOException if an I/O error occurs during compression or file operations
     * @throws E if the operation is cancelled or the size limit is exceeded during compression
     * @since 6.9
     */
    public long exportWorkflowWithLimit(
            final ResourcesToCopy localItems, final Path tempFile, final long uploadLimit,
            final BooleanSupplier cancelChecker, final Supplier<E> exception) throws IOException, E {
        try (final var outputStream =
                new CountingOutputStream(new BufferedOutputStream(Files.newOutputStream(tempFile)))) {
            this.exportInto( //
                localItems, //
                outputStream, //
                progress -> { //
                    final var writtenUntilNow = outputStream.getByteCount();
                    if (cancelChecker.getAsBoolean() || writtenUntilNow > uploadLimit) {
                        throw exception.get();
                    } //
                }); //
            return outputStream.getByteCount();
        } catch (Exception e) { // NOSONAR
            if (!cancelChecker.getAsBoolean()) {
                // not canceled by the user, so we must be over the limit
                return Files.size(tempFile);
            }
            throw exception.get();
        }
    }

    /** Types of items to upload. */
    public enum ItemType {
        /** Workflow-like items, like Workflows, Components and Metanodes. */
        WORKFLOW_LIKE,
        /** Workflow groups (i.e., folders). */
        WORKFLOW_GROUP,
        /** Data files. */
        DATA_FILE
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowExporter.class);

    private final boolean m_excludeData;

    /**
     * Constructor.
     *
     * @param excludeData whether execution data should be excluded by the export
     */
    public WorkflowExporter(final boolean excludeData) {
        m_excludeData = excludeData;
    }

    private interface ResourcesCollector {
        void addResource(Path path, long size, IPath destination, boolean isData) throws IOException;
    }

    /**
     * Scans the elements to export and collects all files and directories that will be archived. If execution data is
     * to be excluded, it will not be path of this method's result.
     *
     * @param elementsToExport top-level items to be exported
     * @param root root directory relative to which the destination path of the resources is determined
     * @return record of items to be copied
     * @throws IOException if files or folders could not be accessed
     */
    public ResourcesToCopy collectResourcesToCopy(final Iterable<Path> elementsToExport, final Path root)
            throws IOException {
        final Map<Path, IPath> nonData = new LinkedHashMap<>();
        final Map<Path, IPath> data = new LinkedHashMap<>();
        final long[] counts = { 0, 0 };
        final ResourcesCollector collector = (path, size, destination, isData) -> {
            (isData ? data : nonData).put(path, destination);
            if (size >= 0) {
                counts[0]++;
                counts[1] += size;
            }
        };

        for (Path element : elementsToExport) {
            // add all files within the workflow or group
            addResourcesFor(collector, root, element);
        }

        final var firstDataEntry = data.keySet().stream().findFirst().orElse(null);
        final var resourceList = nonData;
        resourceList.putAll(data);
        return new ResourcesToCopy(resourceList, (int)counts[0], counts[1], firstDataEntry);
    }

    /**
     * Determines whether or not the given ZIP file (assumed to be a {@code .knwf} file) has a single top-level
     * folder containing the item's contents.
     *
     * @param zipFile the ZIP file's path
     * @return {@code true} if there is a single top-level folder, {@code false} otherwise
     * @throws IOException I/O-related exception
     */
    public static boolean hasZipSingleRootFolder(final Path zipFile) throws IOException {
        String root = null;
        try (final var zip = ZipFile.builder().setPath(zipFile).get()) {
            for (final var entries = zip.getEntries(); entries.hasMoreElements();) {
                final var entry = entries.nextElement();
                final var entryPath = org.eclipse.core.runtime.Path.forPosix(entry.getName());
                if (entryPath.segmentCount() == 0) {
                    // entry for the root folder, skip
                    continue;
                }

                if (entryPath.segmentCount() == 1 && !entry.isDirectory()) {
                    // top-level file
                    return false;
                }

                final var newRoot = entryPath.segment(0);
                if (root != null && !root.equals(newRoot)) {
                    // found a second top-level folder
                    return false;
                }
                root = newRoot;
            }
        }
        return root != null;
    }

    /**
     * Implements the exclude policy. Resource is skipped if "exclude data" is checked.
     *
     * @param resource the resource to check
     * @param isDir whether the resource is a directory
     * @return true if the given resource should be excluded, false if it should be included
     */
    private static boolean excludeResource(final Path resource, final boolean isDir) {
        final var name = resource.getFileName().toString();
        if (name.equals(INTERN_FILE_DIR)) {
            return true;
        }

        final Stream<String> excludedPrefixes;
        if (isDir) {
            // directories to exclude:
            excludedPrefixes = Stream.of(PORT_FOLDER_PREFIX,
                INTERNAL_TABLE_FOLDER_PREFIX, FILESTORE_FOLDER_PREFIX,
                INTERN_FILE_DIR, DROP_DIR_NAME);
        } else {
            // files to exclude:
            if (name.equals("data.xml")) {
                return true;
            }
            excludedPrefixes = Stream.of(MODEL_PREFIX, SAVED_WITH_DATA_FILE, LOCK_FILE);
        }

        return excludedPrefixes.anyMatch(name::startsWith);
    }

    /**
     * Collects the files (files only) that are contained in the passed workflow or workflow group and are that are not
     * excluded. For workflows it does include all files contained in sub dirs (unless excluded).
     *
     * @param collector result mapping from local resources to export to target path
     * @param root root directory relative to which the destination path of the resources is determined
     * @param element the resource representing the thing to export
     * @throws IOException
     */
    private void addResourcesFor(final ResourcesCollector collector, final Path root, final Path element)
            throws IOException {

        // top-level elements are never execution data
        final var isData = false;

        if (Files.isRegularFile(element)) {
            // data files: add directly
            addResource(collector, root, element, Files.size(element), isData);
        } else if (Files.isRegularFile(element.resolve(WORKFLOW_FILE))) {
            // workflows, components and metanodes: add with optionally excluded data
            addRecursively(collector, root, element, isData, true);
        } else {
            // workflow groups: only add `workflowset.meta` if present
            final var metadata = element.resolve(METAINFO_FILE);
            if (Files.isRegularFile(metadata)) {
                addResource(collector, root, metadata, Files.size(metadata), isData);
            }
        }
    }

    @SuppressWarnings({"java:S134", "java:S135"}) // complexity acceptable
    private void addRecursively(final ResourcesCollector collector, final Path root, final Path dir,
            final boolean isData, final boolean isWFRoot) throws IOException {
        try (final var contents = Files.list(dir)) {
            final Path[] children = contents.toArray(Path[]::new);
            if (children.length == 0) {
                // see AP-13538 (empty dirs are ignored -- so we add them)
                addResource(collector, root, dir, -1, isData);
            }
            for (final var child : children) {
                final var childIsDir = Files.isDirectory(child);
                final var childIsExcluded = excludeResource(child, childIsDir);

                if (childIsExcluded && m_excludeData && !isMetaNode(child, dir)) {
                    // skip excluded data files and non-metanode directories
                    continue;
                }

                final var childIsData = isData || childIsExcluded;
                if (childIsDir) {
                    // the data folder contains user data, so it is data (not metadata) but not excluded
                    final var childIsWFDataDir = isWFRoot && child.getFileName().equals(Path.of(WORKFLOW_DATA_DIR));
                    addRecursively(collector, root, child, childIsData || childIsWFDataDir, false);
                } else if (Files.isRegularFile(child)) {
                    // Files to exclude on root level. Exclusion here is independent of the "exclude data" option.
                    if (child.getFileName().startsWith("knime.log")) {
                        continue;
                    }
                    addResource(collector, root, child, Files.size(child), childIsData);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Skipping unexpected item '{}' (neither file nor directory)", child);
                }
            }
        }
    }

    private static void addResource(final ResourcesCollector collector, final Path root, final Path resourcePath,
            final long size, final boolean isData) throws IOException {
        CheckUtils.checkArgument(root == null || resourcePath.startsWith(root), //
                "File '%s' is not below the root '%s'", resourcePath, root);
        IPath targetRelPath = IPath.EMPTY;
        for (final var segment : (root == null ? resourcePath : root.relativize(resourcePath).normalize())) {
            targetRelPath = targetRelPath.append(segment.toString());
        }
        collector.addResource(resourcePath, size, targetRelPath, isData);
    }

    /**
     * @param file file to check
     * @param parent parent of the file
     * @return true if this is a metanode (or a sub node) in a workflow (or metanode in another metanode, etc.)
     */
    private static boolean isMetaNode(final Path file, final Path parent) {
        return Files.isRegularFile(parent.resolve(WORKFLOW_FILE)) && Files.isDirectory(file)
                && Files.isRegularFile(file.resolve(WORKFLOW_FILE));
    }

    /**
     * Record of all resources to be copied into an archive.
     *
     * @param paths mapping from paths of all resources to copy to their respective path inside the archive
     * @param numFiles number of files (as opposed to directories) to copy
     * @param numBytes number of bytes of all files combined
     * @param markedEntry specially marked entry (may be {@code null}), used to mark the first non-metadata entry to
     *                    enable early abort
     */
    public record ResourcesToCopy(Map<Path, IPath> paths, int numFiles, long numBytes, Path markedEntry) {
        /** @since 6.11 */
        public ResourcesToCopy {}

        /**
         * Constructor without marked entry.
         *
         * @param paths mapping from paths of all resources to copy to their respective path inside the archive
         * @param numFiles number of files (as opposed to directories) to copy
         * @param numBytes number of bytes of all files combined
         */
        public ResourcesToCopy(final Map<Path, IPath> paths, final int numFiles, final long numBytes) {
            this(paths, numFiles, numBytes, null);
        }
    }

    /**
     * Exports the resources into the given output stream.
     *
     * @param resources resources to copy into export archive
     * @param outputStream stream to write the export archive to, will be closed in the end
     * @param updater progress updater
     * @throws E if execution was cancelled
     * @throws IOException if something went wrong with the export
     */
    public void exportInto(final ResourcesToCopy resources, final OutputStream outputStream,
            final FailableDoubleConsumer<E> updater)
            throws E, IOException {
        try (final var zipper = new Zipper(path -> Objects.equals(resources.markedEntry, path), outputStream)) {
            final var numBytesWritten = new AtomicLong();
            final FailableLongConsumer<E> subUpdater =
                add -> updater.accept(1.0 * numBytesWritten.addAndGet(add) / resources.numBytes());
            for (final var file : resources.paths().entrySet()) {
                zipper.addEntry(file.getKey(), file.getValue(), subUpdater);
            }
        }
    }

    private final class Zipper implements Closeable {

        private static final ZipExtraField MARKER_EXTRA_FIELD;
        static {
            final var extraField = new UnrecognizedExtraField();
            extraField.setHeaderId(new ZipShort(MARKER_HEADER_ID));
            extraField.setLocalFileDataData(new byte[] { 0x01 });
            MARKER_EXTRA_FIELD = extraField;
        }

        private static final int BUFFER_SIZE = 64 * (int)FileUtils.ONE_KB;

        private final byte[] m_buffer = new byte[BUFFER_SIZE];

        private final Predicate<Path> m_toBeMarked;

        private @Owning ZipArchiveOutputStream m_zipOutStream;

        @SuppressWarnings("resource") // missing `@Owning` annotations on `ZipArchiveOutputStream::new`
        Zipper(final Predicate<Path> toBeMarked, @Owning final OutputStream outputStream) {
            m_toBeMarked = toBeMarked;
            m_zipOutStream = new ZipArchiveOutputStream(new BufferedOutputStream(outputStream, BUFFER_SIZE));
        }

        void addEntry(final Path source, final IPath destination, final FailableLongConsumer<E> updater)
                throws IOException, E {
            final ZipArchiveEntry entry = createZipEntry(source, destination);
            if (entry.isDirectory() || entry.getSize() == 0) {
                // the empty-file condition is mainly for the .knimeLock file of open workflows; the file is locked and
                // windows forbids mmap-ing locked files but FileInputStream seems to mmap files which leads to
                // exceptions while reading the (non-existing) contents of the file
                m_zipOutStream.putArchiveEntry(entry);
                m_zipOutStream.closeArchiveEntry();
                return;
            }

            try (final var inStream = new BufferedInputStream(Files.newInputStream(source), BUFFER_SIZE)) {
                m_zipOutStream.setLevel(entry.getSize() > FileUtils.ONE_KB && isAlreadyCompressed(inStream)
                    ? Deflater.NO_COMPRESSION : Deflater.BEST_COMPRESSION);

                m_zipOutStream.putArchiveEntry(entry);
                for (int read; (read = inStream.read(m_buffer)) >= 0;) {
                    m_zipOutStream.write(m_buffer, 0, read);
                    updater.accept(read);
                }
                m_zipOutStream.closeArchiveEntry();
            } catch (final IOException ioe) {
                throw new IOException(String.format("Unable to add file \"%s\" to archive: %s",
                    source.toAbsolutePath(), ioe.getMessage()), ioe);
            }
        }

        private ZipArchiveEntry createZipEntry(final Path source, final IPath destination) throws IOException {
            final var isDir = Files.isDirectory(source);
            final var name = isDir ? destination.addTrailingSeparator() : destination;
            final var entry = new ZipArchiveEntry(name.toString());

            // set size explicitly to enable per-item ZIP64 detection
            entry.setSize(isDir ? 0 : Files.size(source));

            if (m_toBeMarked.test(source)) {
                entry.addExtraField(MARKER_EXTRA_FIELD);
            }

            return entry;
        }

        private static final byte[][] COMPRESSED_DATA_MAGIC_BYTES = { //
            { 'P', 'K', 0x03, 0x04 }, // ZIP
            { 'A', 'R', 'R', 'O', 'W', '1' } // ARROW
        };

        private static boolean isAlreadyCompressed(final BufferedInputStream inStream) throws IOException {
            inStream.mark(6);
            final var bytes = inStream.readNBytes(6);
            inStream.reset();
            return Arrays.stream(COMPRESSED_DATA_MAGIC_BYTES) //
                    .anyMatch(mb -> bytes.length >= mb.length && Arrays.equals(bytes, 0, mb.length, mb, 0, mb.length));
        }

        @SuppressWarnings("resource") // `@Owning` analysis doesn't understand try-with-resources
        @Override
        public void close() throws IOException {
            try (final var zipOut = m_zipOutStream) {
                m_zipOutStream = null;
            }
        }
    }
}
