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
 *   Aug 10, 2024 (lw): created
 */
package org.knime.core.util.proxy.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.function.FailableRunnable;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.search.GlobalProxyStrategy.GlobalProxySearchResult;

/**
 * Strategy context factory that allows performing {@link GlobalProxySearch} calls with
 * predefined (i.e. configured) results. Note that a context-based proxy strategy like
 * {@code withEnvironment} is not possible since {@link System#getenv()} is immutable.
 */
public class GlobalProxyTestContext {

    private static void runWithStrategy(final FailableRunnable<IOException> test, final GlobalProxyStrategy... strategies)
        throws IOException {
        final GlobalProxySearch original = GlobalProxySearch.getDefault();
        try {
            GlobalProxySearch.setDefault(new GlobalProxySearch(strategies));
            test.run();
        } finally {
            GlobalProxySearch.setDefault(original);
        }
    }

    /**
     * Searches with a strategy that returns an EMPTY signal.
     *
     * @throws IOException
     */
    public static void withEmpty(final FailableRunnable<IOException> test) throws IOException {
        runWithStrategy(test, (u, p) -> GlobalProxySearchResult.empty());
    }

    /**
     * Searches with a strategy that returns the specified {@link GlobalProxyConfig}.
     *
     * @throws IOException
     */
    public static void withConfig(final GlobalProxyConfig config, final FailableRunnable<IOException> test)
        throws IOException {
        runWithStrategy(test, (u, p) -> GlobalProxySearchResult.found(config));
    }

    /**
     * Searches with two strategies sequentially where the return behavior is defined by
     * the {@link GlobalProxySearchResult}s themselves (STOP, EMPTY, FOUND).
     *
     * @throws IOException
     */
    public static void withTwoResults(final GlobalProxySearchResult a, final GlobalProxySearchResult b,
        final FailableRunnable<IOException> test) throws IOException {
        runWithStrategy(test, (u, p) -> a, (u, p) -> b);
    }

    /**
     * Searches with a strategy modifies Java system properties, then invokes the Java-based
     * proxy strategy to search proxies therein.
     *
     * @throws IOException
     */
    public static void withFoundInJava(final Map<String, String> properties, final FailableRunnable<IOException> test)
        throws IOException {
        final Map<String, String> copy = new HashMap<>();
        try {
            properties.entrySet().forEach(e -> copy.put(e.getKey(), System.setProperty(e.getKey(), e.getValue())));
            runWithStrategy(test, new JavaProxyStrategy());
        } finally {
            for (var e : copy.entrySet()) {
                final var value = e.getValue();
                if (value != null) {
                    System.setProperty(e.getKey(), value);
                } else {
                    System.clearProperty(e.getKey());
                }
            }
        }
    }
}
