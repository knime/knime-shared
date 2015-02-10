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
package org.knime.core.util.node.quickform;

import java.io.Serializable;

import org.knime.core.node.InvalidSettingsException;

/**
 * Super class of all form elements.
 *
 * @author Bernd Wiswedel, KNIME.com, Zurich, Switzerland
 */
public abstract class AbstractQuickFormElement implements Serializable {

    private static final long serialVersionUID = 9185133246163139446L;

    private final String m_label;
    private final String m_description;
    private final int m_weight;

    /** Constructor with a given label and description.
     * @param label A label shown in the controller.
     * @param description A description shown in the controller,
     *        possibly null.
     * @param weight A weight value to define order of elements in form,
     *        lighter elements come first. */
    protected AbstractQuickFormElement(
            final String label, final String description,
            final int weight) {
        m_weight = weight;
        if (label == null) {
            throw new NullPointerException("Argument must not be null.");
        }
        m_label = label;
        m_description = description;
    }

    /** @return the label, never null. */
    public String getLabel() {
        return m_label;
    }

    /** @return the description, possibly null.  */
    public String getDescription() {
        return m_description;
    }

    /** @return the weight */
    public int getWeight() {
        return m_weight;
    }

    /** Casts the argument to the expected class, throws exception if not
     * an instance.
     * @param <T> The expected type.
     * @param cl The class to the type.
     * @param el The element to cast.
     * @return <code>el</code> casted to the argument class.
     * @throws InvalidSettingsException If not of correct class or null.
     */
    public static <T extends AbstractQuickFormElement> T cast(
            final Class<T> cl, final AbstractQuickFormElement el)
        throws InvalidSettingsException {
        if (cl.isInstance(el)) {
            return cl.cast(el);
        } else {
            throw new InvalidSettingsException("Expected quick form element "
                    + "of type " + cl.getSimpleName() + "; got "
                    + ((el == null) ? "<null>" : el.getClass().getSimpleName()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_description == null) ? 0 : m_description.hashCode());
        result = prime * result + ((m_label == null) ? 0 : m_label.hashCode());
        result = prime * result + m_weight;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractQuickFormElement other = (AbstractQuickFormElement)obj;
        if (m_description == null) {
            if (other.m_description != null) {
                return false;
            }
        } else if (!m_description.equals(other.m_description)) {
            return false;
        }
        if (m_label == null) {
            if (other.m_label != null) {
                return false;
            }
        } else if (!m_label.equals(other.m_label)) {
            return false;
        }
        if (m_weight != other.m_weight) {
            return false;
        }
        return true;
    }
}
