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
 *   Feb 7, 2019 (wiswedel): created
 */
package org.knime.core.util;

/**
 * Constants used in the KNIME framework. Constants are shared in the KNIME AP code and other, non-OSGI components
 * (server).
 *
 * <p>
 * Class and fields are not meant as public API and can change between releases.
 *
 * @noreference This class is not intended to be referenced by clients.
 *
 * @author Bernd Wiswedel, KNIME AG, Zurich, Switzerland
 *
 * @since 5.11
 */
public final class CoreConstants {

    private CoreConstants() {
    }

    /**
     * Identifier of a credentials object whose user/pass field should be pre-filled with the login information.
     * Currently used when the user logs in on the server web portal and in API hooks in KNIME AP (AP-11261).
     */
    public static final String CREDENTIALS_KNIME_SYSTEM_DEFAULT_ID = "knime.system.default";

    /**
     * The id of the examples mount point.
     */
    public static final String KNIME_EXAMPLES_MOUNT_ID = "EXAMPLES";

    /**
     * The id of the KNIME Hub mount point.
     */
    public static final String KNIME_HUB_MOUNT_ID = "My-KNIME-Hub";

    /** JSON field prefix for the workflow credentials. */
    public static final String WORKFLOW_CREDENTIALS = "knime.cred.";

    /** JSON field prefix for the workflow variables. */
    public static final String WORKFLOW_VARIABLES = "knime.var.";

    /**
     * Enum used to differentiate between configuration template and configuration representation.
     *
     * @author Moritz Heine, KNIME GmbH, Konstanz, Germany
     */
    public static enum ConfigurationType {
        /**
         * Denotes the configuration template of a workflow containing the configuration parameter names and their
         * default values.
         */
        CONFIGURATION_TEMPLATE("workflow-configuration.json"),
        /**
         * Denotes the configuration representation, i.e. the serialized dialog representations of configuration
         * nodes.
         */
        CONFIGURATION_REPRESENTATION("workflow-configuration-representation.json");

        private final String m_fileName;

        private ConfigurationType(final String path) {
            m_fileName = path;
        }

        /**
         * Returns the name of the file that is stored in the artifacts folder containing the json of this configuration
         * type.
         *
         * @return the path to the artifacts file containing the configuration of the specified type.
         */
        public String getFileName() {
            return m_fileName;
        }
    }

    /**
     * Since we don't expose the real password to the world, we need to send a magic placeholder instead. When sent back
     * to the node it indicates that the default password from the node settings should be used.
     */
    public static final String MAGIC_DEFAULT_PASSWORD = "*************";

    /**
     * The default view for serializing Jackson Annotated classes. Doesn't need to be specified explicitly because
     * properties without view annotation are always serialized.
     */
    public interface DefaultView {
    }

    /**
     * A view for serializing Jackson Annotated classes used to serialize Configuration nodes representations. This view
     * is meant to be used when sensitive data (e.g. passwords) shouldn't be serialized. The sensitive data should have
     * Jackson Annotation that only allows {@link DefaultView} to serialize it.
     */
    public interface ArtifactsView {
    }
}
