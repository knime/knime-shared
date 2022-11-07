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
 *   Aug 26, 2022 (bjoern): created
 */
package org.knime.core.node.workflow.contextv2;

import java.net.URI;
import java.util.Objects;

import org.knime.core.node.workflow.contextv2.RestLocationInfoBuilderFactory.RestLocationInfoReqRABuilder;
import org.knime.core.node.workflow.contextv2.ServerLocationInfoBuilderFactory.ServerLocationInfoBuilder;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.auth.Authenticator;

/**
 * Provides information about a KNIME Server repository (a REST endpoint) that holds the current workflow. This class
 * exists only for typing purposes and provides no additional fields on top of its superclass {@link RestLocationInfo}.
 * Please see the Javadoc of {@link RestLocationInfo}.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @see RestLocationInfo
 * @since 4.7
 */
public class ServerLocationInfo extends RestLocationInfo {

    /**
     * Creates a fluent builder for {@link ServerLocationInfo} instances.
     *
     * @return new builder
     */
    public static RestLocationInfoReqRABuilder<ServerLocationInfoBuilder> builder() {
        return ServerLocationInfoBuilderFactory.create();
    }

    ServerLocationInfo( //
            final URI repositoryAddress, //
            final Authenticator authenticator, //
            final String workflowPath, //
            final String defaultMountId) {
        super(LocationType.SERVER_REPOSITORY, repositoryAddress, authenticator, workflowPath, defaultMountId);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final ServerLocationInfo that = (ServerLocationInfo)other;
        return Objects.equals(getType(), that.getType())
                && Objects.equals(getRepositoryAddress(), that.getRepositoryAddress())
                && Objects.equals(getAuthenticator(), that.getAuthenticator())
                && Objects.equals(getWorkflowPath(), that.getWorkflowPath())
                && Objects.equals(getDefaultMountId(), that.getDefaultMountId());
    }

    @Override
    public int hashCode() {
        return Objects.hash( //
            getType(), //
            getRepositoryAddress(), //
            getAuthenticator(), //
            getWorkflowPath(), //
            getDefaultMountId());
    }
}
