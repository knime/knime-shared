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
 *   17.09.2018 (thor): created
 */
package org.knime.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a replacement for {@link Runtime#addShutdownHook(Thread)} which allows ordering of hooks. If a hook
 * must be executed as the first hook, it can be added using {@link #addFirstShutdownHook(Runnable)} which puts it to
 * the front of the list. {@link #addShutdownHook(Runnable)} will add it to the back of the list. In contrast to the
 * implementation in {@link Runtime}, the hooks are executed sequentially one after the other.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 5.10
 */
public class ShutdownHelper {
    private final List<Runnable> m_shutdownHooks = new ArrayList<>();

    private static final ShutdownHelper INSTANCE = new ShutdownHelper();

    private class ShutdownHook extends Thread {
        public ShutdownHook() {
            super("KNIME shutdown hooks");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            for (Runnable r : m_shutdownHooks) {
                setName("KNIME shutdown hooks - " + r.getClass().getName());
                try {
                    r.run();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                        "Error while running shutdown hook " + r.getClass() + ": " + ex.getMessage(), ex);
                }
            }
        }
    }

    private ShutdownHelper() {
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    /**
     * Returns the singleton instance.
     *
     * @return the singleton instance
     */
    public static ShutdownHelper getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a new hook as the first hook to be run during shutdown. The previous first hook will be run in second place.
     * Ideally this method should only be used once in the first code base.
     *
     * @param r the hook
     */
    public void addFirstShutdownHook(final Runnable r) {
        m_shutdownHooks.add(0, r);
    }

    /**
     * Adds a new hook to be run during shutdown. It will be run after all already registered hooks.
     *
     * @param r the hook
     */
    public void addShutdownHook(final Runnable r) {
        m_shutdownHooks.add(r);
    }
}
