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
 *  propagated with or for interoperation with KNIME. The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ------------------------------------------------------------------------
 *
 */
package org.knime.core.util.node.quickform.in;

import org.knime.core.util.node.quickform.AbstractQuickFormElement;

/**
 * Super class of all input elements.
 *
 * @author Bernd Wiswedel, KNIME.com, Zurich, Switzerland
 */
public abstract class AbstractQuickFormInElement
    extends AbstractQuickFormElement {

    private static final long serialVersionUID = -6790238955178501177L;


    /** Constructor with a given label and description.
     * @param label A label shown in the controller.
     * @param description A description shown in the controller,
     *        possibly null.
     * @param weight Weight factory,
     *        lighter value for more top-level alignment */
    protected AbstractQuickFormInElement(
            final String label, final String description, final int weight) {
        super(label, description, weight);
    }

    /** @return associated type. */
    public abstract Type getType();

    /** Enum of all known types. */
    public enum Type {
        /** File upload element. */
        FileUpload,
        /** String input element. */
        StringInput,
        /** One string out of a list of string input elements (radio buttons).
         */
        StringSelectionInput,
        /** One or multiple strings in two lists of string input elements.
         * @since 4.1
         */
        TwinStringListInput, // column filter (multiple): twin list
        /** One options of a list of string input elements.
         * @since 4.1
         */
        StringOptionInput, // single: dropdown
        /** Selection of a single value based on another single value.
         * @since 4.1
         */
        ValueSelectionInput,
        /** Selection of one or multiple values based on a single value.
         * @since 4.1
         */
        ValueFilterInput,
        /** One or multiple strings of a list of string input elements.
         * @since 4.1
         */
        StringListInput, // value list (multiple): list
        /** A list of strings in a textarea input element.
         * @since 4.1
         */
        StringListPasteboxInput,
        /** Integer input element. */
        IntInput,
        /** Double input element. */
        DoubleInput,
        /** Date-string input element. */
        DateStringInput,
        /** Molecule sketcher input element.
         * @since 4.1 */
        SketcherInput,
        /** Checkbox input element.
         * @since 4.1 */
        CheckboxInput,
        /** A dummy input (no UI, only a breakpoint marker on a wizard page).
         * @since 4.2 */
        DummyInput,
        /** Single selection input element.
         * @since 4.4
         */
        SingleSelectionInput,
        /** Multiple selection input element.
         * @since 4.4
         */
        MultipleSelectionInput;
    }

}
