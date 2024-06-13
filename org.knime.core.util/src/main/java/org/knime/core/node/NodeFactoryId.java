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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 *   Oct 25, 2023 (hornm): created
 */
package org.knime.core.node;

import java.util.function.Supplier;

import org.knime.core.node.util.CheckUtils;

/**
 * Utility methods related to the node-factory-id.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @since 6.2
 */
public final class NodeFactoryId {

    private static final String FACTORY_ID_UNIQUIFIER_SEPARATOR = "#";

    private NodeFactoryId() {
        // utility
    }

    /**
     * @param factoryClassName the class name ({@link Class#getName()}) of the node factory
     * @param isParameterizedNodeFactory whether the factory-id is composed for a ParameterizedNodeFactory
     * @param factoryIdUniquifier only relevant if {@code isParameterizedNodeFactory)} is {@code true} - included in the
     *            factory id to make it globally unique; if not provided in case of a parameterized node factory (i.e.
     *            if {@code null}) the node name will be used instead @param nodeName required as a fallback if no
     *            factory-id-uniquifier is provided; usually only the case for dynamic/parameterized nodes created with
     *            AP < 5.2 @return the factory id
     * @param nodeName supplier of the node's name used in case no factory-id-uniquifier is provided and
     *            {@code isParameterizedNodeFactory} is {@code true}
     * @return the factory-id
     */
    @SuppressWarnings("java:S2301")
    public static String compose(final String factoryClassName, final boolean isParameterizedNodeFactory,
        final String factoryIdUniquifier, final Supplier<String> nodeName) {
        CheckUtils.checkNotNull(factoryClassName);
        var id = factoryClassName;
        if (isParameterizedNodeFactory) {
            return id + FACTORY_ID_UNIQUIFIER_SEPARATOR
                + (factoryIdUniquifier == null ? nodeName.get() : factoryIdUniquifier);
        } else {
            return id;
        }
    }

}
