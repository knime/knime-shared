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
 *   Oct 12, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Abstract {@link FileVisitor} for walking a file system and reading encountered workflows.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public abstract class WorkflowFileVisitor implements FileVisitor<Path> {

    private final static String WORKFLOW = "workflow.knime";
    private final static String WORKFLOW_GROUP = "workflowset.meta";
    private final static String TEMPLATE = "template.knime";


    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        try (final Stream<Path> siblings = Files.list(dir);
                final Stream<Path> metaSiblings = Files.list(dir);
                final Stream<Path> tempSiblings = Files.list(dir)) {

            final Optional<Path> workflowSibling = siblings.filter(p -> testType(p, WORKFLOW)).findFirst();
            final Optional<Path> workflowSetMetaSibling =
                metaSiblings.filter(p -> testType(p, WORKFLOW_GROUP)).findFirst();
            final boolean templateSibling = tempSiblings.filter(p -> testType(p, TEMPLATE)).findFirst().isPresent();

            // Directory is part of a template, skip this subtree. It will be parsed by the Workflowalizer
            if (templateSibling && workflowSibling.isPresent()) {
                visitTemplate(dir);
                return FileVisitResult.SKIP_SUBTREE;
            }
            // Directory is part of a workflow group
            if (workflowSetMetaSibling.isPresent() && !workflowSibling.isPresent()) {
                return visitWorkflowGroup(workflowSetMetaSibling.get());
            }
            // Directory is a node in the workflow, skip this subtree. It will be parsed by the Workflowalizer
            if (workflowSibling.isPresent()) {
                visitWorkflow(dir);
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        Objects.requireNonNull(file);
        if (exc != null) {
            throw exc;
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        Objects.requireNonNull(dir);
        if (exc != null) {
            throw exc;
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * Visit the workflow group, and determine if workflows within the group should be read.
     *
     * @param workflowSetMeta the path to the workflowset.meta file
     * @return a {@link FileVisitResult} which will determine if workflows in the group are read or skipped
     */
    abstract FileVisitResult visitWorkflowGroup(final Path workflowSetMeta);

    /**
     * Reads the template workflow
     *
     * @param templateDir the template directory
     */
    abstract void visitTemplate(final Path templateDir);

    /**
     * Read the given workflow
     *
     * @param workflowDir the workflow directory
     */
    abstract void visitWorkflow(final Path workflowDir);

    // -- Helper methods --

    private static boolean testType(final Path p, final String type) {
        // zip files usually have a "/" directory whose name is null
        if (p.getFileName() == null) {
            return false;
        }
        return p.getFileName().toString().equals(type);
    }

}
