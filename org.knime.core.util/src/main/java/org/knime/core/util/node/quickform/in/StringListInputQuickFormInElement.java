/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
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
 * ------------------------------------------------------------------------
 *
 */
package org.knime.core.util.node.quickform.in;


/**
 * A form element to enter a string or list of strings (which is a selection
 * of possible choices).
 * @author Dominik Morent, KNIME.com, Zurich, Switzerland
 * @since 4.1
 */
public class StringListInputQuickFormInElement extends
        TwinStringListInputQuickFormInElement {
    
    private static final long serialVersionUID = 5768026515480530298L;

    private boolean m_multiple = true;

    /**
     * Create an string list input with a given description.
     *
     * @param label The label, not null!
     * @param description The description, possibly null.
     * @param weight Weight factory,
     *        lighter value for more top-level alignment
     */
    public StringListInputQuickFormInElement(final String label,
            final String description, final int weight) {
        super(label, description, weight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return Type.StringListInput;
    }
    
    /**
     * @return true, if multiple selection possible
     */
    public boolean isMultiple() {
        return m_multiple;
    }
    
    /**
     * Set true if multiple selection possible; otherwise false.
     * @param multiple true, if multiple selection possible
     */
    public void setMultiple(final boolean multiple) {
        m_multiple = multiple;
    }

}
