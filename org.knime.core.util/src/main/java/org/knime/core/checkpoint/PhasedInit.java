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
 *   Feb 18, 2026 (wiswedel): created
 */
package org.knime.core.checkpoint;

/**
 * Lifecycle interface for components that can defer initialization until after a CRaC checkpoint restore.
 *
 * <p>
 * Defers component (e.g. network resource) initialization until after a CRaC checkpoint restore, if enabled.
 * On unconfigured JVMs or non-CRaC environments,
 * {@link PhasedInitSupport#registerOrActivate(Object, PhasedInit)} immediately triggers the activation,
 * ensuring compatibility without manual intervention.
 * </p>
 *
 * <p>
 * <b>Example:</b>
 * </p>
 *
 * <pre>
 * public class MyEarlyStartup implements IEarlyStartup, PhasedInit&lt;RuntimeException> {
 *     &#64;Override
 *     public void run() {
 *         prepareContext();
 *         PhasedInitSupport.registerOrActivate(this, this);  // Defers or runs immediately
 *     }
 *
 *     &#64;Override
 *     public void activate() {
 *         // opening sockets, connecting to servers, loading resources, etc.
 *     }
 * }
 * </pre>
 *
 * @author Bernd Wiswedel, Manuel Hotz, KNIME GmbH, Konstanz, Germany
 * @param <E> the type of exception possibly thrown by the activation
 * @since 6.11
 * @noreference This interface is not intended to be referenced by clients (pending API)
 */
public interface PhasedInit<E extends Exception> {

    /**
     * Functional interface for the activation step, used with {@link PhasedInit#ofActivation(Activation)}.
     *
     * @param <E> the type of exception possibly thrown
     */
    @FunctionalInterface
    interface Activation<E extends Exception> {
        /** @throws E on activation failure */
        void activate() throws E;
    }

    /**
     * Creates a {@link PhasedInit} that only implements {@link #activate()}, with a no-op
     * {@link #beforeCheckpoint()}. Intended for use with a lambda or method reference.
     *
     * @param <E> the type of exception possibly thrown by the activation
     * @param callback the activation logic
     * @return a {@link PhasedInit} delegating {@link #activate()} to {@code callback}
     */
    static <E extends Exception> PhasedInit<E> ofActivation(final Activation<E> callback) {
        return new PhasedInit<>() {
            @Override
            public void activate() throws E {
                callback.activate();
            }
        };
    }

    /**
     * Called just before a CRaC checkpoint snapshot is taken, allowing the component to quiesce (e.g. flush buffers,
     * release resources that cannot be serialized). May not be called at all if the JVM is not checkpointed.
     *
     * @throws E if preparation fails
     */
    default void beforeCheckpoint() throws E {
        // default implementation does nothing
    }

    /**
     * Lightweight activation called after checkpoint restore, or immediately on unconfigured JVMs.
     * <p>
     * Assuming this <code>PhasedInit</code> is
     * {@linkplain PhasedInitSupport#registerOrActivate(Object, PhasedInit) registered}, this method will be
     * invoked exactly once: either directly at registration time, or after a checkpoint restore.
     *
     * </p>
     * <p>
     * Should only perform fast operations, like connecting to a service, not heavy initialization.
     * </p>
     * @throws E if activation fails, will be handled (wrapped) by CRaC or forwarded to caller
     */
    default void activate() throws E {
        // default implementation does nothing
    }

}
