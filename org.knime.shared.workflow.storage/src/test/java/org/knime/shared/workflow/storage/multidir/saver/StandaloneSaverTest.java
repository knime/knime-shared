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
 *   9 May 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.shared.workflow.def.ConfigValueDef;
import org.knime.shared.workflow.def.CredentialPlaceholderDef;
import org.knime.shared.workflow.def.FlowVariableDef;
import org.knime.shared.workflow.def.RootWorkflowDef;
import org.knime.shared.workflow.def.StandaloneDef;
import org.knime.shared.workflow.def.StandaloneDef.ContentTypeEnum;
import org.knime.shared.workflow.storage.multidir.loader.NodeLoaderTestUtils;
import org.knime.shared.workflow.storage.multidir.loader.StandaloneLoader;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.xml.sax.SAXException;

/**
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings({"squid:S2698", "squid:S5961"})
class StandaloneSaverTest {

    static File OUTPUT_DIR;

    @BeforeAll
    static void setup() throws IOException {
        OUTPUT_DIR = NodeLoaderTestUtils.readResourceFolder(SaverTestUtils.OUTPUT_DIR_NAME);
        OUTPUT_DIR.mkdir();
    }

    /**
     * loads and saves a given standalone resource
     *
     * @param dirName
     * @throws IOException
     */
    StandaloneDef loadAndSave(final String dirName) throws IOException {
        // load
        var inputDir = NodeLoaderTestUtils.readResourceFolder(dirName);
        var standalone = StandaloneLoader.load(inputDir);

        if (standalone.getContentType() == null) {
            fail("couldn't load standalone " + dirName);
        }

        if(standalone.getLoadExceptionTree().hasExceptions()) {
            standalone.getLoadExceptionTree().getFlattenedLoadExceptions().forEach(Exception::printStackTrace);
            fail("Loaded workflow has problems");
        }

        // save
        var saver = new StandaloneSaver(standalone);
        saver.save(OUTPUT_DIR.getParentFile(), SaverTestUtils.OUTPUT_DIR_NAME);

        assertThat(OUTPUT_DIR).as("The output dir shouldn't be empty.").isNotEmptyDirectory();

        return StandaloneLoader.load(OUTPUT_DIR);
    }

    /**
     * Load a workflow standalone and save it again -- then check what has been written
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws Exception
     */
    @Test
    void testRootWorkflowStandalone() throws IOException, SAXException, ParserConfigurationException {
        var written = loadAndSave("Workflow_Test");

        assertThat(OUTPUT_DIR).isDirectoryNotContaining(f -> f.getName().equals("template.knime"));
        assertThat(written.getContentType()).as("A root workflow should have been written")
            .isEqualTo(StandaloneDef.ContentTypeEnum.ROOT_WORKFLOW);

        var rootWF = (RootWorkflowDef)written.getContents();
        assertThat(rootWF.getCredentialPlaceholders().get()).as("Check the contents of the credentials").hasSize(2)
            .extracting(CredentialPlaceholderDef::getName).containsExactly("credential test", "credential test 2");
        var vars = rootWF.getFlowVariables().get();
        assertThat(vars).as("Check that the number of variables is 2").hasSize(2);
        assertThat(vars).extracting(FlowVariableDef::getName).containsExactly("dummy variable", "another var");
        assertThat(vars).extracting(FlowVariableDef::getPropertyClass).containsExactly("INTEGER", "STRING");
        assertThat(vars).extracting(FlowVariableDef::getValue).allMatch(c -> c instanceof ConfigValueDef);
        assertThat(rootWF.getTableBackendSettings()).as("No table backend should be set").isEmpty();
    }

    /**
     * Load a workflow standalone and save it again -- then check what has been written
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws Exception
     */
    @Test
    void testWorkflowStandalone() throws IOException, SAXException, ParserConfigurationException {
        var mock = mock(StandaloneDef.class);
        when(mock.getContentType()).thenReturn(ContentTypeEnum.WORKFLOW);

        var saver = new StandaloneSaver(mock);
        var file = NodeLoaderTestUtils.readResourceFolder("dummy");
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> saver.save(file, "dummy"));
    }

    /**
     * Load a component standalone and save it again -- then check what has been written
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws InvalidSettingsException
     * @throws Exception
     */
    @Test
    void testComponentStandalone() throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        var written = loadAndSave("Component_Template");

        assertThat(OUTPUT_DIR).isDirectoryContaining(f -> f.getName().equals("template.knime"));
        assertThat(written.getContentType()).as("A component should have been written")
            .isEqualTo(StandaloneDef.ContentTypeEnum.COMPONENT);

        var templateFile = new File(OUTPUT_DIR, IOConst.TEMPLATE_FILE_NAME.get());
        var templateConfig = new SimpleConfig(IOConst.TEMPLATE_FILE_NAME.get());
        try (var fis = new FileInputStream(templateFile)) {
            XMLConfig.load(templateConfig, fis);
        }

        assertThat(templateConfig.getString(IOConst.WORKFLOW_HEADER_CREATED_BY_KEY.get())).isEqualTo("4.6.0.qualifier");
        assertThat(templateConfig.getBoolean(IOConst.WORKFLOW_HEADER_CREATED_BY_NIGHTLY_KEY.get())).isTrue();
        assertThat(templateConfig.getString(IOConst.WORKFLOW_HEADER_VERSION_KEY.get())).isEqualTo("4.1.0");

        var templateInfo = templateConfig.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        assertThat(templateInfo.getString(IOConst.WORKFLOW_TEMPLATE_ROLE_KEY.get())).isEqualTo("Template");
        assertThat(templateInfo.getString(IOConst.TIMESTAMP.get())).startsWith("2022");
        assertThat(templateInfo.getString(IOConst.SOURCE_URI_KEY.get())).isNull();
        assertThat(templateInfo.getString(IOConst.WORKFLOW_TEMPLATE_TYPE_KEY.get())).isEqualTo("SubNode");
    }

    /**
     * Load a metanode standalone and save it again -- then check what has been written
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws InvalidSettingsException
     * @throws Exception
     */
    @Test
    void testMetaNodeStandalone() throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        var written = loadAndSave("Metanode_Template");

        assertThat(OUTPUT_DIR).isDirectoryContaining(f -> f.getName().equals("template.knime"));
        assertThat(written.getContentType()).as("A metanode should have been written")
            .isEqualTo(StandaloneDef.ContentTypeEnum.METANODE);

        var templateFile = new File(OUTPUT_DIR, IOConst.TEMPLATE_FILE_NAME.get());
        var templateConfig = new SimpleConfig(IOConst.TEMPLATE_FILE_NAME.get());
        try (var fis = new FileInputStream(templateFile)) {
            XMLConfig.load(templateConfig, fis);
        }

        assertThat(templateConfig.getString(IOConst.WORKFLOW_HEADER_CREATED_BY_KEY.get())).isEqualTo("4.6.0.qualifier");
        assertThat(templateConfig.getBoolean(IOConst.WORKFLOW_HEADER_CREATED_BY_NIGHTLY_KEY.get())).isTrue();
        assertThat(templateConfig.getString(IOConst.WORKFLOW_HEADER_VERSION_KEY.get())).isEqualTo("4.1.0");

        var templateInfo = templateConfig.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        assertThat(templateInfo.getString(IOConst.WORKFLOW_TEMPLATE_ROLE_KEY.get())).isEqualTo("Template");
        assertThat(templateInfo.getString(IOConst.TIMESTAMP.get())).startsWith("2022");
        assertThat(templateInfo.getString(IOConst.SOURCE_URI_KEY.get())).isNull();
        assertThat(templateInfo.getString(IOConst.WORKFLOW_TEMPLATE_TYPE_KEY.get())).isEqualTo("MetaNode");
    }

    @AfterEach
    void cleanup() throws IOException {
        FileUtils.cleanDirectory(OUTPUT_DIR);
    }

    @AfterAll
    static void deleteOutputDir() throws IOException {
        FileUtils.deleteDirectory(OUTPUT_DIR);
    }
}
