/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 * History
 *   Jan 10, 2017 (wiswedel): created
 */
package org.knime.core.node.config.base.json;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.knime.core.node.config.base.AbstractConfigEntry;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.util.CheckUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Jackson serializable representation of a byte.
 * @author Bernd Wiswedel, KNIME.com, KNIME.com, Zurich
 */
@JsonRootName("root")
public final class JSONRoot {

    private final String m_rootName;
    private final Map<String, AbstractJSONEntry> m_map;

    /**
     * @param rootName The root name, not null.
     * @param map The map, not null.
     */
    public JSONRoot(@JsonProperty("name") final String rootName,
        @JsonProperty("value") final Map<String, AbstractJSONEntry> map) {
        m_rootName = CheckUtils.checkArgumentNotNull(rootName);
        m_map = CheckUtils.checkArgumentNotNull(map);
    }

    /** @return the root name*/
    @JsonProperty("name")
    public String getRootName() {
        return m_rootName;
    }

    /** @return the root name*/
    @JsonProperty("value")
    public Map<String, AbstractJSONEntry> getMap() {
        return m_map;
    }

    /** Adds the elements in this tree recursively to the argument {@link ConfigBase}. The key
     * of this root element is ignored and the one in the argument is retained.
     * @param base To add to
     * @param addToConfigCallBack A callback providing access to the package-scope put method.
     */
    public void addToConfigBase(final ConfigBase base,
        final BiConsumer<ConfigBase, AbstractConfigEntry> addToConfigCallBack) {
        m_map.entrySet().forEach(e -> e.getValue().addToConfigBase(
            e.getKey(), base, addToConfigCallBack));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("%s: %s", m_rootName, m_map);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(m_rootName)
                .append(m_map).toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JSONRoot)) {
            return false;
        }
        String otherName = ((JSONRoot)obj).m_rootName;
        Map<String, AbstractJSONEntry> otherMap = ((JSONRoot)obj).m_map;
        return super.equals(obj) && Objects.equals(otherName, m_rootName)
                && Objects.equals(otherMap, m_map);
    }
}
