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
 *   Nov 26, 2019 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.knime.core.util.LoadVersion;
import org.knime.core.util.Pair;
import org.knime.core.util.PathUtils;
import org.knime.core.util.workflowalizer.RepositoryItemMetadata.ContentType;

/**
 * Tests the {@link Workflowalizer} for {@link LoadVersion#V5100}.
 *
 * <p>
 * The intention of these tests is to ensure the Workflowalizer can parse 5.1.0 items, specifically the changes in
 * AP-20406: Workflow and SubnodeContainer metadata moved into separate files.
 * </p>
 * <p>
 * <ul>
 * <li>Reading workflow metadata from the workflow-metadata.xml</li>
 * <li>Reading shared component metadata from the component-metadata.xml</li>
 * </ul>
 * </p>
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public class Workflowalizer510Test extends AbstractWorkflowalizerTest {

    /**
     * The directory containing the test workflow. The workflow is unzipped from the workflow510.zip file.
     * The workflow has the following metadata:
     * <pre>
            author: Carl Witt
            created: 2023-06-22T14:00:00.000+01:00
            lastModified: 2023-06-22T15:00:00.000+01:00
            description: This is a <b>test</b> workflow.
            tags: ["tags, good", "tag2"]
            links: [(https://knime.com, knime), (https://www.knime.com, www.knime.com)]
            contentType: text/html
        </pre>
     */
    private static Path workflowDir;

    /**
     * The directory containing the test component. The component is unzipped from the component510.zip file.
     * The component has the following metadata:
     * <pre>
     *      author: Carl Witt
     *      created: 2023-06-22T14:00:00.000+01:00
     *      lastModified: 2023-06-22T15:00:00.000+01:00
     *      description: This is a <b>test</b> component.
     *      tags: ["useful", "nice"]
     *      links: [(https://knime.com, knime), (https://www.knime.com, null)]
     *      contentType: text/html
     *      componentType: Learner
     *      icon: iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAhElEQVR4nGNgoDIox4HzgJiVGAP+A3EHFgwS
     *            X0+MIf/xiK8nxhB8BtQA8UeoISQbgO4dkg1gBGJ+AmpQJEWBOBdJvAWIjwAxBzEGcEAVfwbiBCi+DcT9QCxL
     *            jAEgRZ1ArAHEL6BYHYsr8XoBBhygGJ8a4iXpZgCu/ADDeA3Alg+wYeoBAO4gNc6sqU+FAAAAAElFTkSuQmCC
     *      inPortNames: ["train", "test"]
     *      inPortDescriptions: ["training data", "test data"]
     *      outPortNames: ["model"]
     *      outPortDescriptions: ["trained model"]
     * </pre>
     */
    private static Path componentDir;

    private static final String Version510 = "5.1.0";

    /**
     * Unzip test workflow and component.
     *
     * @throws Exception
     */
    @BeforeAll
    public static void setup() throws Exception {
        final Path wd = PathUtils.createTempDir(Workflowalizer510Test.class.getName());
        try (InputStream is = getResourceAsStream("/workflow510.zip")) {
            unzip(is, wd);
        }
        workflowDir = wd.resolve("WorkflowWithMetadata");

        final Path cd = PathUtils.createTempDir(Workflowalizer510Test.class.getName());
        try (final InputStream is = getResourceAsStream("/component510.zip")) {
            unzip(is, cd);
        }
        componentDir = cd.resolve("ComponentWithMetadata");
    }

    /**
     * Tests that 5.1.0 workflow metadata is parsed correctly.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReadingWorkflowMetadata() throws Exception {
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir);
        assertEquals(Version510, wm.getVersion().toString());
        assertEquals("WorkflowWithMetadata", wm.getName());
        testStructure(wm, workflowDir.resolve("workflow.knime"));

        final var workflowMetadata = wm.getWorkflowSetMetadata().orElseThrow();
        assertEquals(ContentType.TEXT_HTML, workflowMetadata.getContentType());
        assertEquals("Carl Witt", workflowMetadata.getAuthor().orElseThrow());
        assertEquals(14, workflowMetadata.getCreated().orElseThrow().getHour());
        assertEquals(15, workflowMetadata.getLastModified().orElseThrow().getHour());
        assertEquals("This is a <b>test</b> workflow.", workflowMetadata.getDescription().orElseThrow());
        assertEquals(List.of("tags, good", "tag2"), workflowMetadata.getTags().orElseThrow());
        assertEquals(
            List.of(new WorkflowSetMeta.Link("https://knime.com", "knime"),
                new WorkflowSetMeta.Link("https://www.knime.com", "www.knime.com")),
            workflowMetadata.getLinks().orElseThrow());
    }

    /**
     * Tests that 5.1.0 component metadata is parsed correctly.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReadingComponent() throws Exception {
        final var compMetadata = (ComponentMetadata)Workflowalizer.readTemplate(componentDir);
        assertEquals(Version510, compMetadata.getVersion().toString());
        assertEquals("ComponentWithMetadata", compMetadata.getName());
        testStructure(compMetadata, componentDir.resolve("workflow.knime"));
        assertEquals("LEARNER", compMetadata.getComponentType().orElseThrow());
        assertEquals("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAhElEQVR4nGNgoDIox4HzgJiVGAP+A3EHFgwS"
            + "X0+MIf/xiK8nxhB8BtQA8UeoISQbgO4dkg1gBGJ+AmpQJEWBOBdJvAWIjwAxBzEGcEAVfwbiBCi+DcT9QCxL"
            + "jAEgRZ1ArAHEL6BYHYsr8XoBBhygGJ8a4iXpZgCu/ADDeA3Alg+wYeoBAO4gNc6sqU+FAAAAAElFTkSuQmCC",
            compMetadata.getIcon().orElseThrow());
        assertEquals(List.of(Pair.create("train", "training data"), Pair.create("test", "test data")),
            extractNameAndDescription(compMetadata.getInPorts()));
        assertEquals(List.of(Pair.create("model", "trained model")),
            extractNameAndDescription(compMetadata.getOutPorts()));

        final var workflowMetadata = compMetadata.getWorkflowSetMeta().orElseThrow();
        assertEquals(ContentType.TEXT_HTML, workflowMetadata.getContentType());
        assertEquals("Carl Witt", workflowMetadata.getAuthor().orElseThrow());
        assertEquals(14, workflowMetadata.getCreated().orElseThrow().getHour());
        assertEquals(15, workflowMetadata.getLastModified().orElseThrow().getHour());
        assertEquals("This is a <b>test</b> component.", workflowMetadata.getDescription().orElseThrow());
        assertEquals(List.of("useful", "nice"), workflowMetadata.getTags().orElseThrow());
        assertEquals(
            List.of(new WorkflowSetMeta.Link("https://knime.com", "knime"),
                new WorkflowSetMeta.Link("https://www.knime.com", null)),
            workflowMetadata.getLinks().orElseThrow());
    }

    private static List<Pair<String, String>> extractNameAndDescription(final List<ComponentPortInfo> ports) {
        return ports.stream() //
                .map(port -> Pair.create(port.getName().orElse(null), port.getDescription().orElse(null))) //
                .collect(Collectors.toList());
    }

}
