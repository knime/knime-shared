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
 *   18 Feb 2022 (carlwitt): created
 */
package org.knime.core.util.workflow.def;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An object that either is associated with a {@link LoadException} ({@link #getSupplyException()} or references other
 * objects (children forming a tree) that have {@link LoadException}s.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 *
 * @param <K> the type used to identify referenced objects and their {@link LoadException}s. For instance, a Def is
 *            using {@link DefAttribute} to refer to its children, map uses String and list uses an integer offset to
 *            identify a child element and possibly its associated {@link LoadException}.
 */
public interface LoadExceptionTree<K> {

    /**
     * If this is present, it indicates that this instance is a default value that was used to fill in for another
     * instance that could not be loaded due to an exception.
     *
     * @return the exception associated to the creation of the original instance.
     */
    default Optional<LoadException> getSupplyException() {
        return Optional.empty();
    }

    /**
     * Induces a tree structure that covers all LoadExceptions by providing a supplier for every child with
     * LoadException or descendants with load exceptions. Note that this is a subset of the full data tree.
     *
     * The values of the map implement {@link LoadExceptionTree}, e.g., Def-objects that may have children that are
     * also {@link LoadExceptionTree}s or plain {@link LoadException}s that will not have children.
     *
     * The values of the map use a wildcard type in order to let implementations to decide whether to provide a Map<K,
     * LoadException> (no further children) or a Map<K, LoadExceptionSupplier> (may have children). Since we only read
     * from the map, we can give the implementation this freedom.
     *
     * @return the referenced objects (children) of this object that either have {@Link LoadException}s or descendants
     *         with load exceptions. Contains no null keys or values - if a key is present, it is mapped to a LES.
     */
    Map<K, ? extends LoadExceptionTree<?>> getExceptionalChildren();

    /**
     * @return false if no {@link LoadException}s occurred during the construction of this object and no load exceptions
     *         occurred during the construction of its descendants.
     */
    default boolean hasExceptions() {
        return getSupplyException().isPresent() || !getExceptionalChildren().isEmpty();
    }

    /**
     * @param childIdentifier which member (Def attribute, list element, map element) to check for load exceptions
     * @return true if there has been exception supplying the child or the child's descendants have exception.
     */
    default boolean hasExceptions(final K childIdentifier) {
        return getExceptionalChildren().containsKey(childIdentifier);
    }

    /**
     * @param childIdentifier member (Def attribute, list element, map element) to retrieve the supply exception for
     * @return the exception that caused this child element to be inserted as a child into this exception tree node.
     */
    default Optional<LoadException> getSupplyException(final K childIdentifier){
        return getExceptionTree(childIdentifier).flatMap(LoadExceptionTree::getSupplyException);
    }

    /**
     * @param childIdentifier member (Def attribute, list element, map element) to retrieve the load exceptions tree for
     * @return the {@link LoadExceptionTree} that provides access to the descendants load exceptions, or empty if
     *         there are no load exceptions associated to the child or its descendants.
     */
    default Optional<LoadExceptionTree<?>> getExceptionTree(final K childIdentifier) {
        return Optional.ofNullable(getExceptionalChildren().get(childIdentifier));
    }

    /**
     * @return the number of mappings in {@link #getExceptionalChildren()}
     */
    default int numberOfChildren() {
        return getExceptionalChildren().keySet().size();
    }

    /**
     * Aggregates across all attributes (keys of the map returned by {@link #getExceptionalChildren()}) and the list
     * associated to each attribute.
     *
     * @return a flattened list of all load exceptions.
     */
    default List<LoadException> getFlattenedLoadExceptions() {
        return getFlattenedLoadException(this, new java.util.ArrayList<>());
    }

    private static List<LoadException> getFlattenedLoadException(final LoadExceptionTree<?> les, final List<LoadException> accumulator){
        if(!les.hasExceptions()) {
            return accumulator;
        }
        les.getSupplyException().ifPresent(accumulator::add);
        for(var child: les.getExceptionalChildren().values()) {
            getFlattenedLoadException(child, accumulator);
        }
        return accumulator;
    }
}
