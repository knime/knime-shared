/*
 * ------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------
 *
 */
package org.knime.core.util.node.quickform.in;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A form element to select a value (e.g. column) and then another value based
 * on the previous one.
 *
 * @author Dominik Morent, KNIME AG, Zurich, Switzerland
 * @since 4.1
 */
public class ValueSelectionInputQuickFormInElement extends
        AbstractQuickFormInElement {
    private static final long serialVersionUID = -6117453817741563224L;

    private Map<String, Set<String>> m_choiceValues;

    private String m_column;
    private String m_value;
    private boolean m_lockColumn;

    /**
     * Create an string option input with a given description.
     *
     * @param label The label, not null!
     * @param description The description, possibly null.
     * @param weight Weight factory,
     *        lighter value for more top-level alignment
     */
    public ValueSelectionInputQuickFormInElement(final String label,
            final String description, final int weight) {
        super(label, description, weight);
        m_choiceValues = new LinkedHashMap<String, Set<String>>();
        m_column = null;
        m_value = null;
        m_lockColumn = false;
    }

    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Type.ValueSelectionInput;
    }

    /**
     * @param values the value to set (does not ensure that the set value is one
     *            of the choices)
     */
    public void setChoiceValues(final Map<String, Set<String>> values) {
        if (values == null) {
            m_choiceValues = new LinkedHashMap<String, Set<String>>();
        } else {
            m_choiceValues = new LinkedHashMap<String, Set<String>>(values);
        }
    }

    /** @return the value (not necessarily a string from the choices) */
    public Map<String, Set<String>> getChoiceValues() {
        return m_choiceValues;
    }

    /**
     * @return the column
     */
    public String getColumn() {
        return m_column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(final String column) {
        m_column = column;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return m_value;
    }

    /**
     * @param value the values to set
     */
    public void setValue(final String value) {
        m_value = value;
    }

    /**
     * @return the lockColumn
     */
    public boolean getLockColumn() {
        return m_lockColumn;
    }

    /**
     * @param lockColumn the lockColumn to set
     */
    public void setLockColumn(final boolean lockColumn) {
        m_lockColumn = lockColumn;
    }
}
