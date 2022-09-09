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

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;

/**
 * Provides information about a KNIME Hub executor.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class HubJobExecutorInfo extends JobExecutorInfo {

    /**
     * The scope is the account ID of the account that owns the execution context in which the current workflow job is
     * executed.
     */
    private final String m_scope;

    HubJobExecutorInfo(final String userId, final UUID jobId, final boolean isRemote, final String scope) {
        super(ExecutorType.HUB_EXECUTOR, userId, jobId, isRemote);
        m_scope = scope;
    }

    /**
     * @return the scope, which is the account ID of the account that owns the execution context in which the current
     *         workflow job is executed.
     */
    public String getScope() {
        return m_scope;
    }

    /**
     * Builder class for {@link HubJobExecutorInfo}
     */
    public static final class Builder extends JobExecutorInfo.Builder<Builder, HubJobExecutorInfo> {

        /**
         * See {@link HubJobExecutorInfo#getScope()}.
         */
        private String m_scope;

        /**
         * Constructor.
         */
        public Builder() {
            super(ExecutorType.HUB_EXECUTOR);
        }

        /**
         * Sets the scope, which is the account ID of the account that owns the execution context in which the current
         * workflow job is executed
         *
         * @param scope The account ID of the account that owns the execution context in which the current workflow job
         *            is executed.
         * @return this builder instance
         */
        public Builder withScope(final String scope) {
            m_scope = scope;
            return this;
        }

        @Override
        public HubJobExecutorInfo build() {
            checkFields();
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_scope), "Scope must not be empty");

            return new HubJobExecutorInfo(m_userId, //
                m_jobId, //
                m_isRemote, //
                m_scope);
        }
    }
}
