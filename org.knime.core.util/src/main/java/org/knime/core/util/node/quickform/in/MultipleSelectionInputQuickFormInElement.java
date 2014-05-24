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

import java.io.IOException;


/**
 * A form element to select multiple String items (which is a selection of possible choices).
 *
 * @author Thomas Gabriel, KNIME.com, Zurich, Switzerland
 * @since 4.4
 */
public class MultipleSelectionInputQuickFormInElement extends AbstractQuickFormInElement {

    private static final long serialVersionUID = -5784850452527525503L;

    private String m_values;

    private String m_choices;

    private Layout m_layout;

    /** Possible Layout types. */
    public enum Layout {
        /** CheckBox'es arranged vertically. */
        CHECKBOX_VERTICAL {
            /** {@inheritDoc} */
            @Override
            public String toString() {
                return "Check Box (vertical)";
            }
        },
        /** CheckBox'es arranged horizontally. */
        CHECKBOX_HORIZONTAL {
            /** {@inheritDoc} */
            @Override
            public String toString() {
                return "Check Box (horizontal)";
            }
        },
//        /** Single selection list. */
//        LIST {
//            /** {@inheritDoc} */
//            @Override
//            public String toString() {
//                return "List Selection";
//            }
//        },
//        /** Twin selection list. */
//        TWINLIST {
//            /** {@inheritDoc} */
//            @Override
//            public String toString() {
//                return "Twin List Selection";
//            }
//        };
    }

    /**
     * Create an integer input with a given description.
     *
     * @param label The label, not null!
     * @param description The description, possibly null.
     * @param weight Weight factory,
     *        lighter value for more top-level alignment
     */
    public MultipleSelectionInputQuickFormInElement(final String label,
            final String description, final int weight) {
        super(label, description, weight);
        m_choices = "";
        m_layout = Layout.CHECKBOX_VERTICAL;
    }

    /** @return layout for a multiple selection element */
    public Layout getLayout() {
        return m_layout;
    }

    /** Set a new layout for a multiple selection.
     * @param layout the new layout
     */
    public void setLayout(final Layout layout) {
        if (layout == null) {
            throw new IllegalArgumentException("layout must not be null");
        }
        m_layout = layout;
    }

    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Type.MultipleSelectionInput;
    }

    /**
     * @param values the values to set (does not ensure that the set value is one
     *            of the choices)
     */
    public void setValues(final String values) {
        m_values = values;
    }

    /** @return the value (not necessarily a string from the choices) */
    public String getValues() {
        return m_values;
    }

    /**
     * @param choices new set of possible values to show if the user is to enter
     *            a new value.
     */
    public void setChoices(final String choices) {
        if (choices == null) {
            m_choices = "";
        } else {
            m_choices = choices;
        }
    }

    /**
     * @return a clone of the currently set possible values (might be empty,
     *         never null)
     */
    public String getChoices() {
        return m_choices;
    }

    private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
        out.writeBoolean(m_values != null); // is null?
        if (m_values != null) {
            out.writeUTF(m_values);
        }
        out.writeBoolean(m_choices != null); // is null?
        if (m_choices != null) {
            out.writeUTF(m_choices);
        }
        // cannot serialize enums: https://forums.oracle.com/thread/1152088?tstart=306
        // Caused by: java.io.InvalidObjectException: can't deserialize enum
        //      at java.lang.Enum.readObject(Enum.java:205)
        out.writeUTF(m_layout.name());
    }

    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (in.readBoolean()) {
            m_values = in.readUTF();
        }
        if (in.readBoolean()) {
            m_choices = in.readUTF();
        }
        String layout = in.readUTF();
        try {
            m_layout = Layout.valueOf(layout);
        } catch (IllegalArgumentException e) {
            throw new IOException("Cannot map enum constant: " + layout);
        }
    }
}
