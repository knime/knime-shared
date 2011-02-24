/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright (c) KNIME.com, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 *
 * Created: Feb 23, 2011
 * Author: ohl
 */
package org.knime.core.util.node.quickform.in;

/**
 * Creates a date input control and delivers the input as string.
 *
 * @author ohl, University of Konstanz
 */
public class DateStringInputQuickFormInElement extends
        AbstractQuickFormInElement {

    private static final long serialVersionUID = -4981974737903893978L;

    private String m_value;

    /**
     * Create date input control with the given description.
     *
     * @param label label in front of the date field
     * @param description the tooltip
     */
    public DateStringInputQuickFormInElement(final String label,
            final String description) {
        super(label, description);
        m_value = null;
    }

    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Type.DateStringInput;
    }

    /** @param value the value to set */
    public void setValue(final String value) {
        m_value = value;
    }

    /** @return the date as string. If no value is set, null is returned. */
    public String getValue() {
        return m_value;
    }
}
