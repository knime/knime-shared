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
 *   Oct 31, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.knime.core.node.InvalidSettingsException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for reading workflow/templates and writing them to JSON
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public class JSONTest {

    private static ObjectMapper workflowMapper;
    private static ObjectMapper templateMapper;
    private static int count;
    private static String dest;

    /**
     * @param args
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws URISyntaxException
     * @throws ParseException
     * @throws InvalidSettingsException
     * @throws IOException
     * @throws XPathExpressionException
     * @throws FileNotFoundException
     */
    public static void main(final String[] args) throws FileNotFoundException, XPathExpressionException, IOException,
        InvalidSettingsException, ParseException, URISyntaxException, ParserConfigurationException, SAXException {
        workflowMapper = WorkflowMetadata.getConfiguredObjectMapper();
        templateMapper = TemplateMetadata.getConfiguredObjectMapper();

        final Path p = Paths.get(args[0]); // TODO: Change to path to directory containing workflows
        dest = args[1]; // TODO: Change to desired write location
        Files.createDirectories(Paths.get(dest));
        Files.walkFileTree(p, new Walker());
        System.out.println("count: " + count);
    }

    private static final class Walker extends WorkflowFileVisitor {

        /**
         * {@inheritDoc}
         */
        @Override
        FileVisitResult visitWorkflowGroup(final Path workflowSetMeta) {
            return FileVisitResult.CONTINUE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        void visitTemplate(final Path templateDir) {
            try {
                final TemplateMetadata tm = Workflowalizer.readTemplate(templateDir);
                final String json = templateMapper.writeValueAsString(tm.flatten());
                try (final PrintWriter writer = new PrintWriter(new File(dest + count + ".json"))) {
                    writer.write(json);
                    count++;
                }
            } catch (IOException | URISyntaxException | InvalidSettingsException | ParseException
                    | XPathExpressionException | ParserConfigurationException | SAXException e) {
                System.out.println("Error reading template: " + templateDir);
                System.out.println(e);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Unsupported workflow version:")) {
                    System.out.println(e.getMessage());
                } else {
                    throw e;
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        void visitWorkflow(final Path workflowDir) {
            try {
                System.out.println("Processing " + workflowDir);
                final WorkflowMetadata tm = Workflowalizer.readWorkflow(workflowDir);
                final String json = workflowMapper.writeValueAsString(tm.flatten());
                try (final PrintWriter writer = new PrintWriter(new File(dest + count + ".json"))) {
                    writer.write(json);
                    count++;
                }
            } catch (IOException | URISyntaxException | InvalidSettingsException | ParseException
                    | XPathExpressionException | ParserConfigurationException | SAXException e) {
                System.out.println("Error reading workflow: " + workflowDir);
                System.out.println(e);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Unsupported workflow version:")) {
                    System.out.println(e.getMessage());
                } else {
                    throw e;
                }
            }
        }
    }

}
