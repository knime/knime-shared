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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.ProxyProtocol;
import org.knime.core.util.proxy.search.GlobalProxyStrategy.GlobalProxySearchResult.SearchSignal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implements selecting a custom proxy-routing for a list of internal proxies.
 * This list is specified via the {@link Path} provided by an environment variable.
 * <p>
 * Attempts to parse the file at this {@link Path} on class initialization, then selects the proxy-routing
 * (i.e. the {@link GlobalProxyConfig}) based on an exact match of the incoming {@link URI}.
 * </p>
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
final class InternalProxyStrategy implements GlobalProxyStrategy {

    private static final Log LOGGER = LogFactory.getLog(InternalProxyStrategy.class);

    // empty default value, mutable for tests
    private static final Map<String, GlobalProxyConfig> INITIAL_MAPPING = new HashMap<>();

    // package scope for tests
    static final Map<String, GlobalProxyConfig> MAPPING = parseEnvironment(System.getenv());

    // package scope for tests
    static final String ENVIRONMENT_VARIABLE = "KNIME_INTERNAL_PROXIES_FILE";

    @Override
    public GlobalProxySearchResult getCurrentFor(final URI uri, final ProxyProtocol... protocols) {
        if (MAPPING.isEmpty() || uri == null || uri.getHost() == null) {
            return GlobalProxySearchResult.empty();
        }
        return new GlobalProxySearchResult(SearchSignal.EVALUATE, //
            Optional.ofNullable(MAPPING.get(uri.getHost())));
    }

    static Map<String, GlobalProxyConfig> parseEnvironment(final Map<String, String> environment) {
        final var value = environment.get(ENVIRONMENT_VARIABLE);
        if (value == null) {
            return INITIAL_MAPPING;
        }
        try {
            // (1) read file at path
            final var path = Paths.get(value);
            try (var in = Files.newInputStream(path)) {
                final var mapper = new ObjectMapper();

                // (2) parse into a list of `InternalHostConfig` objects
                final List<InternalHostConfig> configs = mapper.readValue( //
                    in, //
                    new TypeReference<List<InternalHostConfig>>() {
                    });

                // (3) create the hashed mapping for fast access
                return configs.stream() //
                    .flatMap(cfg -> cfg.matches().stream() //
                        .map(m -> Map.entry(m, cfg.toGlobalProxyConfig()))) //
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
            }
        } catch (IOException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Could not read internal proxies of from file path "
                    + "\"%s\" from environment variable \"%s\"".formatted(value, ENVIRONMENT_VARIABLE), e);
            }
            return INITIAL_MAPPING;
        }
    }

    /**
     * JSON schema that we expect from the {@link Path}, specified by the
     * environment variable {@value #ENVIRONMENT_VARIABLE}.
     *
     * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
     */
    static record InternalHostConfig(String host, int port, List<String> matches) {

        /**
         * Creates a simple {@link GlobalProxyConfig} from this instance.
         * Defaults to the {@link ProxyProtocol#HTTP}.
         *
         * @return proxy config
         */
        GlobalProxyConfig toGlobalProxyConfig() {
            return new GlobalProxyConfig( //
                ProxyProtocol.HTTP, // protocol is not relevant here, defaulting to HTTP
                host(), //
                String.valueOf(port()), //
                false, null, null, // no authentication
                false, null // no excluded hosts
            );
        }
    }
}
