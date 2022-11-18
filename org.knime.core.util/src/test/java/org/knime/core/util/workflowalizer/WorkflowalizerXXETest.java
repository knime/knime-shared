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
 *   Nov 17, 2022 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.knime.core.util.PathUtils;

/**
 * Tests for XXE vulnerabilities in the workflowalizer parsing.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
public class WorkflowalizerXXETest extends AbstractWorkflowalizerTest {

    /**
     * Tests for XXE in a workflow SVG from a workflow which has been extracted to the filesystem (i.e. unzipped).
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testXxeInSvg() throws Exception {
        Path workspace = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (InputStream is = WorkflowalizerXXETest.class.getResourceAsStream("/xxe/XXE_SVG.knwf")) {
            unzip(is, workspace.toFile());
        }
        Path workflowDir = new File(workspace.toFile(), "XXE_SVG").toPath();
        testXxeExceptionThrown(workflowDir, "workflow.svg");
    }

    /**
     * Tests for XXE in a workflow SVG in a workflow knwf.
     *
     * @throws URISyntaxException if URI syntax error occurs
     */
    @Test
    public void testXxeInSvgZip() throws URISyntaxException {
        Path workflowPath = Paths.get(WorkflowalizerXXETest.class.getResource("/xxe/XXE_SVG.knwf").toURI());
        testXxeExceptionThrown(workflowPath, "workflow.svg");
    }

    /**
     * Tests for XXE in workflowset.meta files in a workflow which has been extracted to the filesystem (i.e. unzipped).
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testXxeInWorkflowSetMeta() throws Exception {
        Path workspace = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (InputStream is = WorkflowalizerXXETest.class.getResourceAsStream("/xxe/XXE_WorkflowSetMeta.knwf")) {
            unzip(is, workspace.toFile());
        }
        Path workflowDir = new File(workspace.toFile(), "XXE_WorkflowSetMeta").toPath();
        testXxeExceptionThrown(workflowDir, "workflowset.meta");
    }

    /**
     * Tests for XXE in wokflowset.meta files which are part of a workflow knwf.
     *
     * @throws URISyntaxException
     */
    @Test
    public void testXxeInWorkflowSetMetaInZip() throws URISyntaxException {
        Path workflowPath = Paths.get(WorkflowalizerXXETest.class.getResource("/xxe/XXE_WorkflowSetMeta.knwf").toURI());
        testXxeExceptionThrown(workflowPath, "workflowset.meta");
    }

    // -- Helper methods --

    private static void testXxeExceptionThrown(final Path path, final String fileName) {
        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> Workflowalizer.readRepositoryItem(path));
        assertThat("Unexpected error message when preventing XXE", ex.getMessage(), startsWith(String
            .format("Cannot parse the given file '%s', as it contains XML elements which are not allowed", fileName)));
    }
}
