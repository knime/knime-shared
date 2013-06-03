/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 * Created on 03.06.2013 by thor
 */
package org.knime.core.node.workflow;

import java.io.File;

/**
 * This class holds information about the context in which a workflows currently resides. It includes information such
 * as the current workflow directory or the ID of the user executing the workflow.
 *
 * <b>This class is not intended to be used by clients.</B>
 *
 * @author Thorsten Meinl, KNIME.com Zurich, Switzerland
 * @since 4.4
 */
public class WorkflowContext {
    private final String m_userid;

    private final File m_currentLocation;

    private final File m_originalLocation;

    private final File m_tempLocation;

    /**
     * Creates a new workflow context;
     *
     * @param userId the user's id
     * @param currentLocation the current location of the workflow, can be copy of the original workflow
     * @param originalLocation the original location of the workflow e.g. in the server repository
     * @param tempLocation the temporary directory for the workflow
     */
    public WorkflowContext(final String userId, final File currentLocation, final File originalLocation,
        final File tempLocation) {
        m_userid = userId;
        m_currentLocation = currentLocation;
        m_originalLocation = originalLocation;
        m_tempLocation = tempLocation;
    }

    /**
     * Returns the ID of the user which executes this workflow.
     *
     * @return a user id
     */
    public String getUserid() {
        return m_userid;
    }

    /**
     * Returns the current location of the workflow, which can be a temporary directory.
     *
     * @return a local directory
     */
    public File getCurrentLocation() {
        return m_currentLocation;
    }

    /**
     * Returns the original location of the workflow, e.g. in the server repository. This has only meaning if the
     * current directory is a copy.
     *
     * @return a local directory
     */
    public File getOriginalLocation() {
        return m_originalLocation;
    }

    /**
     * Returns the location of the temporary directory for this workflow.
     *
     * @return a temporary directory
     */
    public File getTempLocation() {
        return m_tempLocation;
    }
}
