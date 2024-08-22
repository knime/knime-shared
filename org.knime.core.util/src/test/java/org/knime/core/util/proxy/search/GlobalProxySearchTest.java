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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.knime.core.util.HttpbinTestContext;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.ProxyProtocol;
import org.knime.core.util.proxy.TinyproxyTestContext;
import org.knime.core.util.proxy.search.GlobalProxyStrategy.GlobalProxySearchResult;

/**
 * Tests the public, static API of the {@link GlobalProxySearch} class.
 */
class GlobalProxySearchTest {

    // -- ASSERTIONS --

    private static void assertGlobalProxySearch(final ProxyProtocol protocol,
        final Optional<GlobalProxyConfig> expected) {
        // compare to expected with protocol
        assertThat(GlobalProxySearch.getCurrentFor(protocol)) //
            .as("Proxy search did not find the matching config") //
            .usingRecursiveComparison() //
            .isEqualTo(expected);

        final var scheme = StringUtils.lowerCase(protocol.name());
        final var uri = HttpbinTestContext.getURI(scheme);

        // compare to expected with URI
        assertThat(GlobalProxySearch.getCurrentFor(uri)) //
            .as("Proxy search did not find the matching config") //
            .usingRecursiveComparison() //
            .isEqualTo(expected);
    }

    // -- SETUP --

    private static String targetHost;

    // not making any HTTP requests, just for testing config creation and search
    private static String proxyHost;

    @BeforeAll
    static void setupTargetAndProxy() {
        targetHost = HttpbinTestContext.getHost();
        proxyHost = TinyproxyTestContext.getWithoutAuth(ProxyProtocol.HTTP).host();
    }

    // -- TESTS --

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testEmptyConfig(final ProxyProtocol protocol) throws IOException {
        // empty search result
        GlobalProxyTestContext.withEmpty(() -> {
            assertGlobalProxySearch(protocol, Optional.empty());
        });

        // technically valid search result, but file:// is caught early
        GlobalProxyTestContext
            .withConfig(new GlobalProxyConfig(protocol, proxyHost, null, false, null, null, false, null), () -> {
                assertThat(GlobalProxySearch.getCurrentFor(HttpbinTestContext.getURI("file"))) //
                    .as("File URI should not have received a proxy, but has") //
                    .isEmpty();
            });
    }

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testSpecificConfig(final ProxyProtocol protocol) throws IOException {
        final var fullConfig =
            new GlobalProxyConfig(protocol, proxyHost, "1234", true, "user", "password", true, "localhost");
        GlobalProxyTestContext.withConfig(fullConfig, () -> {
            assertGlobalProxySearch(protocol, Optional.of(fullConfig));
        });
    }

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testMultipleConfigs(final ProxyProtocol protocol) throws IOException {
        final var plainConfig = new GlobalProxyConfig(protocol, proxyHost, null, false, null, null, false, null);
        final var excludingConfig =
            new GlobalProxyConfig(protocol, proxyHost, null, false, null, null, true, targetHost);

        // first one is stop
        GlobalProxyTestContext.withTwoResults(GlobalProxySearchResult.stop(),
            GlobalProxySearchResult.found(plainConfig), () -> {
                assertGlobalProxySearch(protocol, Optional.empty());
            });

        // first one is empty
        GlobalProxyTestContext.withTwoResults(GlobalProxySearchResult.empty(),
            GlobalProxySearchResult.found(plainConfig), () -> {
                assertGlobalProxySearch(protocol, Optional.of(plainConfig));
            });

        // first one is present but excluded (should still be found)
        GlobalProxyTestContext.withTwoResults(GlobalProxySearchResult.found(excludingConfig),
            GlobalProxySearchResult.empty(), () -> {
                assertGlobalProxySearch(protocol, Optional.of(excludingConfig));
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
        String proxyHost, proxyPort;
        if (protocol == ProxyProtocol.SOCKS) {
            proxyHost = "socksProxyHost";
            proxyPort = "socksProxyPort";
        } else {
            final var scheme = StringUtils.lowerCase(protocol.name());
            proxyHost = "%s.proxyHost".formatted(scheme);
            proxyPort = "%s.proxyPort".formatted(scheme);
        }
        final var plainConfig =
            new GlobalProxyConfig(protocol, proxyHost, "8888", false, null, null, false, null);

        GlobalProxyTestContext
            .withFoundInJava(Map.of(proxyHost, plainConfig.host(), proxyPort, plainConfig.port()), () -> {
                assertGlobalProxySearch(protocol, Optional.of(plainConfig));

                // assert that other protocols are not found (i.e. empty search)
                Arrays.stream(ProxyProtocol.values()) //
                    .filter(p -> p != protocol) //
                    .forEach(p -> {
                        assertGlobalProxySearch(p, Optional.empty());
                    });
            });
    }

    @ParameterizedTest
    @EnumSource(ProxyProtocol.class)
    void testSearchForExcludedHosts(final ProxyProtocol protocol) throws IOException {
        final var scheme = StringUtils.lowerCase(protocol.name());
        final var excludingConfig =
            new GlobalProxyConfig(protocol, proxyHost, null, false, null, null, true, targetHost);

        // assert that host exclusion is not performed in search
        GlobalProxyTestContext.withConfig(excludingConfig, () -> {
            assertThat(GlobalProxySearch.getCurrentFor(protocol)) //
                .as("Proxy search should not perform host exclusion, but does") //
                .isNotEmpty() //
                .map(cfg -> cfg.isHostExcluded(HttpbinTestContext.getURI(scheme))) //
                .as("Found proxy config should exclude the host, that it was searched for") //
                .isEqualTo(Optional.of(true));
        });
    }
}
