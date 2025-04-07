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
 *   13 Mar 2025 (Manuel Hotz, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.util.hub;

import org.apache.commons.lang3.function.FailableCallable;
import org.apache.commons.lang3.function.FailableIntFunction;

/**
 * Reference to a KNIME Hub item version.
 *
 * @since 6.5
 * @author Manuel Hotz, KNIME GmbH, Konstanz, Germany
 */
/*
 * Implementation nodes wrt. "Item Versioning" and "Space Versioning":
 *
 * During the "Space Versioning" period, the string "-1" was used to represent the "current-state" and "latest" was
 * used to represent the "most-recent" version. These two legacy strings are not used anymore.
 *
 * This interface and its permitted records do not contain URI/query parameter methods by design.
 * See {@link ItemVersionURIUtil} for that purpose.
 */
public sealed interface ItemVersion permits CurrentState, MostRecent, SpecificVersion {

    /**
     * Checks if this version is a floating or specific version.
     *
     * @return {@code true} if this version is {@link MostRecent floating} or a {@link SpecificVersion specific
     *         version}, {@code false} otherwise
     */
    default boolean isVersioned() {
        return !(this instanceof CurrentState);
    }

    /**
     * Casts this version to the given subtype.
     *
     * @param <T> concrete type of result
     * @param type target type
     * @return this version as the given type
     */
    // The purpose of this method is to mark all casts to subtypes such that in the future with pattern-matching switch
    // we can easily replace instances of this call with a pattern-matching switch.
    default <T extends ItemVersion> T cast(final Class<T> type) {
        return type.cast(this);
    }

    /**
     * Matches the version to one of the three subtypes and calls/applies the corresponding callback or function.
     *
     * @param <T> result type of functions
     * @param <E> exception type of functions
     * @param version version to apply function to
     * @param currentState callback to call in case of {@link CurrentState}
     * @param mostRecent callback to call in case of {@link MostRecent}
     * @param specificVersionFn function to apply to {@link SpecificVersion}
     * @return result of the applied function
     * @throws E custom exception raised by functions
     */
    // once pattern-matching switch is available, invocations of this method can be replaced with the switch and
    // callsites potentially be improved
    default <T, E extends Throwable> T match(final FailableCallable<T, E> currentState,
        final FailableCallable<T, E> mostRecent, final FailableIntFunction<T, E> specificVersionFn) throws E {
        // good candidate for pattern-matching switch :(
        if (this instanceof CurrentState) {
            return currentState.call();
        } else if (this instanceof MostRecent) {
            return mostRecent.call();
        } else if (this instanceof SpecificVersion sv) {
            return specificVersionFn.apply(sv.version());
        } else {
            throw new IllegalStateException("Unexpected version subtype: " + this.getClass().getName());
        }
    }

    /**
     * Returns the instance for the "current state" pseudo-version.
     *
     * @return the current state
     */
    static CurrentState currentState() {
        return CurrentState.getInstance();
    }

    /**
     * Returns the instance for the "most recent" floating version.
     *
     * @return the "most recent" floating version
     */
    static MostRecent mostRecent() {
        return MostRecent.getInstance();
    }

    /**
     * Creates a specific version.
     *
     * @param version the non-negative version number
     * @return the specific version
     * @see SpecificVersion#SpecificVersion(int)
     */
    static SpecificVersion of(final int version) {
        return new SpecificVersion(version);
    }

}
