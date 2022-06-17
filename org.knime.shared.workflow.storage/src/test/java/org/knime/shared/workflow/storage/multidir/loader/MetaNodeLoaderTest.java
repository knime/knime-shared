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
 *   28 Feb 2022 (Dionysios Stolis): created
 */
package org.knime.shared.workflow.storage.multidir.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.core.util.LoadVersion;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.xml.sax.SAXException;

/**
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@SuppressWarnings("squid:S2698")
class MetaNodeLoaderTest {


    @Test
    void templateMetaNodeLoaderTest() throws InvalidSettingsException, IOException {
        // given
        var file = NodeLoaderTestUtils.readResourceFolder("Metanode_Template");

        // when
        MetaNodeDef metanodeDef = (MetaNodeDef)StandaloneLoader.load(file).getContents();

        // then

        // Assert MetaNodeLoader
        assertThat(metanodeDef.getInPorts()).isEmpty();
        assertThat(metanodeDef.getOutPorts()).isEmpty();
        assertThat(metanodeDef.getInPortsBarBounds()).isEmpty();
        assertThat(metanodeDef.getOutPortsBarBounds()).isEmpty();
        assertThat(metanodeDef.getTemplateMetadata().get().getVersion().getMonthValue()).isEqualTo(1);
        assertThat(metanodeDef.getWorkflow()).isNotNull();
        assertThat(metanodeDef.getWorkflow().getNodes().get()).hasSize(1);

        // Assert NodeLoader
        assertThat(metanodeDef.getId()).isEmpty(); // standalone metanode does not have an ID
        assertThat(metanodeDef.getAnnotation().get().getData()).isEmpty();
        assertThat(metanodeDef.getCustomDescription()).isEmpty();
        assertThat(metanodeDef.getJobManager()).isNotNull();
        assertThat(metanodeDef.getLocks().get()) //
            .extracting("m_hasDeleteLock", "m_hasResetLock", "m_hasConfigureLock") //
            .containsExactly(false, false, false);
        assertThat(metanodeDef.getBounds()).isEmpty();
        LoadExceptionTree<?> let =((LoadExceptionTreeProvider)metanodeDef).getLoadExceptionTree();
        assertThat(let.hasExceptions()).as(let.getFlattenedLoadExceptions().toString()).isFalse();
    }

    @Test
    void linkMetaNodeLoaderTest() throws IOException, InvalidSettingsException, SAXException, ParserConfigurationException {
        // given
        var file = NodeLoaderTestUtils.readResourceFolder("Workflow_Test/MetanodeTest (#12)");

        var parentWorkflowFile = new File(file.getParent(), IOConst.WORKFLOW_FILE_NAME.get());
        var workflowConfig = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(parentWorkflowFile)) {
            XMLConfig.load(workflowConfig, fis);
        }

        // when
        var metanodeDef = MetaNodeLoader.load(workflowConfig.getConfigBase("nodes").getConfigBase("node_12"), file,
            LoadVersion.FUTURE, false);

        // then

        // Assert MetaNodeLoader
        assertThat(metanodeDef.getInPorts().get())
            .extracting(p -> p.getIndex(), p -> p.getName(),
                p -> p.getPortType().getPortObjectClass().endsWith("BufferedDataTable"))
            .contains(tuple(0, Optional.of("Inport 0"), true));
        assertThat(metanodeDef.getOutPorts().get())
            .extracting(p -> p.getIndex(), p -> p.getName(),
                p -> p.getPortType().getPortObjectClass().endsWith("BufferedDataTable"))
            .contains(tuple(0, Optional.of("Connected to: Concatenated table"), true),
                tuple(1, Optional.of("Connected to: Concatenated table"), true));
        assertThat(metanodeDef.getInPortsBarBounds()).isEmpty();
        assertThat(metanodeDef.getOutPortsBarBounds()).isEmpty();
        assertThat(metanodeDef.getTemplateLink().get().getVersion().getYear()).isEqualTo(2022);
        assertThat(metanodeDef.getTemplateLink().get().getUri()).isEqualTo("knime://knime.mountpoint/MetanodeTest");
        assertThat(metanodeDef.getWorkflow().getNodes().get()).hasSize(4);

        // Assert NodeLoader
        assertThat(metanodeDef.getId()).contains(12);
        assertThat(metanodeDef.getAnnotation().get()).isInstanceOf(NodeAnnotationDef.class);
        assertThat(metanodeDef.getCustomDescription()).isEmpty();
        assertThat(metanodeDef.getJobManager()).isEmpty();
        assertThat(metanodeDef.getLocks().get()) //
            .extracting("m_hasDeleteLock", "m_hasResetLock", "m_hasConfigureLock") //
            .containsExactly(false, false, false);
        assertThat(metanodeDef.getBounds().get())
            .extracting(n -> n.getLocation().getX(), n -> n.getLocation().getY(), n -> n.getHeight(), n -> n.getWidth())
            .containsExactly(324, 317, 94, 64);
        assertThat(metanodeDef.getLoadExceptionTree().hasExceptions()).isFalse();
    }
}
