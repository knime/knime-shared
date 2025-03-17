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

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.knime.core.node.util.CheckUtils;

/**
 * Utility class to add {@code ItemVersion} information to {@link URI}s. This class replaces the old
 * {@code HubItemVersion} class in core, but does <b>not</b> support the legacy "Space Versioning" anymore.
 *
 * @author Manuel Hotz, KNIME GmbH, Konstanz, Germany
 * @since 6.5
 */
public final class ItemVersionURIUtil {

    private static final Log LOGGER = LogFactory.getLog(ItemVersionURIUtil.class.getName());

    private static final String MOST_RECENT_IDENTIFIER = "most-recent";

    private static final String CURRENT_STATE_IDENTIFIER = "current-state";

    private static final String VERSION_QUERY_PARAM = "version";

    private ItemVersionURIUtil() {
        // utility class
    }

    // these methods formerly lived in the HubItemVersion class

    /**
     * Gets the version as a query parameter.
     *
     * @param version the version to get the query parameter for
     * @return the value for the version query parameter, e.g., "most-recent" or "4". Empty if this refers to the
     *         current state.
     */
    public static Optional<String> getQueryParameterValue(final ItemVersion version) {
        return match(version, //
            c -> Optional.empty(), //
            m -> Optional.of(MOST_RECENT_IDENTIFIER), //
            v -> Optional.of(Integer.toString(v.version())) //
        );
    }

    // URI applyTo(URI) should be handled by hubclient-sdk service API

    /**
     * Tries to parse the {@link ItemVersion} from the given URI.
     *
     * @param uri to parse the version from
     * @return the parsed {@link ItemVersion} or {@link Optional#empty()} if the version could not be parsed
     */
    public static Optional<ItemVersion> parseVersion(final URI uri) {
        CheckUtils.checkArgumentNotNull(uri);
        return fromQueryParameters(uri.getQuery());
    }

    /**
     * Parses the first occurrence of the "version" query parameter from the given query string into an ItemVersion.
     *
     * @param query query string to parse ItemVersion from
     * @return
     */
    private static Optional<ItemVersion> fromQueryParameters(final String query) {
        if (query != null && !query.isBlank()) {
            // parse "version" parameter from query string and parse its value into an ItemVersion
            final var versionParams = URLEncodedUtils.parse(query, StandardCharsets.UTF_8).stream()
                .filter(p -> VERSION_QUERY_PARAM.equals(p.getName())).toList();
            if (versionParams.size() > 1) {
                // TODO exception? debug? try first successful one?
                // old code threw IllegalArgumentException in case of multiple inconsistent _values_
                LOGGER.warn("Multiple \"version\" query parameters found in URI query string \"%s\", taking first one"
                    .formatted(query));
            }
            for (final var param : versionParams) {
                if (VERSION_QUERY_PARAM.equals(param.getName())) {
                    final var value = CheckUtils.checkArgumentNotNull(param.getValue(),
                        "Version parameter cannot be empty in query parameters \"%s\"", query);
                    return match(value);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Adds the version information to the query parameters of the URI.
     *
     * @param version version to add to URI
     * @param uriBuilder URI builder to add version to
     */
    public static void addVersionToURI(final ItemVersion version, final URIBuilder uriBuilder) {
        if (!version.isVersioned()) {
            // current-state does not need version parameter, so we omit it like the old method did
            return;
        }
        uriBuilder.addParameter(VERSION_QUERY_PARAM, format(version));
    }

    // these methods formerly lived in the core TemplateUpdateUtil#LinkType class

    /**
     * Gets the value used in query parameters to denote a specific link type.
     *
     * @param version version to get identifier for
     *
     * @return the value used in query parameters to denote a specific link type or {@code null} for a fixed,
     *         non-floating version.
     */
    public static String getIdentifier(final ItemVersion version) {
        return match(version, //
            c -> CURRENT_STATE_IDENTIFIER, //
            m -> MOST_RECENT_IDENTIFIER, //
            v -> null //
        );
    }

    /**
     * Gets the string for the {@code version} query parameter value.
     *
     * @param version version to get query parameter string for
     * @return the string for the {@code version} query parameter value
     */
    public static String getQueryParameterString(final ItemVersion version) {
        return format(version);
    }

    // Helper methods

    /**
     * Matches one of the functions with the given version and applies it.
     *
     * @param <T> return type of the functions
     * @param version version to apply functions to
     * @param currentStateFn function to apply if given current-state version
     * @param mostRecentFn function to apply if given most-recent version
     * @param versionFn function to apply if given fixed version number
     * @return function return value
     */
    private static <T> T match(final ItemVersion version, final Function<CurrentState, T> currentStateFn,
        final Function<MostRecent, T> mostRecentFn, final Function<SpecificVersion, T> versionFn) {
        // good candidate for pattern-matching switch in next Java version (preview in Java 17)
        if (version instanceof CurrentState cs) {
            return currentStateFn.apply(cs);
        }
        if (version instanceof MostRecent mr) {
            return mostRecentFn.apply(mr);
        }
        // safe cast because of sealed interface
        return versionFn.apply((SpecificVersion)version);
    }

    /**
     * Matches the given query parameter value potentially representing an {@link ItemVersion} to the proper
     * {@link ItemVersion} if possible.
     *
     * @param itemVersionParamValue query parameter value to match to {@link ItemVersion}
     * @return {@link ItemVersion} if it can be matched (parsed), otherwise {@link Optional#empty()}
     */
    private static Optional<ItemVersion> match(final String itemVersionParamValue) {
        if (CURRENT_STATE_IDENTIFIER.equals(itemVersionParamValue)) {
            return Optional.of(new CurrentState());
        }
        if (MOST_RECENT_IDENTIFIER.equals(itemVersionParamValue)) {
            return Optional.of(new MostRecent());
        }
        try {
            return Optional.of(new SpecificVersion(Integer.parseUnsignedInt(itemVersionParamValue)));
        } catch (final NumberFormatException e) { // NOSONAR yes, we want to parse and return Optional.empty if it fails
            return Optional.empty();
        }
    }

    private static String format(final ItemVersion version) {
        return match(version, //
            c -> CURRENT_STATE_IDENTIFIER, //
            m -> MOST_RECENT_IDENTIFIER, //
            v -> Integer.toString(v.version()) //
        );
    }

}
