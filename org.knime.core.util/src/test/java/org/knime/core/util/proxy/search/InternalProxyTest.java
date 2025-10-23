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
 *   Oct 23, 2025 (lw): created
 */
package org.knime.core.util.proxy.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.knime.core.util.proxy.search.InternalProxyStrategy.ENVIRONMENT_VARIABLE;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.knime.core.util.proxy.ProxyProtocol;
import org.knime.core.util.proxy.TinyproxyTestContext;
import org.knime.core.util.proxy.URLConnectionFactory;
import org.knime.core.util.proxy.search.InternalProxyStrategy.InternalHostConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests whether the environment variables are correctly parsed, looking for
 * the one specifying the JSON mapping of internal proxies.
 *
 * Expects the following JSON schema.
 * <pre>
 * [
 *   {
 *     "host": string,
 *     "port": int,
 *     "matches": [ string, ... ]
 *   },
 *   ...
 * ]
 * </pre>
 */
class InternalProxyTest {

    private static final String TEST_FILE = "internal-proxies.json";

    @Test
    void testParseEnvironment(@TempDir final Path temporaryDirectory) throws IOException {
        // nothing to parse, either empty or invalid
        assertThat(InternalProxyStrategy.parseEnvironment(Collections.emptyMap())) //
            .as("Internal proxies should be empty, is empty environment") //
            .isEmpty();
        assertThat(InternalProxyStrategy.parseEnvironment(Map.of(ENVIRONMENT_VARIABLE, "")))
            .as("Internal proxies should be empty, is empty environment variable")
            .isEmpty();
        final var invalidJson = temporaryDirectory.resolve(TEST_FILE);
        assertThat(InternalProxyStrategy.parseEnvironment(Map.of(ENVIRONMENT_VARIABLE, invalidJson.toString()))) //
            .as("Internal proxies should be empty, path does not point to file") //
            .isEmpty();
        Files.writeString(invalidJson, "{}", StandardCharsets.UTF_8);
        assertThat(InternalProxyStrategy.parseEnvironment(Map.of(ENVIRONMENT_VARIABLE, invalidJson.toString()))) //
            .as("Internal proxies should be empty, file at path has invalid schema") //
            .isEmpty();
        Files.writeString(invalidJson, "This is invalid JSON...", StandardCharsets.UTF_8);
        assertThat(InternalProxyStrategy.parseEnvironment(Map.of(ENVIRONMENT_VARIABLE, invalidJson.toString()))) //
            .as("Internal proxies should be empty, file at path has invalid JSON") //
            .isEmpty();

        // existing and valid JSON inputs (local files)
        final var localJson = getClass().getResource("/" + TEST_FILE).getPath();
        assertThat(InternalProxyStrategy.parseEnvironment(Map.of(ENVIRONMENT_VARIABLE, localJson))) //
            .as("Internal proxies should be non-empty, file path contains valid schema") //
            .containsExactlyInAnyOrderEntriesOf(Map.of( //
                "baz.stats", new InternalHostConfig("foo.bar", 8888, Collections.emptyList()).toGlobalProxyConfig() //
            ));
    }

    @Test
    void testIntegration(@TempDir final Path temporaryDirectory) throws IOException {
        final var proxy = TinyproxyTestContext.getWithoutAuth(ProxyProtocol.HTTP);
        final var host = TinyproxyTestContext.getStatsURI().getHost();

        // write the POJO to a temporary JSON file
        final var temporaryJson = temporaryDirectory.resolve(TEST_FILE);
        final var mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        mapper.writeValue(temporaryJson.toFile(), List.of( //
            new InternalHostConfig(proxy.host(), proxy.intPort(), List.of(host))));

        // test that proxy search currently returns nothing
        final var url = TinyproxyTestContext.getStatsURI().toURL();
        assertThat(GlobalProxySearch.getCurrentFor(url)) //
            .as("Proxy search should not have found any proxy") //
            .isEmpty();

        final var backup = new HashMap<>(InternalProxyStrategy.MAPPING);
        try {
            InternalProxyStrategy.MAPPING.clear();
            InternalProxyStrategy.MAPPING.putAll(InternalProxyStrategy.parseEnvironment( //
                Map.of(ENVIRONMENT_VARIABLE, temporaryJson.toString())));

            // test whether proxy search finds the entry
            assertThat(GlobalProxySearch.getCurrentFor(url)) //
                .as("Proxy search should have found internal proxy") //
                .isPresent() //
                .get() //
                .extracting("host", "port") //
                .as("Proxy host and port should have been correctly detected")
                .containsExactly(proxy.host(), proxy.port());

            // run actual integration test with HTTP request
            final var connection = (HttpURLConnection)URLConnectionFactory.getConnection(url);
            connection.connect();
            assertThat(connection.getResponseCode()) //
                .as("HTTP request to host behind internal proxy should have succeeded") //
                .isEqualTo(HttpURLConnection.HTTP_OK);

        } finally {
            InternalProxyStrategy.MAPPING.clear();
            InternalProxyStrategy.MAPPING.putAll(backup);
        }
    }
}
