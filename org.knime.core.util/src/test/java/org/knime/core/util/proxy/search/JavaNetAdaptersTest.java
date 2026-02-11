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
 *   Aug 13, 2024 (lw): created
 */
package org.knime.core.util.proxy.search;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.knime.core.util.auth.DelegatingAuthenticator;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.ProxyProtocol;
import org.knime.core.util.proxy.ProxySelectorAdapter;
import org.knime.core.util.proxy.search.GlobalProxyStrategy.GlobalProxySearchResult;
import org.knime.core.util.proxy.testing.HttpbinTestContext;
import org.knime.core.util.proxy.testing.ProxyParameterProvider;
import org.knime.core.util.proxy.testing.TinyproxyTestContext;

/**
 * Tests the globally-set {@link ProxySelectorAdapter} and {@link ProxyAuthenticatorAdapter}.
 * This test is very similar to {@link GlobalProxySearchTest}.
 *
 * Yes, this is technically the wrong test package those classes, but the {@link GlobalProxyTestContext}
 * has package-scope API, because of some package-scope-only types.
 */
class JavaNetAdaptersTest {

    @RegisterExtension
    private static final ProxyParameterProvider TEST_CONTEXT = GlobalProxyTestContext.INSTANCE;

    // -- ASSERTIONS --

    private static void assertProxySelector(final ProxyProtocol protocol, final Proxy expected) {
        final var scheme = StringUtils.lowerCase(protocol.name());
        final var uri = HttpbinTestContext.getURI(scheme);

        // compare to expected with URI
        assertThat(ProxySelector.getDefault().select(uri)) //
            .as("Proxy selector did not find the matching java.net.Proxy") //
            .usingRecursiveComparison() //
            .ignoringFields("version") // ignoring SOCKS version as it requires sun.net access
            .isEqualTo(List.of(expected));
    }

    private static void assertGlobalAuthenticator(final GlobalProxyConfig config,
        final PasswordAuthentication expected) {
        assertLocalAuthenticator(Authenticator.getDefault(), config, expected);
    }

    private static void assertLocalAuthenticator(final Authenticator authenticator, final GlobalProxyConfig config, final PasswordAuthentication expected) {
        final var scheme = StringUtils.lowerCase(config.protocol().name());
        URL url = null;
        try {
            url = new URI("%s://%s".formatted(scheme, targetHost)).toURL();
        } catch (URISyntaxException | MalformedURLException ex) { // NOSONAR - URL remains null for SOCKS proxies
        }

        // compare to expected with requesting parameters
        assertThat(authenticator.requestPasswordAuthenticationInstance(config.host(), null, config.intPort(), "Basic",
            null, scheme, url, RequestorType.PROXY)) //
                .as("Authenticator did not find the matching java.net.PasswordAuthentication") //
                .usingRecursiveComparison() //
                .withEqualsForType((t1, t2) -> { //
                    return Strings.CI.equals(t1.getUserName(), t2.getUserName()) && Objects.deepEquals(t1.getPassword(), t2.getPassword());
                }, PasswordAuthentication.class)
                .isEqualTo(expected);
    }

    // -- SETUP --

    private static String targetHost;

    @BeforeAll
    static void setupTarget() {
        targetHost = HttpbinTestContext.getHost();
    }

    private static ProxySelector previousSelector;

    private static Authenticator previousAuthenticator;

    @BeforeAll
    static void setupJavaNet() {
        previousSelector = ProxySelector.getDefault();
        previousAuthenticator = Authenticator.getDefault();
        ProxySelectorAdapter.installProxySelector();
        DelegatingAuthenticator.installAuthenticators();
    }

    @AfterAll
    static void teardownJavaNet() {
        ProxySelector.setDefault(previousSelector);
        Authenticator.setDefault(previousAuthenticator);
    }

    // -- TESTS --

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testEmptyConfig(final ProxyProtocol protocol) throws IOException {
        final var proxy = TinyproxyTestContext.getWithoutAuth(protocol);

        // empty search result
        TEST_CONTEXT.withEmpty(() -> {
            assertProxySelector(protocol, Proxy.NO_PROXY);
            assertGlobalAuthenticator(proxy, null);
        });

        // technically valid search result, but file:// is caught early
        TEST_CONTEXT.withConfig(proxy, () -> {
                assertThat(ProxySelector.getDefault().select(HttpbinTestContext.getURI("file"))) //
                    .as("File URI should not have received a proxy, but has") //
                    .isEqualTo(List.of(Proxy.NO_PROXY));
                assertGlobalAuthenticator(proxy, null);
            });
    }

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testSpecificConfig(final ProxyProtocol protocol) throws IOException {
        // dummy proxy, for assertion of config values (different to tinyproxy)
        final var proxy =
            new GlobalProxyConfig(protocol, "somehost", "1234", true, "user", "password", true, "localhost");
        final var auth = new PasswordAuthentication(proxy.username(), proxy.password().toCharArray());

        // assert proxy selection, the converted authenticator, and the global authenticator adapter
        TEST_CONTEXT.withConfig(proxy, () -> {
            assertProxySelector(protocol, proxy.forJavaNetProxy().getFirst());
            assertLocalAuthenticator(proxy.forJavaNetProxy().getSecond(), proxy, auth);
            assertGlobalAuthenticator(proxy, auth);
        });
    }

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testMultipleConfigs(final ProxyProtocol protocol) throws IOException {
        final var plainProxy = new GlobalProxyConfig(protocol, "somehost", null, false, null, null, false, null);
        final var excludingProxy =
            new GlobalProxyConfig(protocol, "somehost", null, false, null, null, true, targetHost);

        // first one is stop
        TEST_CONTEXT.withTwoResults(GlobalProxySearchResult.stop(),
            GlobalProxySearchResult.found(plainProxy), () -> {
                assertProxySelector(protocol, Proxy.NO_PROXY);
                assertGlobalAuthenticator(plainProxy, null);
            });

        // first one is empty
        TEST_CONTEXT.withTwoResults(GlobalProxySearchResult.empty(),
            GlobalProxySearchResult.found(plainProxy), () -> {
                assertProxySelector(protocol, plainProxy.forJavaNetProxy().getFirst());
                assertGlobalAuthenticator(plainProxy, null);
            });

        // first one actually excludes the host, i.e. NO_PROXY result
        TEST_CONTEXT.withTwoResults(GlobalProxySearchResult.found(excludingProxy),
            GlobalProxySearchResult.empty(), () -> {
                assertProxySelector(protocol, Proxy.NO_PROXY);
                assertGlobalAuthenticator(excludingProxy, null);
            });
    }

    /**
     * Tests the {@link GlobalProxySearch} with the Java-based strategy only.
     * <ul>
     * <li>Testing the {@link EclipseProxyStrategy} is not possible since not in OSGi bundle.</li>
     * <li>Testing the {@link EnvironmentProxyStrategy} is not possible due to the immutability of
     * {@link System#getenv()}. See also {@code EnvironmentProxyConfigProviderTest}.
     * </ul>
     */
    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testSearchInStrategy(final ProxyProtocol protocol) throws IOException {
        String proxyHost, proxyPort, proxyUser, proxyPassword;
        if (protocol == ProxyProtocol.SOCKS) {
            proxyHost = "socksProxyHost";
            proxyPort = "socksProxyPort";
            proxyUser = "java.net.socks.username";
            proxyPassword = "java.net.socks.password";
        } else {
            final var scheme = StringUtils.lowerCase(protocol.name());
            proxyHost = "%s.proxyHost".formatted(scheme);
            proxyPort = "%s.proxyPort".formatted(scheme);
            proxyUser = "%s.proxyUser".formatted(scheme);
            proxyPassword = "%s.proxyPassword".formatted(scheme);
        }
        final var proxy = TinyproxyTestContext.getWithAuth(protocol);
        final var auth = new PasswordAuthentication(proxy.username(), proxy.password().toCharArray());

        GlobalProxyTestContext.withFoundInJava(Map.of( //
            proxyHost, proxy.host(), proxyPort, proxy.port(), //
            proxyUser, proxy.username(), proxyPassword, proxy.password()), //
            () -> {
                assertProxySelector(protocol, proxy.forJavaNetProxy().getFirst());
                assertGlobalAuthenticator(proxy, auth);

                // assert that other protocols are not found (i.e. empty search)
                Arrays.stream(ProxyProtocol.values()) //
                    .filter(p -> p != protocol) //
                    .forEach(p -> {
                        /*
                         * Why this exception?
                         * > We use the default java.net.ProxySelector (from sun.net.spi) as fallback if
                         *   the proxy search returns nothing - no exclusion - just nothing.
                         * > We aim at an explicit query-response API which does not do silent defaulting, e.g.
                         *   requesting proxies for HTTP, HTTPS, SOCKS consistently works when using the
                         *   #getCurrentFor(URI, ProxyProtocol...) method. However, since we give control back to
                         *   java.net here, it uses it's defaulting behavior which (in order of precedence) is:
                         *      HTTP  -> HTTP, SOCKS
                         *      HTTPS -> HTTPS, SOCKS
                         *      SOCKS -> SOCKS
                         * > Thus, when *not* using socks (i.e. protocol == SOCKS here), the HTTP(S) queries
                         *   still find a proxy which we just expected here.
                         */
                        final var expectedJavaNetProxy = protocol == ProxyProtocol.SOCKS //
                            ? proxy.forJavaNetProxy().getFirst() //
                            : Proxy.NO_PROXY;
                        assertProxySelector(p, expectedJavaNetProxy);
                    });
            });
    }

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testSearchForExcludedHosts(final ProxyProtocol protocol) throws IOException {
        final var scheme = StringUtils.lowerCase(protocol.name());
        final var uri = HttpbinTestContext.getURI(scheme);
        final var proxy = new GlobalProxyConfig(protocol, "somehost", null, false, null, null, true, targetHost);

        // assert that host exclusion *is* performed in selector
        TEST_CONTEXT.withConfig(proxy, () -> {
            assertThat(ProxySelector.getDefault().select(uri)) //
                .as("Proxy selector performed host exclusion, but does not") //
                .usingRecursiveComparison() //
                .isEqualTo(List.of(Proxy.NO_PROXY));
        });
    }
}
