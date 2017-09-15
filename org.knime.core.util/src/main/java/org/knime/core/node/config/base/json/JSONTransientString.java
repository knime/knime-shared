/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 * History
 *   Jan 10, 2017 (wiswedel): created
 */
package org.knime.core.node.config.base.json;

import java.util.Objects;
import java.util.function.BiConsumer;

import org.knime.core.node.config.base.AbstractConfigEntry;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigTransientStringEntry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Jackson serializable representation of a transient String (value not saved).
 * @author Bernd Wiswedel, KNIME.com, KNIME.com, Zurich
 */
@JsonTypeName("transient-string")
public final class JSONTransientString extends AbstractJSONEntry {

    private final String m_transientString;

    /** Desiralization constructor for Jackson. */
    @JsonCreator
    public JSONTransientString() {
        this(ConfigTransientStringEntry.HIDDEN_VALUE);
    }

    /** User constructor to initialize with the real string (which is not saved). |
     * @param s The value
     */
    @JsonIgnore
    public JSONTransientString(final String s) {
        m_transientString = s;
    }

    /** @return the 'real' string as passed in the constructor. */
    @JsonIgnore
    public String getString() {
        return m_transientString;
    }

    /** {@inheritDoc} */
    @Override
    void addToConfigBase(final String key, final ConfigBase config,
        final BiConsumer<ConfigBase, AbstractConfigEntry> addToConfigCallBack) {
        addToConfigCallBack.accept(config, new ConfigTransientStringEntry(key, m_transientString));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return m_transientString;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return super.hashCode() ^ Objects.hash(m_transientString);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JSONTransientString)) {
            return false;
        }
        return super.equals(obj) &&
                Objects.equals(((JSONTransientString)obj).m_transientString, m_transientString);
    }
}
