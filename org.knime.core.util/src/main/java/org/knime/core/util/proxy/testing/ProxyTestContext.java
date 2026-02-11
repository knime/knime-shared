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
 *   Feb 11, 2026 (lw): created
 */
package org.knime.core.util.proxy.testing;

import java.io.IOException;

import org.apache.commons.lang3.function.FailableRunnable;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.search.GlobalProxySearch;
import org.knime.core.util.proxy.search.GlobalProxyStrategy.GlobalProxySearchResult;

/**
 * Interface implemented by the {@link ProxyParameterProvider}.
 * Used for supplying tests, test parameters will be declared as this interface.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.11
 */
public interface ProxyTestContext {

    /**
     * Clears the proxy from the test context, and then runs the test.
     *
     * @param test the test to run
     * @throws IOException if something goes wrong
     */
    void withEmpty(final FailableRunnable<IOException> test) throws IOException;

    /**
     * Applies the single, given {@link GlobalProxyConfig}, and then runs the test
     *
     * @param single single proxy
     * @param test the test to run
     * @throws IOException if something goes wrong
     */
    void withConfig(final GlobalProxyConfig single, final FailableRunnable<IOException> test) throws IOException;

    /**
     * Adds both {@link GlobalProxyConfig} instances to the test context,
     * and runs the test within, such that both are considered by the {@link GlobalProxySearch}.
     *
     * @param first first proxy
     * @param second second proxy
     * @param test the test to run
     * @throws IOException if something goes wrong
     */
    void withTwoResults(final GlobalProxySearchResult first, final GlobalProxySearchResult second,
        final FailableRunnable<IOException> test) throws IOException;

}
