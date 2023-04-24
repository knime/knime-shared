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
 *   Apr 24, 2023 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * Test parsing encrypted components and items which contain encrypted components.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
class WorkflowalizerEncryptedComponentTest extends AbstractWorkflowalizerTest {

    private static final String COMPONENT_ICON_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAADoAAABACAYAAABLAmSPAAAA+ElEQVR4nO2Uy"
        + "Q0CQRADOxSiIVk+xEI8SPDqJ7tjxp5j2yXND6u6tBIR8/kcvEvh0JlHKdgm9Hnyztgm9OjQlmMdqji2B4eGQ5v3S+HQcGjzXu2GcCjhWLUb"
        + "YptQ5aEObdxT3Q5VyMA91e1QhQzcU90OVcjAPdXtUIUM3FPdDlXIwD3V7VCFDNxT3Q5VyMC92g3hUMKxajeEQwnHqt0QM0OH4tBwaPN+KUa"
        + "GDv3zYcsdKnB1USa0F4eCv3XoKpQJRXBoOHRtzoLQtzSsyPvow//h8l8zeUWByKREZFIiMikRmZSITH5FPmYepeAWBb5m8o4CkUmJSJgvE0"
        + "6l+i/bzCsAAAAASUVORK5CYII=";

    /**
     * Tests parsing a workflow which contains an encrypted component.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testParseWorkflowWithEncryptedComponent47() throws Exception {
        var workflowMetadata = Workflowalizer.readWorkflow(Path.of(WorkflowalizerEncryptedComponentTest.class
            .getResource("/encrypted-components/Workflow-with-encrypted-component-4.7.knwf").toURI()));

        assertThat("Unexpected value when checking if workflow contains an encrypted component",
            workflowMetadata.containsEncrypted(), is(true));
        // 3 native nodes, and 1 encrypted component
        // the encrypted component contains an additional two native nodes which cannot be read
        assertThat("Unexpected number of nodes in workflow", workflowMetadata.flatten().getNodes().size(), is(4));
    }

    /**
     * Tests that parsing a workflow which does not contain any encrypted elements returns the correct boolean value.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testParseWorkflowDoesntContainEcryptedComponent() throws Exception {
        var workflowMetadata = Workflowalizer.readWorkflow(
            Path.of(WorkflowalizerEncryptedComponentTest.class.getResource("/simple-workflow.zip").toURI()));

        assertThat("Unexpected value when workflow does not contain any encrypted components",
            workflowMetadata.containsEncrypted(), is(false));
    }

    /**
     * Tests parsing an encrypted shared component.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testParseEncryptedComponent47() throws Exception {
        var componentMetadata = Workflowalizer.readTemplate(Path.of(WorkflowalizerEncryptedComponentTest.class
            .getResource("/encrypted-components/Encrypted-Component-4.7.knwf").toURI()));

        assertThat("Unexpected value when check if component contains an encrypted component",
            componentMetadata.containsEncrypted(), is(true));
        assertThat("Unexpected encryption value", componentMetadata.getEncryption(), is(Encryption.WEAK));
        // 0 nodes, since this is an encrypted component we can't read any of the nodes in the component
        assertThat("Unexpected number of nodes", componentMetadata.flatten().getNodes().size(), is(0));
    }

    /**
     * Tests parsing a shared component which contains an encrypted component.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testParseComponentContainingEncryptedComponent() throws Exception {
        var templateMetadata = Workflowalizer.readTemplate(Path.of(WorkflowalizerEncryptedComponentTest.class
            .getResource("/encrypted-components/Component-containing-encrypted-component.knwf").toURI()));

        assertThat("Unexpected value when check if component contains an encrypted component",
            templateMetadata.containsEncrypted(), is(true));
        assertThat("Unexpected encryption value", templateMetadata.getEncryption(), is(Encryption.NONE));
        // 2 native nodes, 1 encrypted component
        // the encrypted component contains 2 native nodes which can't be parsed
        assertThat("Unexpected number of nodes", templateMetadata
            .flatten(
                List.of("^org\\.knime\\.core\\.node\\.workflow\\.virtual\\.subnode\\.VirtualSubNodeInputNodeFactory.*",
                    "^org\\.knime\\.core\\.node\\.workflow\\.virtual\\.subnode\\.VirtualSubNodeOutputNodeFactory.*"))
            .getNodes().size(), is(3));

        var componentMetadata = (ComponentMetadata)templateMetadata;
        testComponentFields(componentMetadata,
            List.of(new ComponentPortInfo(Optional.of("First and only inport"), Optional.of("inport 1"),
                "org.knime.core.node.BufferedDataTable")),
            List.of(), Optional.of("Component description"), Optional.of(COMPONENT_ICON_BASE64),
            Optional.of("LEARNER"));
    }

    /**
     * Tests parsing a shared component which isn't encrypted.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testParseNonEncryptedComponent() throws Exception {
        var componentMetadata = Workflowalizer.readTemplate(
            Path.of(WorkflowalizerEncryptedComponentTest.class.getResource("/component-template-simple.zip").toURI()));

        assertThat("Unexpected value when check if component contains an encrypted component",
            componentMetadata.containsEncrypted(), is(false));
        assertThat("Unexpected encryption value", componentMetadata.getEncryption(), is(Encryption.NONE));
    }

    // -- Helper methods --

    private static void testComponentFields(final ComponentMetadata componentMetadata,
        final List<ComponentPortInfo> expectedInports, final List<ComponentPortInfo> expectedOutports,
        final Optional<String> expectedDesc, final Optional<String> expectedIcon, final Optional<String> expectedType) {
        testPortFields(componentMetadata.getInPorts(), expectedInports, "inport");
        testPortFields(componentMetadata.getOutPorts(), expectedOutports, "outport");
        assertThat("Unexpected component description", componentMetadata.getDescription(), is(expectedDesc));
        assertThat("Unexpected component icon", componentMetadata.getIcon(), is(expectedIcon));
        assertThat("Unexpected component type", componentMetadata.getComponentType(), is(expectedType));
    }

    private static void testPortFields(final List<ComponentPortInfo> ports, final List<ComponentPortInfo> expectedPorts,
        final String portType) {
        assertThat("Unexpected %s size".formatted(portType), ports.size(), is(expectedPorts.size()));

        for (int i = 0; i < ports.size(); i++) {
            var port = ports.get(i);
            var expectedPort = expectedPorts.get(i);
            assertThat("Unexpected %s name".formatted(portType), port.getName(), is(expectedPort.getName()));
            assertThat("Unexpected %s description".formatted(portType), port.getDescription(),
                is(expectedPort.getDescription()));
            assertThat("Unexpected %s object class".formatted(portType), port.getObjectClass(),
                is(expectedPort.getObjectClass()));
        }
    }
}
