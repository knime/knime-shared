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
 *   Sep 26, 2022 (leonard.woerteler): created
 */
package org.knime.core.node.workflow.contextv2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;
import org.knime.core.util.User;

/**
 * Factory of fluent builders for all {@link ExecutorInfo} types.
 *
 * @param <B> type of the rest of the builder chain for a specific location type
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public abstract class ExecutorInfoBuilderFactory<B> {

    ExecutorInfoBuilderFactory() {
    }

    /**
     * Creates a new builder instance.
     *
     * @return new builder
     */
    public ExecutorInfoUIBuilder<B> newInstance() {
        return new ExecutorInfoUIBuilder<>(this::createBuilder);
    }

    /**
     * Builder accepting the repository address.
     *
     * @param <B> type of the rest of the builder chain
     */
    public static class ExecutorInfoUIBuilder<B> {

        final BiFunction<String, Path, B> m_continuation;

        ExecutorInfoUIBuilder(final BiFunction<String, Path, B> continuation) {
            m_continuation = continuation;
        }

        /**
         * Determines and sets the user id derived from the OS (see {@link User#getUsername()}).
         *
         * @return this builder instance
         */
        public final ExecutorInfoWPBuilder<B> withCurrentUserAsUserId() {
            String userid;
            try {
                userid = User.getUsername();
            } catch (Exception ex) {
                userid = System.getProperty("user.name");
            }
            return withUserId(CheckUtils.checkArgumentNotNull(userid, "User id must be set."));
        }

        /**
         * See {@link ExecutorInfo#getUserId()}.
         *
         * @param userId user ID
         * @return rest of the builder chain
         */

        public final ExecutorInfoWPBuilder<B> withUserId(final String userId) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(userId), "User ID must not be null or blank");
            return new ExecutorInfoWPBuilder<>(lp -> m_continuation.apply(userId, lp));
        }
    }

    /**
     * Builder accepting the repository address.
     *
     * @param <B> type of the rest of the builder chain
     */
    public static class ExecutorInfoWPBuilder<B> {

        final Function<Path, B> m_continuation;

        ExecutorInfoWPBuilder(final Function<Path, B> continuation) {
            m_continuation = continuation;
        }

        /**
         * See {@link ExecutorInfo#getLocalWorkflowPath()}.
         *
         * @param localWorkflowPath local workflow path
         * @return rest of the builder chain
         */
        public B withLocalWorkflowPath(final Path localWorkflowPath) {
            return m_continuation.apply(
                CheckUtils.checkArgumentNotNull(localWorkflowPath, "Local workflow path must not be null"));
        }
    }

    /**
     * Creates the remaining builder steps for the specific subclass of {@link ExecutorInfo} to build.
     */
    abstract B createBuilder(String userId, Path localWorkflowPath);

    /**
     * Base class for the finishing (optional argument) stage of {@link ExecutorInfo} builders.
     *
     * @param <I> The type of {@link ExecutorInfo} produced.
     * @param <B> The actual type of the builder.
     */
    @SuppressWarnings("unchecked")
    public abstract static class ExecutorInfoBuilder<I extends ExecutorInfo, B extends ExecutorInfoBuilder<I, ?>> {

        private static final Pattern NON_NAME_CHARS = Pattern.compile("[^a-zA-Z0-9 ]");

        // some random number between 10000 - 20000
        private static final AtomicInteger FOLDER_NAME_UNIQUIFIER =
                new AtomicInteger(10000 + (int)(Math.random() * 10000)); // NOSONAR one-off use of randomness

        /** See {@link ExecutorInfo#getType()}. */
        protected ExecutorType m_type;

        /** See {@link ExecutorInfo#getUserId()}. */
        protected String m_userId;

        /** See {@link ExecutorInfo#getLocalWorkflowPath()}. */
        protected Path m_localWorkflowPath;

        /** See {@link ExecutorInfo#getTempFolder()}. */
        protected Path m_tempFolder;

        ExecutorInfoBuilder( //
                final ExecutorType type, //
                final String userId, //
                final Path localWorkflowPath) {
            m_type = type;
            m_userId = userId;
            m_localWorkflowPath = localWorkflowPath;
        }

        /**
         * See {@link ExecutorInfo#getTempFolder()}.
         *
         * @param tempFolder temp folder
         * @return rest of the builder chain
         */
        public final B withTempFolder(final Path tempFolder) {
            m_tempFolder = tempFolder;
            return (B) this;
        }

        /**
         * Initializes the temp folder field if not previously set by the caller of the builder. When running on Hub or
         * Server the temp folder is set by the caller (there are designated "job folders" including a job temp folder)
         * and this method only returns the argument. When opening a workflow in AP the temp folder is not set by the
         * caller, hence it need to be "guessed". The folder will be put in "java.io.tmpdir", which is also used by
         * org.knime.core.node.KNIMEConstants.getKNIMETempPath(), i.e. the KNIME Preferences.
         *
         * <p>
         * This method does not create the temp folder but returns a Path to a temporary folder that does not exist
         * (yet). (The WFM will create the folder and only if it creates it itself it will delete it on close.)
         *
         * @param tempFolder Currently set temp folder or <code>null</code>.
         * @param wkfFolderName Suggested base name, non-null
         * @return Non-null temp file to use.
         */
        Path ensureTempFolder() {
            final var wkfFolderName = Objects.toString(m_localWorkflowPath.getFileName(), "");
            // derive from workflow directory (which usually reflects the name of the workflow)
            final var baseName = StringUtils.stripEnd(
                "knime_" + StringUtils.truncate(NON_NAME_CHARS.matcher(wkfFolderName).replaceAll("_"), 15), "_");

            if (m_tempFolder == null) {
                // this creates a new temp folder under System.getProperty("java.io.tmpdir")
                // which will always be the KNIME temp folder, see KNIMEConstants
                var tempFolderRootPath = Paths.get(System.getProperty("java.io.tmpdir"));
                Path temp = null;
                do {
                    final int suffix = FOLDER_NAME_UNIQUIFIER.getAndIncrement();
                    temp = tempFolderRootPath.resolve(baseName + "_" + suffix);
                } while (Files.exists(temp)); // should also work with d:\temp\knime_foo and d:\temp\knime_FOO
                m_tempFolder = temp;
            }
            return m_tempFolder;
        }

        /**
         * Builds a new {@link LocationInfo} instance according to the configuration of this builder.
         *
         * @return new instance
         */
        public abstract I build();
    }
}
