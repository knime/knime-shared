/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by 
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
 *  propagated with or for interoperation with KNIME. The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ------------------------------------------------------------------------
 *
 */
package org.knime.core.util.node.quickform.in;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A form element to select a value (e.g. column) and then a list of values
 * based on the previous one.
 *
 * @author Dominik Morent, KNIME.com, Zurich, Switzerland
 * @since 4.1
 */
public class ValueFilterInputQuickFormInElement extends
        AbstractQuickFormInElement {
    private static final long serialVersionUID = -6117453817741563224L;

    private Map<String, Set<String>> m_choiceValues;

    private String m_column;
    private String[] m_values;

    /**
     * Create an string option input with a given description.
     *
     * @param label The label, not null!
     * @param description The description, possibly null.
     * @param weight Weight factory,
     *        lighter value for more top-level alignment
     */
    public ValueFilterInputQuickFormInElement(final String label,
            final String description, final int weight) {
        super(label, description, weight);
        m_choiceValues = new LinkedHashMap<String, Set<String>>();
        m_column = null;
        m_values = new String[0];
    }

    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Type.ValueFilterInput;
    }

    /**
     * @param choiceValues all possible choices
     * @param selectedColumn the selected column
     * @param selectedValues the selected values
     * @since 4.3
     */
    public void setChoiceValues(final Map<String, Set<String>> choiceValues,
                                final String selectedColumn, final String[] selectedValues) {
        if (choiceValues == null) {
            m_choiceValues = new LinkedHashMap<String, Set<String>>();
        } else {
            m_choiceValues = new LinkedHashMap<String, Set<String>>(choiceValues);
        }

        if (m_choiceValues.keySet().contains(selectedColumn)) {
            m_column = selectedColumn;
        } else {
            m_column = m_choiceValues.keySet().isEmpty() ? null : m_choiceValues.keySet().iterator().next();
        }

        if (selectedValues != null
                && selectedValues.length > 0 && m_column != null
                && m_choiceValues.get(m_column).containsAll(Arrays.asList(selectedValues))) {
            m_values = selectedValues;
        } else {
            m_values = new String[0];
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
     * @param values the values to set
     * @since 4.3
     */
    public void setSelection(final String column, final String[] values) {
        if (m_choiceValues == null) {
            throw new IllegalStateException("No choice values set.");
        }
        if (column != null && !m_choiceValues.containsKey(column)) {
            throw new IllegalArgumentException(column);
        }
        m_column = column;

        if (values != null && m_column != null) {
            if (m_choiceValues.get(m_column).containsAll(Arrays.asList(values))) {
                m_values = values;
            } else {
                throw new IllegalArgumentException(Arrays.toString(values));
            }
        } else {
            m_values = new String[0];
        }
    }

    /**
     * @return the values
     */
    public String[] getValues() {
        return m_values;
    }

}
