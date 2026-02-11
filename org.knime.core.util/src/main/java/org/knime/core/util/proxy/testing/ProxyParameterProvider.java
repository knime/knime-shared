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
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.function.FailableRunnable;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.search.GlobalProxyStrategy.GlobalProxySearchResult;

/**
 * All-in-one solution for supplying an entire with a proxy "environment",
 * i.e. a test context. Example usage is as follows:
 * <pre>
 * class MyTest {
 *     \@RegisterExtension
 *     ProxyParameterProvider provider = MyTestContext.INSTANCE;
 *
 *     \@TestTemplate
 *     void testABC(final ProxyTestContext context) {
 *         // use as an instance of `ProxyTestContext` here
 *     }
 *
 *     \@ParameterizedTest
 *     \@EnumSource(ProxyProtocol.class)
 *     void testDEF(final ProxyProtocol protocol) {
 *         // no `TestTemplate` annotation for custom parameters
 *     }
 *
 *     \@Test
 *     void testGHI() {
 *         // no `TestTemplate` annotation for no parameters
 *     }
 * }
 * </pre>
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.11
 */
public class ProxyParameterProvider
    implements TestTemplateInvocationContextProvider, ProxyTestContext {

    // -- TestTemplateInvocationContextProvider --

    @Override
    public boolean supportsTestTemplate(final ExtensionContext context) {
        return context.getRequiredTestMethod().isAnnotationPresent(TestTemplate.class);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(final ExtensionContext context) {
        return Stream.of(new TestTemplateInvocationContext() {
            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(new ParameterResolver() {

                    @Override
                    public boolean supportsParameter(final ParameterContext parameter, final ExtensionContext context) {
                        return parameter.getParameter().getType().isAssignableFrom(ProxyTestContext.class);
                    }

                    @Override
                    public ProxyTestContext resolveParameter(final ParameterContext parameter,
                        final ExtensionContext context) {
                        return ProxyParameterProvider.this; // as test context
                    }
                }); // as parameter resolver
            }
        });
    }

    // -- ProxyTestContext --

    @Override
    public void withEmpty(final FailableRunnable<IOException> test) throws IOException {
        // to be overridden again
    }

    @Override
    public void withConfig(final GlobalProxyConfig single,
        final FailableRunnable<IOException> test) throws IOException {
        // to be overridden again
    }

    @Override
    public void withTwoResults(final GlobalProxySearchResult first, final GlobalProxySearchResult second,
        final FailableRunnable<IOException> test) throws IOException {
        // to be overridden again
    }

}
