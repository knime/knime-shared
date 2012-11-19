/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2011
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
package org.knime.core.util.node.quickform.out;


/**
 * Text (read only) output element.
 *
 * @author Bernd Wiswedel, KNIME.com, Zurich, Switzerland
 * @since 4.2
 */
public class TextAreaOutputQuickFormOutElement extends AbstractQuickFormOutElement {

    private static final long serialVersionUID = -5845017118981663694L;

    /** Format as shown in the web page. */
    public enum TextFormat {
        /** Ordinary text. */
        Text,
        /** Preformatted text (respects line breaks). */
        Preformatted,
        /** Text w/ html tags. */
        Html;
    }

    private final String m_text;

    private final TextFormat m_textFormat;

    /** Creates output element with the given label and description.
     * @param label The label as shown in the GUI/Web, not null.
     * @param description The description, maybe null.
     * @param weight Weight factory,
     *        lighter value for more top-level alignment
     * @param text The text.
     * @param format the text format
     */
    public TextAreaOutputQuickFormOutElement(final String label,
            final String description, final int weight, final String text, final TextFormat format) {
        super(label, description, weight);
        m_text = text;
        m_textFormat = format;
    }

    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Type.TextAreaOutput;
    }

    /** @return the text. */
    public String getText() {
        return m_text;
    }

    /**
     * @return the format
     */
    public TextFormat getTextFormat() {
        return m_textFormat;
    }

}
