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
 *   Mar 13, 2018 (Tobias Urhaug): created
 */
package org.knime.core.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

/**
 * Utility class for encoding the path segments of URIs/URLs.
 *
 * @author Tobias Urhaug
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public final class URIPathEncoder {

    /** Encoder using the UTF-8 charset. */
    public static final URIPathEncoder UTF_8 = new URIPathEncoder(StandardCharsets.UTF_8);

    private static final Logger LOGGER = Logger.getLogger(URIPathEncoder.class.getName());

    private static final String UNC_PREFIX = "//";

    private final Charset m_encoding;

    /**
     * Returns an encoder instance for the given charset.
     *
     * @param charset character set for encoding the URI
     * @return encoder instance
     */
    public static URIPathEncoder forCharset(final Charset charset) {
        return charset.equals(StandardCharsets.UTF_8) ? UTF_8 : new URIPathEncoder(charset);
    }

    /**
     * Extract, decode and return the path part of a given URL. If the path part is not valid according to the spec,
     * assume it is already decoded and return it unchanged.
     * @param url The URL to extract the decoded path part from.
     * @return The decoded path part of the given URL, i.e. any escaped hex sequences in the shape of "% hex hex"
     *         (potentially repeated) are decoded into their respective Unicode characters.
     * @see URI#getPath()
     */
    public static String decodePath(final URL url) {
        try {
            // Obtain the decoded path part using java.net.URI. Note that using java.net.URLDecoder follows a different
            // spec and is not correct (see AP-17103).
            // We must not use something like `new URI( input.getPath() )` here because inputs
            // containing double slashes at the beginning of the path part (e.g. knime://knime.workflow//Files;
            // these are indeed technically valid) will lead the `URI` constructor to incorrectly interpret this as a
            // scheme part.
            // Instead, we can rely on converting the entire `URL` to `URI`. `URI#getPath` will decode its path part.
            return url.toURI().getPath();
        } catch (URISyntaxException e) {
            LOGGER.log(Level.INFO, e, () -> "URL path could not be encoded: '" + url + "'");
            // In this case, we assume there are disallowed characters in the path string, although
            // there are other instances that also trigger a parse error. This means this method does not enforce
            // or ensure that the given and returned paths are actually valid.
            // Assuming there are disallowed characters, we conclude that the string is already decoded and return it
            // as-is. (Checking for encoded-ness is not trivial because of ambiguous instances such as "per%cent" which
            // may be taken literally or interpret %ce as a byte pair representing an encoded character.)
            return url.getPath();
        }
    }

    /**
     * Creates a URI encoder with the given encoding.
     *
     * @param encoding character encoding
     */
    private URIPathEncoder(final Charset encoding) {
        m_encoding = encoding;
    }
    /**
     * Encodes the path segments of a URL.
     *
     * @param url URL to be encoded
     * @return an equivalent URL with encoded path or the input URL itself if it has
     * a syntax error.
     */
    public URL encodePathSegments(final URL url) {
        try {
            return encodePathSegments(url.toURI()).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            LOGGER.log(Level.INFO, e, () -> "Could not encode path segments in URL '" + url + "'.");
            return url;
        }
    }

    /**
     * Encodes the path segments of a URI.
     *
     * @param uri URI to be encoded
     * @return an equivalent URI with all path segment encoded
     */
    public URI encodePathSegments(final URI uri) {
        final var host = uri.getHost();
        final var path = uri.getPath();

        // A UNC URI has no host and its scheme specific part must start with four leading slashes:
        // In `file:////path/segments` the scheme is "file", the host is "" and the path is "//path/segments".
        final var isUNC = StringUtils.isEmpty(host) && path != null && path.startsWith(UNC_PREFIX);

        try {
            final var uriBuilder = new URIBuilder(uri, m_encoding);

            // The `setPath` method must be called as it is where the actual encoding happens.
            // The Apache `URIBuilder` does not encode the URI in the builder constructor.
            uriBuilder.setPath(uri.getPath());

            if (isUNC) {
                // the host has to be set to an empty string to preserve the four slashes, otherwise only two remain
                uriBuilder.setHost("");
            }
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            LOGGER.log(Level.INFO, e,
                () -> "Could not encode path segments in " + (isUNC ?  "UNC " : "") + "URI '" + uri + "'.");
            return uri;
        }
    }
}
