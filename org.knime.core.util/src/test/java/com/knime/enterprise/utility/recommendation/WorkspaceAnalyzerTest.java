package com.knime.enterprise.utility.recommendation;
/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
 *   17.03.2016 (thor): created
 */


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.NodeInfo;
import org.knime.core.node.NodeTriple;
import org.knime.core.node.recommendation.WorkspaceAnalyzer;
import org.knime.core.util.PathUtils;

/**
 * Testcase for {@link WorkspaceAnalyzer}.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 */
public class WorkspaceAnalyzerTest {
    private Path m_workspaceDir;

    /**
     * Extracts the test workflow.
     *
     * @throws Exception if an error occurs
     */
    @Before
    public void setup() throws Exception {
        m_workspaceDir = PathUtils.createTempDir(WorkspaceAnalyzerTest.class.getName());
        try (InputStream is = WorkspaceAnalyzerTest.class.getResourceAsStream("/simple-workflow.zip")) {
            unzip(is, m_workspaceDir.toFile());
        }
    }

    /**
     * Cleans up after each test.
     *
     * @throws Exception if an error occurs
     */
    @After
    public void cleanup() throws Exception {
        PathUtils.deleteDirectoryIfExists(m_workspaceDir);
    }

    /**
     * Testcase with a simple workflow.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleWorkflow() throws Exception {
        WorkspaceAnalyzer analyzer = new WorkspaceAnalyzer(m_workspaceDir);
        analyzer.analyze();

        NodeTriple t1 = new NodeTriple(null,
            new NodeInfo("org.knime.base.node.io.tablecreator.TableCreator2NodeFactory", "Table Creator"),
            new NodeInfo("org.knime.base.node.preproc.filter.row.RowFilterNodeFactory", "Row Filter"));
        t1.incrementCount();
        t1.incrementCount();

        NodeTriple t2 = new NodeTriple(
            new NodeInfo("org.knime.base.node.io.tablecreator.TableCreator2NodeFactory", "Table Creator"),
            new NodeInfo("org.knime.base.node.preproc.filter.row.RowFilterNodeFactory", "Row Filter"),
            new NodeInfo("org.knime.base.node.preproc.sorter.SorterNodeFactory", "Sorter"));
        t2.incrementCount();

        NodeTriple t3 = new NodeTriple(
            new NodeInfo("org.knime.base.node.io.tablecreator.TableCreator2NodeFactory", "Table Creator"),
            new NodeInfo("org.knime.base.node.preproc.filter.row.RowFilterNodeFactory", "Row Filter"),
            new NodeInfo("MetaNode", "Metanode"));
        t3.incrementCount();

        List<NodeTriple> expectedTriplets = Arrays.asList(t1, t2, t3);

        List<NodeTriple> actualTriplets = new ArrayList<>(analyzer.getTriplets());

        for (int i = 0; i < expectedTriplets.size(); i++) {
            assertThat("Unexpected triplet " + i, actualTriplets.get(i), is(expectedTriplets.get(i)));
            assertThat("Unexpected count for triplet " + i, actualTriplets.get(i).getCount(),
                is(expectedTriplets.get(i).getCount()));
        }
    }

    /** Unzips the given zip input to the given folder.
     * @param in Non-null input stream.
     * @param folder Non-null output folder
     * @throws IOException ...
     * @throws ArchiveException ...
     */
    final static void unzip(final InputStream in, final File folder) throws IOException, ArchiveException {
        try (ArchiveInputStream ais =
                new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in)) {
            ZipArchiveEntry entry;
            while ((entry = (ZipArchiveEntry) ais.getNextEntry()) != null) {
                File outputFile = new File(folder, entry.getName());
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    try (OutputStream os = new FileOutputStream(outputFile)) {
                        IOUtils.copy(ais, os);
                    }
                }
            }
        }
    }
}
