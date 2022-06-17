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
 *   28 Mar 2022 (carlwitt): created
 */
package org.knime.core.util.workflow.def;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Pair;

/**
 * {@link LoadExceptionTree} implementation that can be constructed from lists or maps of {@link LoadExceptionTree}s.
 * Adds all elements of the given collection that return true for {@link LoadExceptionTree#hasExceptions()} to
 * {@link LoadExceptionTree#getExceptionalChildren()}. Optionally, a {@link LoadException} related to the creation of
 * the list or map container can be provided to be returned by {@link LoadExceptionTree#getSupplyException()}.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @param <K> identical to the meaning of the type parameter of {@link LoadExceptionTree}.
 */
public final class SimpleLoadExceptionTree<K> implements LoadExceptionTree<K>, LoadExceptionTreeProvider {

    /**
     * A reusable instance to indicate no load exceptions are present.
     */
    public static final LoadExceptionTree<?> EMPTY = new SimpleLoadExceptionTree<>(null, Map.of());

    /** @see #getSupplyException() */
    private final Optional<LoadException> m_supplierException;

    /**
     * Contains an (offset, LES) mapping for each element in {@link #m_list} that implements {@link LoadExceptionTree},
     * where offset is the index of the element in {@link #m_list} and LES provides the load exception associated to
     * that element and its child elements.
     *
     * This may point to a Map<Integer, LoadException> or a Map<Integer, LoadExceptionSupplier<?>>.
     */
    private final Map<K, ? extends LoadExceptionTree<?>> m_childSuppliers;

    /**
     * @param supplierException nullable load exception related to the creation of the container.
     * @param childSuppliers map that contains only entries whose values have load exceptions.
     */
    private SimpleLoadExceptionTree(final LoadException supplierException,
        final Map<K, ? extends LoadExceptionTree<?>> childSuppliers) {
        m_supplierException = Optional.ofNullable(supplierException);
        m_childSuppliers = childSuppliers.isEmpty() ? Map.of() : childSuppliers;
    }

    @Override
    public Optional<LoadException> getSupplyException() {
        return m_supplierException;
    }

    @Override
    public Map<K, ? extends LoadExceptionTree<?>> getExceptionalChildren() {
        return m_childSuppliers;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Utility methods for creating from a list
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Checks the given objects for {@link LoadException}s. If an object is a {@link LoadExceptionTree}, it will be
     * added to the list returned by {@link #getExceptionalChildren()}.
     *
     * @param elements to check for load exceptions
     * @return instance containing as exceptional children all elements of the list that return true for
     *         {@link LoadExceptionTree#hasExceptions()}
     */
    public static LoadExceptionTree<Integer> list(final List<?> elements) {
        return list(elements, (LoadException)null);
    }

    /**
     * For lists that contain Def objects that can provide their {@link LoadException}s themselves, as opposed to other
     * types, like String.
     *
     * Checks the given objects for {@link LoadException}s. If an object is a {@link LoadExceptionTree}, it will be
     * added to the list returned by {@link #getExceptionalChildren()}.
     *
     * @param elements nullable list to check for load exceptions. If null, no children are added.
     * @param supplierException nullable exception related to the creation of the list, will be returned as
     *            {@link #getSupplyException()}.
     * @return instance containing as exceptional children all elements of the list that return true for
     *         {@link LoadExceptionTree#hasExceptions()}
     */
    public static LoadExceptionTree<Integer> list(final List<?> elements, final LoadException supplierException) {
        final var elementsNonNull = Objects.requireNonNullElse(elements, List.<LoadException> of());
        final var childSuppliers = IntStream.range(0, elementsNonNull.size())//
            // if the list element is a LoadExceptionSupplier, return it as a pair with its index
            .<Pair<Integer, LoadExceptionTree<?>>> mapToObj(i -> { // TODO use Stream.mapMulti
                var e = elementsNonNull.get(i);
                return LoadExceptionTreeProvider.hasExceptions(e) ? //
                Pair.of(i, LoadExceptionTreeProvider.getTree(e)) : null;
            }).filter(Objects::nonNull) // ignore the elements that have no load exceptions
            .collect(Collectors.toUnmodifiableMap(Pair::getLeft, Pair::getRight));
        return new SimpleLoadExceptionTree<>(supplierException, childSuppliers);
    }

    /**
     * Creates a supplemental {@link LoadExceptionTree} instance for lists that contain objects that do not implement
     * {@link LoadExceptionTree}. The exceptions have to be collected separately and can be merged along with the list's
     * supply exception into a single {@link LoadExceptionTree} using this method.
     *
     * @param exceptions map containing an (index, exception) mapping for each object with a {@link LoadException} where
     *            index is the element's offset in the given list
     * @return instance containing as exceptional children all given elements
     */
    public static LoadExceptionTree<Integer> list(final Map<Integer, LoadException> exceptions) {
        return new SimpleLoadExceptionTree<>((LoadException)null, exceptions);
    }

    /**
     * Creates a supplemental {@link LoadExceptionTree} instance for lists that contain objects that do not implement
     * {@link LoadExceptionTree}. The exceptions have to be collected separately and can be merged along with the list's
     * supply exception into a single {@link LoadExceptionTree} using this method.
     *
     * @param exceptions map containing an (index, exception) mapping for each object with a {@link LoadException} where
     *            index is the element's offset in the given list
     * @param supplierException nullable exception related to the creation of the list
     * @return instance containing as exceptional children all given elements and optionally the given supply exception.
     */
    public static LoadExceptionTree<Integer> list(final Map<Integer, LoadException> exceptions,
        final LoadException supplierException) {
        return new SimpleLoadExceptionTree<>(supplierException, exceptions);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Utility methods for creating from a map
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Filters the given map for values with load exceptions to be provided as
     * {@link LoadExceptionTree#getExceptionalChildren()}.
     *
     * @param <K> type of the keys in the map
     * @param map elements to check for load exceptions
     * @return instance containing as exceptional children all elements of the map that return true for
     *         {@link LoadExceptionTree#hasExceptions()}
     */
    public static <K> LoadExceptionTree<K> map(final Map<K, ?> map) {
        return map(map, (LoadException)null);
    }

    /**
     * Filters the given map for values with load exceptions to be provided as
     * {@link LoadExceptionTree#getExceptionalChildren()}.
     *
     * @param <K> type of the keys in the map
     * @param map elements to check for load exceptions
     * @param containerSupplyException nullable exception related to the creation of the map
     * @return instance containing as exceptional children all elements of the map that return true for
     *         {@link LoadExceptionTree#hasExceptions()}
     */
    public static <K, V> LoadExceptionTree<K> map(final Map<K, V> map, final LoadException containerSupplyException) {
        var nonNullMap = Objects.requireNonNullElse(map, Map.<K, V> of());
        final var exceptionalChildren = nonNullMap.entrySet().stream()//
            .filter(entry -> LoadExceptionTreeProvider.hasExceptions(entry.getValue()))//
            .collect(Collectors.toUnmodifiableMap(Entry::getKey, e -> LoadExceptionTreeProvider.getTree(e.getValue())));
        return new SimpleLoadExceptionTree<>(containerSupplyException, exceptionalChildren);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Utility method for creating from a def
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @param <K> type of the values that identify child elements
     * @param tree e.g., the exceptional children of a def instance
     * @param supplyException e.g., the exception that forced a def to be used as a default value
     * @return load exception tree with the given supplyException set as new supply exception
     */
    public static <K> LoadExceptionTree<K> tree(final LoadExceptionTree<K> tree, final LoadException supplyException) {
        return new SimpleLoadExceptionTree<>(supplyException, tree.getExceptionalChildren());
    }

    @Override
    public LoadExceptionTree<?> getLoadExceptionTree() {
        return this;
    }

}
