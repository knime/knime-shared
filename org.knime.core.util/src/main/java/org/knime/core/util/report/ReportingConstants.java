/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
 *   Aug 10, 2010 (morent): created
 */
package org.knime.core.util.report;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A bunch of static constants that are required for report generation. They are
 * used in open source code (for instance "call local workflow") and also on the
 * KNIME Server.
 *
 * @since 5.6
 *
 * @author Dominik Morent, KNIME.com, Zurich, Switzerland
 */
public final class ReportingConstants {

    private ReportingConstants() {
        // hide constructor of utility class
    }

    /** Name of the report design file. */
    public static final String KNIME_REPORT_FILE = "default_report.rptdesign";

    /** Name of the report config file (this contains parameters for the report.) */
    public static final String KNIME_REPORT_CONFIG_FILE = "default_report.rptconfig";

    /**
     * The parameter name for the report. DO NOT change this name as it is required by BIRT.
     */
    public static final String REPORT_PARAM = "__report";

    /** The parameter name for the user name. */
    public static final String USERNAME_PARAM = "knime-username";

    /** The parameter name for the password. */
    public static final String PASSWORD_PARAM = "knime-password";

    /** The parameter name for the credentials. */
    public static final String CREDENTIALS_PARAM = "report-credentials";

    /** The parameter suffix for the flag to save the credentials. */
    public static final String SAVE_SUFFIX = "_knime-save";

    /**
     * The prefix for all default BIRT emitters.
     *
     * @since 3.9
     */
    public static final String STANDARD_EMITTER_ID = "org.eclipse.birt.report.engine.emitter.";

    /**
     * The prefix for Arctorus BIRT emitter plugin and emitter ids.
     *
     * @since 3.9
     */
    public static final String ARCTORUS_EMITTER_ID = "com.arctorus.export.birt37x.";

    /** Supported formats for generated reports. */
    public enum RptOutputFormat {
        /** PDF format. */
        PDF("pdf", "pdf-icon.jpg", "application/pdf"),
        /** HTML format. */
        HTML("html", "html-icon.jpg", "text/html"),
        /** MS Office Word. */
        DOC("doc", "doc-icon.jpg", "application/msword"),
        /**
         * Microsoft Office Word 2007 document.
         *
         * @since 3.9
         */
        DOCX("docx", "doc-icon.jpg", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        /** MS Office XLS format. */
        XLS("xls", "xls-icon.jpg", "application/excel"),
        /**
         * Microsoft Office Excel 2007 workbook.
         *
         * @since 3.9
         */
        XLSX("xlsx", "xls-icon.jpg", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        /** MS Office Powerpoint. */
        PPT("ppt", "ppt-icon.jpg", "application/powerpoint"),
        /**
         * Microsoft Office PowerPoint 2007 presentation.
         *
         * @since 3.9
         */
        PPTX("pptx", "ppt-icon.jpg", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        /**
         * Postscript.
         *
         * @since 3.5
         */
        PS("ps", "ps-icon.jpg", "application/postscript"),
        /**
         * Open document presentation.
         *
         * @since 3.6
         */
        ODP("odp", "odp-icon.jpg", "application/vnd.oasis.opendocument.presentation"),
        /**
         * Open document text.
         *
         * @since 3.6
         */
        ODT("odt", "odt-icon.jpg", "application/vnd.oasis.opendocument.text"),
        /**
         * Open document spreadsheet.
         *
         * @since 3.6
         */
        ODS("ods", "ods-icon.jpg", "application/vnd.oasis.opendocument.spreadsheet"),
        /**
         * CSV files.
         *
         * @since 3.9
         */
        CSV("csv", "xls-icon.jpg", "text/comma-separated-values");

        private static final Map<String, RptOutputFormat> NAMES = new HashMap<String, RptOutputFormat>();

        private static final Map<String, RptOutputFormat> EXTENSIONS = new LinkedHashMap<>();

        private final String m_extension;

        private final String m_icon;

        private final String m_mimeType;

        static {
            for (RptOutputFormat format : values()) {
                NAMES.put(format.toString(), format);
                EXTENSIONS.put(format.getExtension(), format);
            }
        }

        private RptOutputFormat(final String extension, final String icon, final String mimeType) {
            m_extension = extension;
            m_icon = icon;
            m_mimeType = mimeType;
        }

        /**
         * @return the extension
         */
        public String getExtension() {
            return m_extension;
        }

        /**
         * @return the icon
         */
        public String getIcon() {
            return m_icon;
        }

        /**
         * @return the mimeType
         */
        public String getMimeType() {
            return m_mimeType;
        }

        /**
         * @param name the name of the format
         * @return the RptOutput format for the name (or null)
         */
        static RptOutputFormat getByString(final String name) {
            return NAMES.get(name);
        }

        /**
         * @param extension the file name extension of the format
         * @return the RptOutput format for the extension (or null)
         */
        public static RptOutputFormat getByExtension(final String extension) {
            return EXTENSIONS.get(extension.toLowerCase());
        }

        /**
         * @return all possible extensions (from different formats) in an array, not null.
         * @since 3.7
         */
        public static String[] getAvailableExtensions() {
            return EXTENSIONS.keySet().toArray(new String[EXTENSIONS.size()]);
        }

    }

    /**
     * Counterpart to birt's RenderOption class. Passed during rendering generation, currently used to specify html
     * image directories.
     */
    public static class RptOutputOptions implements Serializable {
        private static final long serialVersionUID = 1785732712075844025L;

        private final Map<String, String> m_options;

        /**
         * Creates a new instance.
         */
        public RptOutputOptions() {
            m_options = new LinkedHashMap<String, String>();
        }

        /**
         * Sets the specified option. (@see org.eclipse.birt.report.engine.api.IRenderOption)
         *
         * @param name the name of the option
         * @param value the value of the option
         */
        public void setOption(final String name, final String value) {
            m_options.put(name, value);
        }

        /**
         * @param name the name of the option to retrieve
         * @return the options value
         */
        public String getOption(final String name) {
            return m_options.get(name);
        }

        /**
         * @return a map containing all options mapping option names to values
         */
        public Map<String, String> getOptions() {
            return Collections.unmodifiableMap(m_options);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return m_options.toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((m_options == null) ? 0 : m_options.hashCode());
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            RptOutputOptions other = (RptOutputOptions)obj;
            return Objects.equals(m_options, other.m_options);
        }
    }

    /**
     * This class is an extension of {@link RptOutputOptions} which is specialized on HTML output.
     */
    public static final class HtmlRptOutputOptions extends RptOutputOptions {

        private static final long serialVersionUID = -2904800080384269813L;

        private final String m_baseUrl;

        private final String m_imageDirectory;

        private boolean m_embedImages = false;

        /**
         * @param baseUrl base url
         * @param imageDirectory image directory path
         */
        public HtmlRptOutputOptions(final String baseUrl, final String imageDirectory) {
            m_baseUrl = baseUrl;
            m_imageDirectory = imageDirectory;
        }

        /** @return the baseUrl */
        public String getBaseUrl() {
            return m_baseUrl;
        }

        /** @return the imageDirectory */
        public String getImageDirectory() {
            return m_imageDirectory;
        }

        /**
         * @return If report images are supposed to be embedded in the resulting HTML.
         * @since 3.8
         */
        public boolean isEmbedImages() {
            return m_embedImages;
        }

        /**
         * @param embedImages If report images are supposed to be embedded in the resulting HTML.
         * @since 3.8
         */
        public void setEmbedImages(final boolean embedImages) {
            m_embedImages = embedImages;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((m_baseUrl == null) ? 0 : m_baseUrl.hashCode());
            result = prime * result + (m_embedImages ? 1231 : 1237);
            result = prime * result + ((m_imageDirectory == null) ? 0 : m_imageDirectory.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            HtmlRptOutputOptions other = (HtmlRptOutputOptions)obj;
            if (m_baseUrl == null) {
                if (other.m_baseUrl != null) {
                    return false;
                }
            } else if (!m_baseUrl.equals(other.m_baseUrl)) {
                return false;
            }
            if (m_embedImages != other.m_embedImages) {
                return false;
            }
            if (m_imageDirectory == null) {
                if (other.m_imageDirectory != null) {
                    return false;
                }
            } else if (!m_imageDirectory.equals(other.m_imageDirectory)) {
                return false;
            }
            return true;
        }
    }

    /**
     * RptOutputOptions to be used when Arctorus BIRT emitters are installed.
     *
     * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland
     * @since 3.9
     */
    public static final class ArctorusRptOutputOptions extends RptOutputOptions {

        private static final long serialVersionUID = 8023093658489883063L;

        private final boolean m_supportsDOC;

        private final boolean m_supportsXLS;

        private final boolean m_supportsPPT;

        /**
         * @param supportsDOC true if Arctorus DOC, DOCX and EPUB BIRT emitters are installed
         * @param supportsXLS true if Arctorus XLSX, XLS, ODS and CSV BIRT emitters are installed
         * @param supportsPPT true if Arctorus PPTX BIRT emitters are installed
         *
         */
        public ArctorusRptOutputOptions(final boolean supportsDOC, final boolean supportsXLS, final boolean supportsPPT) {
            m_supportsDOC = supportsDOC;
            m_supportsXLS = supportsXLS;
            m_supportsPPT = supportsPPT;
        }

        /**
         * @return true if Arctorus DOC, DOCX and EPUB BIRT emitters are installed
         */
        public boolean getSupportsDOC() {
            return m_supportsDOC;
        }

        /**
         * @return true if Arctorus XLSX, XLS, ODS and CSV BIRT emitters are installed
         */
        public boolean getSupportsXLS() {
            return m_supportsXLS;
        }

        /**
         * @return true if Arctorus PPTX BIRT emitters are installed
         */
        public boolean getSupportsPPT() {
            return m_supportsPPT;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (m_supportsDOC ? 1231 : 1237);
            result = prime * result + (m_supportsPPT ? 1231 : 1237);
            result = prime * result + (m_supportsXLS ? 1231 : 1237);
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ArctorusRptOutputOptions other = (ArctorusRptOutputOptions)obj;
            if (m_supportsDOC != other.m_supportsDOC) {
                return false;
            }
            if (m_supportsPPT != other.m_supportsPPT) {
                return false;
            }
            if (m_supportsXLS != other.m_supportsXLS) {
                return false;
            }
            return true;
        }
    }

    /**
     * Creates default reporting options for the desired report format.
     *
     * @param format the desired report format
     * @param additionalReportFormats
     * @return the default reporting options
     * @since 4.1
     */
    public static RptOutputOptions getDefaultReportOptions(final RptOutputFormat format,
        final Collection<String> additionalReportFormats) {
        RptOutputOptions renderOptions = new RptOutputOptions();
        if (RptOutputFormat.HTML.equals(format)) {
            renderOptions = new HtmlRptOutputOptions(null, null);
            ((HtmlRptOutputOptions)renderOptions).setEmbedImages(true);
        }

        return renderOptions;
    }
}
