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
 *   Oct 27, 2022 (leonard.woerteler): created
 */
package org.knime.core.node.workflow.contextv2;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.Test;

/**
 * Tests for {@link WorkflowContextV2Test}.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 */
public class WorkflowContextV2Test {

    private static final Path ROOT = Path.of("/").toAbsolutePath();

    private static final String MOUNTPOINT_ROOT = "/home/jenkins/workspace/ontext-v-2-in-analytics-platform/tmp/"
        + "knime_temp/tempTestRootDirs13515/workflows";

    private static final String WORKFLOW_PATH = MOUNTPOINT_ROOT + "/Testflows (master)/knime-core/WorkflowManager/"
            + "AP-19485_WorkflowContext_URL_Resolve_Streaming/AP-19485_WorkflowContext_inStreaming";

    private static final URI MOUNTPOINT_URI = URI.create("knime://LOCAL/Testflows%20(master)/knime-core/WorkflowManager"
        + "/AP-19485_WorkflowContext_URL_Resolve_Streaming/AP-19485_WorkflowContext_inStreaming");

    /** Creates a long mountpoint URI which includes spaces and parentheses. */
    @Test
    public void testMountpointUri() {
        final var ctx = WorkflowContextV2.builder()
                .withAnalyticsPlatformExecutor(exec -> exec
                    .withCurrentUserAsUserId()
                    .withLocalWorkflowPath(ROOT.resolve(WORKFLOW_PATH))
                    .withMountpoint("LOCAL", ROOT.resolve(MOUNTPOINT_ROOT)))
                .withLocalLocation()
                .build();

        assertEquals("Mountpoint URI is not resolved correctly.", Optional.of(MOUNTPOINT_URI), ctx.getMountpointURI());
    }
}
