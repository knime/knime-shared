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
 *   Aug 30, 2022 (bjoern): created
 */
package org.knime.core.node.workflow.contextv2;

import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;

/**
 * Provides information about an Analytics Platform executor.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class AnalyticsPlatformExecutorInfo extends ExecutorInfo {

    private final boolean m_isBatchMode;

    AnalyticsPlatformExecutorInfo(final String userId, final boolean isBatchMode) {
        super(ExecutorType.ANALYTICS_PLATFORM, userId);
        m_isBatchMode = isBatchMode;
    }

    /**
     * @return true, if the Analytics Platform running the current workflow was started in (headless) batch mode.
     */
    public boolean isBatchMode() {
        return m_isBatchMode;
    }

    @Override
    public boolean isHeadless() {
        return isBatchMode();
    }

    /**
     * Builder class for {@link AnalyticsPlatformExecutorInfo}
     */
    public static final class Builder extends BaseBuilder<Builder, AnalyticsPlatformExecutorInfo> {

        private boolean m_isBatchMode;

        /**
         * Constructor.
         */
        public Builder() {
            super(ExecutorType.ANALYTICS_PLATFORM);
            m_isBatchMode = false;
        }

        /**
         * Sets the batch mode flag (defaults to false otherwise).
         *
         * @param isBatchMode Whether Analytics Platform has been started in batch mode or not.
         * @return this builder instance.
         */
        public Builder withBatchMode(final boolean isBatchMode) {
            m_isBatchMode = isBatchMode;
            return this;
        }

        @Override
        public AnalyticsPlatformExecutorInfo build() {
            checkFields();
            return new AnalyticsPlatformExecutorInfo(m_userId, //
                m_isBatchMode);
        }
    }
}
