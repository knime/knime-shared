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
 *   Aug 26, 2022 (bjoern): created
 */
package org.knime.core.node.workflow.contextv2;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;

/**
 * Provides information about the process that runs the current workflow.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class ExecutorInfo {

    private final ExecutorType m_type;

    private final String m_userId;

    ExecutorInfo(final ExecutorType type, //
        final String userId) {
        m_type = type;
        m_userId = userId;
    }

    /**
     * @return the type of executor running the current workflow.
     */
    public ExecutorType getType() {
        return m_type;
    }

    /**
     * @return the user ID of the user running the current workflow.
     */
    public String getUserId() {
        return m_userId;
    }

    /**
     * @return true, if the executor that runs the current workflow does not have a SWT/Swing UI.
     */
    public boolean isHeadless() {
        return true;
    }

    /**
     * Base class for {@link ExecutorInfo} builders.
     *
     * @param <B> The actual type of the builder.
     * @param <I> The type of {@link ExecutorInfo} produced.
     */
    @SuppressWarnings("rawtypes")
    abstract static class BaseBuilder<B extends BaseBuilder, I extends ExecutorInfo> {

        /**
         * See {@link ExecutorInfo#getType()}.
         */
        protected ExecutorType m_type;

        /**
         * See {@link ExecutorInfo#getUserId()}.
         */
        protected String  m_userId;

        protected BaseBuilder(final ExecutorType type) {
            m_type = type;
        }

        @SuppressWarnings("unchecked")
        public final B withUserId(final String userId) {
            m_userId = userId;
            return (B)this;
        }

        protected void checkFields() {
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_userId), "User ID must not be null or blank");
        }

        public abstract I build();
    }
}
