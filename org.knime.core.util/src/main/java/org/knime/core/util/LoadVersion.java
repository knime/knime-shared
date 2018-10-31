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
 *   Oct 9, 2018 (awalter): created
 */
package org.knime.core.util;

import java.util.Optional;

/**
 * A Version representing a specific workflow format. This enum covers only the version that this specific class can
 * read (or write). Ordinal numbering is important.
 *
 * <p>
 * Extracted from FileWorkflowPersistor in knime-core.
 * </p>
 * @since 5.10
 */
public enum LoadVersion {
        // Don't modify order, ordinal number are important.
        /** Pre v2.0. */
        UNKNOWN("<unknown>"),
        /** Version 2.0.0 - 2.0.x. */
        V200("2.0.0"),
        /**
         * Trunk version when 2.0.x was out, covers cluster and server prototypes. Obsolete since 2009-08-12.
         */
        V210_Pre("2.0.1"),
        /** Version 2.1.x. */
        V210("2.1.0"),
        /**
         * Version 2.2.x, introduces optional inputs, flow variable input credentials, node local drop directory.
         */
        V220("2.2.0"),
        /** Version 2.3.x, introduces workflow annotations &amp; switches. */
        V230("2.3.0"),
        /** Version 2.4.x, introduces metanode templates. */
        V240("2.4.0"),
        /** Version 2.5.x, lockable metanodes, node-relative annotations. */
        V250("2.5.0"),
        /**
         * Version 2.6.x, file store objects, grid information, node vendor &amp; plugin information.
         *
         * @since 2.6
         */
        V260("2.6.0"),
        /**
         * node.xml and settings.xml are one file, settings in SNC, meta data in workflow.knime.
         *
         * @since 2.8
         */
        V280("2.8.0"),
        /**
         * basic subnode support, never released (trunk code between 2.9 and 2.10).
         *
         * @since 2.10
         */
        V2100Pre("2.10.0Pre"),
        /**
         * better subnode support, PortObjectHolder, FileStorePortObject w/ array file stores.
         *
         * @since 2.10
         */
        V2100("2.10.0"),
        /**
         * Subnode outputs as port object holder, "VoidTable".
         *
         * @since 3.1
         */
        V3010("3.1.0"),
        /**
         * Different table formats (col store).
         *
         * @since 3.6
         */
        V3060Pre("3.6.0Pre"),
        /**
         * Add support for multiple FileStores in FileStoreCell.
         *
         * @since 3.7
         */
        V3070("3.7.0"),
        /**
         * Try to be forward compatible.
         *
         * @since 2.8
         */
        FUTURE("<future>");

    private final String m_versionString;

    private LoadVersion(final String str) {
        m_versionString = str;
    }

    /** @return The String representing the LoadVersion (workflow.knime). */
    public String getVersionString() {
        return m_versionString;
    }

    /**
     * Is this' ordinal smaller than this ordinal of the argument? For instance
     * getLoadVersion().isOlderThan(LoadVersion.V200) means we are loading a workflow older than 2.0
     *
     * @param version compare version
     * @return that property
     */
    public boolean isOlderThan(final LoadVersion version) {
        return ordinal() < version.ordinal();
    }

    /**
     * Get the load version for the version string or an empty optional if unknown.
     *
     * @param string Version string (as in workflow.knime).
     * @return The LoadVersion as Optional
     */
    public static Optional<LoadVersion> get(final String string) {
        for (LoadVersion e : values()) {
            if (e.m_versionString.equals(string)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

}
