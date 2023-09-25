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
 *   25 Sept 2023 (carlwitt): created
 */
package org.knime.shared.workflow.storage.multidir.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.util.LoadVersion;
import org.knime.core.util.PathUtils;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.storage.multidir.loader.WorkflowLoader;
import org.knime.shared.workflow.storage.neo4j.saver.RelationshipWriter;
import org.knime.shared.workflow.storage.text.util.SingleFileWriter;

/**
 * Walk a directory hierarchy and try to load all workflows.
 *
 * @see RelationshipWriter
 * @see SingleFileWriter
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public class Loader implements FileVisitor<Path> {

    /** knwfs are unzipped into a directory with this name next to them */
    private static final String UNZIP_DIR_NAME = "__unzipped__for__loading";

    private static final Logger LOGGER = Logger.getLogger(Loader.class.getSimpleName());

    private static record LoadEntry(long loadTime, Status status) {
        private enum Status {
                SUCCESS, SKIPPED_DUPLICATE, FAILED
        }
    }

    /** The workflow representations and where they were loaded from for deduplication. */
    private final Map<WorkflowDef, Path> m_loadedWorkflows = new HashMap<>();

    /** After successfully loading a workflow, store an entry here */
    private final Map<Path, LoadEntry> m_processedPaths = new HashMap<>();

    /** Skipped due to exceptions during loading */
    private final Set<Path> m_skippedDirectories = new HashSet<>();

    private final BiConsumer<String, WorkflowDef> m_processor;

    private final long m_startedLoading;

    /** Whether to track and ignore duplicate workflows. */
    private final boolean m_skipDuplicates;

    /** Number of workflows skipped loading because their representation was already in m_workflows */
    private int m_skippedDuplicates;

    /**
     * @param processor takes a workflow id (hub repository item id or otherwise unique identifier) and a workflow and
     *            does something with it (e.g., write to single json file)
     * @param skipDuplicates if true, keep the workflow representations in memory and skip loading workflows with a
     *            representation identical to a workflow loaded earlier
     */
    public Loader(final BiConsumer<String, WorkflowDef> processor, final boolean skipDuplicates) {
        LOGGER.info(() -> "Skipping duplicate workflows: %s".formatted(skipDuplicates));
        m_startedLoading = System.currentTimeMillis();
        m_processor = processor;
        m_skipDuplicates = skipDuplicates;
    }

    /**
     * Process all workflows in the given directory.
     *
     * @param directory tries to interpret this as a workflow otherwise treats it as a workflow group.
     * @throws IOException
     */
    public void processDirectory(final Path directory) throws IOException {
        LOGGER.info(() -> "Loading workflows from %s".formatted(directory));
        Files.walkFileTree(directory, this);
    }

    /**
     * @param directory
     * @param workflowConfig
     */
    private void processWorkflowDirectory(final Path directory, final Optional<ConfigBaseRO> workflowConfig) {
        if (m_processedPaths.size() % 100 == 0) {
            LOGGER.info(() -> "Processed %s workflows".formatted(m_processedPaths.size()));
        }
        final var now = System.currentTimeMillis();
        try {
            final var result = WorkflowLoader.load(directory.toFile(), workflowConfig.get(), LoadVersion.V5100);

            final var isDuplicate = m_skipDuplicates && m_loadedWorkflows.containsKey(result);

            if (isDuplicate) {
                LOGGER.info(() -> "Skipping duplicate workflow%n\t%s already loaded from%n\t%s".formatted(directory,
                    m_loadedWorkflows.get(result)));
                m_skippedDuplicates++;
                m_processedPaths.put(directory,
                    new LoadEntry(System.currentTimeMillis() - now, LoadEntry.Status.SKIPPED_DUPLICATE));
                return;
            }

            if (m_skipDuplicates) {
                // keep loaded workflows in memory
                m_loadedWorkflows.put(result, directory);
            }

            m_processedPaths.put(directory, new LoadEntry(System.currentTimeMillis() - now, LoadEntry.Status.SUCCESS));
            m_processor.accept(directory.toString(), result);
        } catch (Exception ex) {
            m_processedPaths.put(directory, new LoadEntry(System.currentTimeMillis() - now, LoadEntry.Status.FAILED));
            exception(ex, directory);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        // if this has a workflow.knime file, it's a workflow -> load it
        final var workflowConfig = getWorkflowConfig(dir);
        if (workflowConfig.isPresent()) {
            processWorkflowDirectory(dir, workflowConfig);
            // files in this dir have been processed
            return FileVisitResult.SKIP_SUBTREE;
        }

        // if this has a knwfs, unzip them before visiting the dir
        try (var stream = Files.list(dir)) {
            final var paths = stream.toList();
            for (Path path : paths) {
                if (!Files.isDirectory(path) && path.getFileName().toString().endsWith(".knwf")) {
                    var tmpDir = dir.resolve(UNZIP_DIR_NAME);
                    Files.createDirectories(tmpDir);
                    unzipArchive(path, tmpDir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // assume this is a workflow group
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        // ignore files
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        exc.printStackTrace();
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        var tempDir = dir.resolve(UNZIP_DIR_NAME);
        if (Files.exists(tempDir)) {
            org.apache.commons.io.file.PathUtils.deleteDirectory(tempDir);
        }
        return FileVisitResult.CONTINUE;
    }

    private void exception(final Exception ex, final Path directory) {
        LOGGER.log(Level.SEVERE, ex, () -> "Failed to load workflow from %s".formatted(directory));
        m_skippedDirectories.add(directory);
    }

    /**
     * Prints details of the loading process, e.g., the number of workflows loaded or skipped due to exceptions during
     * the load process.
     */
    public void logStats() {
        // load statistics log level info
        LOGGER.info(() -> "Loaded %s workflows".formatted(m_processedPaths.size()));
        final var skippedDirectories = m_skippedDirectories.size();
        if (skippedDirectories > 0) {
            LOGGER.warning(() -> "Skipped %s directories due to load exceptions".formatted(skippedDirectories));
            m_skippedDirectories.forEach(skipPath -> LOGGER.info(() -> "Skipped: " + skipPath));
        }

        if (m_skipDuplicates) {
            LOGGER.info(() -> "Skipped %s duplicate workflows".formatted(m_skippedDuplicates));
        }

        // timing info
        LOGGER.info(() -> "Loading took %s seconds".formatted((System.currentTimeMillis() - m_startedLoading) / 1000));
        // longest load time was
        final var max = m_processedPaths.entrySet().stream()
            .max((e1, e2) -> Long.compare(e1.getValue().loadTime, e2.getValue().loadTime));
        max.ifPresent(m -> LOGGER
            .info(() -> "Longest load time was %s seconds for %s".formatted(m.getValue().loadTime / 1000, m.getKey())));

        // write a summary file with paths and load times in csv format
        final var summaryFile = Path.of("summary.csv");
        if (!Files.exists(summaryFile)) {
            LOGGER.info(() -> "Writing summary file to %s".formatted(summaryFile.toAbsolutePath()));
            try (var writer = Files.newBufferedWriter(summaryFile, StandardOpenOption.CREATE_NEW)) {
                writer.write("result,path,loadTimeMs\n");
                m_processedPaths.forEach((path, entry) -> {
                    try {
                        writer.write("%s,\"%s\",%s%n".formatted(entry.status.toString(), path, entry.loadTime));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void unzipArchive(final Path zipFile, final Path parentDir) {
        try {
            final var zis = new ZipInputStream(Files.newInputStream(zipFile, StandardOpenOption.READ));
            PathUtils.unzip(zis, parentDir);
        } catch (IOException ex) {
            exception(ex, zipFile);
        }
    }

    /** Attempt to interpret a directory as workflow directory. */
    private static Optional<ConfigBaseRO> getWorkflowConfig(final Path directory) {
        try {
            return Optional.of(LoaderUtils.parseWorkflowConfig(directory.toFile()));
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

}
