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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;

/**
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@SuppressWarnings("squid:S2698")
class MetaNodeLoaderTest {

    private ConfigBaseRO m_configBaseRO;

    @BeforeEach
    void setUp() {
        m_configBaseRO = mock(ConfigBaseRO.class);
    }

    @Test
    void templateMetaNodeLoaderTest() throws InvalidSettingsException, IOException {
        // given
        var file = NodeLoaderTestUtils.readResourceFolder("Metanode_Template");

        when(m_configBaseRO.getInt("id")).thenReturn(1);
        when(m_configBaseRO.containsKey("customDescription")).thenReturn(true);

        // when
        var metanodeDef = MetaNodeLoader.load(m_configBaseRO, file, LoadVersion.FUTURE);

        // then

        // Assert MetaNodeLoader
        assertThat(metanodeDef.getInPorts()).isEmpty();
        assertThat(metanodeDef.getOutPorts()).isEmpty();
        assertThat(metanodeDef.getInPortsBarBounds()).isEmpty();
        assertThat(metanodeDef.getOutPortsBarBounds()).isEmpty();
        assertThat(metanodeDef.getTemplateLink().get().getVersion().getMonthValue()).isEqualTo(1);
        assertThat(metanodeDef.getTemplateLink().get().getUri()).isNull();
        assertThat(metanodeDef.getWorkflow()).isNotNull();
        assertThat(metanodeDef.getWorkflow().getNodes().get()).hasSize(1);

        // Assert NodeLoader
        assertThat(metanodeDef.getId()).isEqualTo(1);
        assertThat(metanodeDef.getAnnotation().get().getData()).isEmpty();
        assertThat(metanodeDef.getCustomDescription()).isEmpty();
        assertThat(metanodeDef.getJobManager()).isNotNull();
        assertThat(metanodeDef.getLocks().get()) //
            .extracting("m_hasDeleteLock", "m_hasResetLock", "m_hasConfigureLock") //
            .containsExactly(false, false, false);
        assertThat(metanodeDef.getBounds()).isEmpty();
        metanodeDef.getLoadExceptionTree().getFlattenedLoadExceptions().forEach(Exception::printStackTrace);
        assertThat(metanodeDef.getLoadExceptionTree().hasExceptions()).as(metanodeDef.getLoadExceptionTree().getFlattenedLoadExceptions().toString()).isFalse();
    }

    @Test
    void linkMetaNodeLoaderTest() throws IOException, InvalidSettingsException {
        // given
        var file = NodeLoaderTestUtils.readResourceFolder("Workflow_Test/MetanodeTest (#12)");

        when(m_configBaseRO.getInt("id")).thenReturn(431);
        when(m_configBaseRO.containsKey("customDescription")).thenReturn(false);
        when(m_configBaseRO.containsKey("annotations")).thenReturn(false);
        when(m_configBaseRO.containsKey("ui_settings")).thenReturn(true);
        var uiSettings = new SimpleConfig("ui_settings");
        uiSettings.addIntArray("extrainfo.node.bounds", new int[]{2541, 1117, 122, 65});
        when(m_configBaseRO.getConfigBase("ui_settings")).thenReturn(uiSettings);

        // when
        var metanodeDef = MetaNodeLoader.load(m_configBaseRO, file, LoadVersion.FUTURE);

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
        assertThat(metanodeDef.getId()).isEqualTo(431);
        assertThat(metanodeDef.getAnnotation().get()).isInstanceOf(NodeAnnotationDef.class);
        assertThat(metanodeDef.getCustomDescription()).isEmpty();
        assertThat(metanodeDef.getJobManager().get()).isInstanceOf(JobManagerDef.class);
        assertThat(metanodeDef.getLocks().get()) //
            .extracting("m_hasDeleteLock", "m_hasResetLock", "m_hasConfigureLock") //
            .containsExactly(false, false, false);
        assertThat(metanodeDef.getBounds().get())
            .extracting(n -> n.getLocation().getX(), n -> n.getLocation().getY(), n -> n.getHeight(), n -> n.getWidth())
            .containsExactly(2541, 1117, 122, 65);
        assertThat(metanodeDef.getLoadExceptionTree().hasExceptions()).isFalse();
    }
}
