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

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.WorkflowDef;

/**
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@SuppressWarnings("squid:S2698")
class ComponentLoaderTest {

    @Test
    void templateComponentLoaderTest() throws InvalidSettingsException, IOException {
        // given
        var file = NodeLoaderTestUtils.readResourceFolder("Component_Template");

        var m_configBaseRO = new SimpleConfig("mock");
        m_configBaseRO.addInt("id", 1);
        m_configBaseRO.addString("node_type", "SubNode");
        m_configBaseRO.addString("customDescription", "");


        // when
        var componentDef = ComponentNodeLoader.load(m_configBaseRO, file, LoadVersion.FUTURE);

        // then

        // Assert ComponentLoader
        // TODO should this really be null? Or an empty dialog settings def?
//        assertThat(componentDef.getDialogSettings()).isNull();
        assertThat(componentDef.getInPorts().get()).hasSize(1).extracting(p -> p.getIndex(), p -> p.getName()) //
            .containsExactlyInAnyOrder( //
                tuple(0, Optional.of("inport_0")));
        assertThat(componentDef.getOutPorts()).isEmpty();
        assertThat(componentDef.getVirtualInNodeId()).isEqualTo(3);
        assertThat(componentDef.getVirtualOutNodeId()).isEqualTo(4);
        assertThat(componentDef.getTemplateLink().get().getVersion().getMonthValue()).isEqualTo(1);
        assertThat(componentDef.getWorkflow().getNodes().get()).hasSize(3);

        // Assert SingleNodeLoader
        //TODO assert the ConfigMap value
        assertThat(componentDef.getInternalNodeSubSettings().get().getChildren()).containsKey("memory_policy");
        //        assertThat(nativeNodeDef.getModelSettings().getChildren());
        assertThat(componentDef.getVariableSettings().get().getChildren()).isEmpty();

        // Assert NodeLoader
        assertThat(componentDef.getId()).isEqualTo(1);
        assertThat(componentDef.getAnnotation().get().isAnnotationDefault()).isFalse();
        assertThat(componentDef.getCustomDescription()).isEmpty();
        assertThat(componentDef.getJobManager()).isNotNull();
        assertThat(componentDef.getLocks().get()) //
            .extracting("m_hasDeleteLock", "m_hasResetLock", "m_hasConfigureLock") //
            .containsExactly(true, false, false);
        assertThat(componentDef.getUiInfo().get())
            .extracting(n -> n.getBounds().getHeight(), n -> n.getBounds().getLocation(), n -> n.getBounds().getWidth())
            .containsNull();

        assertThat(componentDef.getLoadExceptionTree().hasExceptions()).isFalse();
    }

    @Test
    void multiPortComponentTest() throws InvalidSettingsException, IOException {
        // given
        var file = NodeLoaderTestUtils.readResourceFolder("Workflow_Test/Component (#14)");

        var workflowConfig = new SimpleConfig("mock");
        workflowConfig.addInt("id", 431);
        var uiSettings = new SimpleConfig("ui_settings");
        uiSettings.addIntArray("extrainfo.node.bounds", new int[]{2541, 1117, 122, 65});
        workflowConfig.addEntry(uiSettings);

        // when
        var componentDef = ComponentNodeLoader.load(workflowConfig, file, LoadVersion.FUTURE);

        // then

        // Assert MetaNodeLoader
        assertThat(componentDef.getInPorts().get())
            .extracting(p -> p.getIndex(), p -> p.getName(),
                p -> p.getPortType().getPortObjectClass().endsWith("BufferedDataTable"))
            .contains(tuple(0, Optional.of("inport_0"), true));
        assertThat(componentDef.getOutPorts().get())
            .extracting(p -> p.getIndex(), p -> p.getName(),
                p -> p.getPortType().getPortObjectClass().endsWith("BufferedDataTable"))
            .contains(tuple(0, Optional.of("outport_0"), true), tuple(1, Optional.of("outport_1"), true));
        assertThat(componentDef.getVirtualInNodeId()).isEqualTo(10);
        assertThat(componentDef.getVirtualOutNodeId()).isEqualTo(11);
        assertThat(componentDef.getTemplateLink().get().getVersion().getYear()).isEqualTo(2022);
        assertThat(componentDef.getTemplateLink().get().getUri()).isEqualTo("knime://knime.mountpoint/Component");
        assertThat(componentDef.getWorkflow()).isInstanceOf(WorkflowDef.class);

        // Assert SingleNodeLoader
        //TODO assert the ConfigMap value
        assertThat(componentDef.getInternalNodeSubSettings().get().getChildren()).containsKey("memory_policy");
        //        assertThat(nativeNodeDef.getModelSettings().getChildren());
        assertThat(componentDef.getVariableSettings().get()).isInstanceOf(ConfigMapDef.class);

        // Assert NodeLoader
        assertThat(componentDef.getId()).isEqualTo(431);
        assertThat(componentDef.getAnnotation().get()).isInstanceOf(NodeAnnotationDef.class);
        assertThat(componentDef.getCustomDescription()).isEmpty();
        assertThat(componentDef.getJobManager().get()).isInstanceOf(JobManagerDef.class);
        assertThat(componentDef.getLocks().get()) //
            .extracting("m_hasDeleteLock", "m_hasResetLock", "m_hasConfigureLock") //
            .containsExactly(true, false, false);
        assertThat(componentDef.getUiInfo().get()).extracting(n -> n.getBounds().getLocation().getX(),
            n -> n.getBounds().getLocation().getY(), n -> n.getBounds().getHeight(), n -> n.getBounds().getWidth())
            .containsExactly(2541, 1117, 122, 65);

        // TODO add back this assertion
//        assertThat(componentDef.getLoadExceptionTree().get().hasExceptions()).isFalse();
    }

}
