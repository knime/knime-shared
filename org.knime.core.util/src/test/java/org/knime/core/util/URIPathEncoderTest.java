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
 *   Mar 16, 2018 (Tobias Urhaug): created
 */
package org.knime.core.util;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

/**
 * Test cases for URIPathEncoder.
 *
 * @author Tobias Urhaug
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings("static-method")
public final class URIPathEncoderTest {

    private static void assertEncoded(final URI expected, final URI uri) {
        Assert.assertEquals("Unexpected encoded URI", expected, URIPathEncoder.UTF_8.encodePathSegments(uri));
    }

    private static void assertEncoded(final URL expected, final URL url) {
        Assert.assertEquals("Unexpected encoded URI", expected, URIPathEncoder.UTF_8.encodePathSegments(url));
    }

    /**
     * Checks that the encoder does not change the syntax of a URI.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testEncodingURIWithoutSpecialCharactersLeavesItUnchanged() throws Exception {
        URI uri = new URI("file://knime.mountpoint/path/file.txt");
        assertEncoded(uri, uri);
    }

    /**
     * Checks that reserved characters are not encoded.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testURIReservedCharactersAreNotEncoded() throws Exception {
        URI uri = new URI("file://knime.mountpoint/pa+th/file@.txt");
        assertEncoded(uri, uri);
    }

    /**
     * Checks that German umlauts are encoded.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testUmlautsAreEncoded() throws Exception {
        URI umlautURI = new URI("file://knime.mountpoint/ä/Ä.txt");
        URI encodedURI = new URI("file://knime.mountpoint/%C3%A4/%C3%84.txt");
        assertEncoded(encodedURI, umlautURI);
    }

    /**
     * Checks that a UNC path has correct syntax after encoding.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testUNCPathHasFourLeadingSlashes() throws Exception {
        Assume.assumeTrue("Only makes sense under Windows", SystemUtils.IS_OS_WINDOWS);

        URI uncURI = new File("\\\\HOST\\path\\file.txt").toURI();
        URI expectedUNC = new URI("file:////HOST/path/file.txt");
        assertEncoded(expectedUNC, uncURI);
    }

    /**
     * Checks that a windows local path has correct syntax after encoding.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testWindowsLocalPath() throws Exception {
        Assume.assumeTrue("Only makes sense under Windows", SystemUtils.IS_OS_WINDOWS);

        URI windowsURI = new File("C:\\path\\fileÄ.txt").toURI();
        URI expectedWindowsURI = new URI("file:/C:/path/file%C3%84.txt");
        assertEncoded(expectedWindowsURI, windowsURI);
    }

    /**
     * Checks that a URL is correctly converted to a URI and back to a URL within the method call.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testURLIsEncoded() throws Exception {
        URL url = new URI("file://knime.mountpoint/path/fileÄ.txt").toURL();
        URL expectedEncodedURL = new URI("file://knime.mountpoint/path/file%C3%84.txt").toURL();
        assertEncoded(expectedEncodedURL, url);
    }

    /**
     * Tests that a whitespace in a UNC path is correctly encoded to %20.
     *
     * @throws Exception
     */
    @Test
    public void testWhitespaceInUncPathIsEncodedToPercentTwenty() throws Exception {
        Assume.assumeTrue("Only makes sense under Windows", SystemUtils.IS_OS_WINDOWS);

        URI uncURI = new File("\\\\HOST\\path\\file with whitespace.txt").toURI();
        URI expectedUNC = new URI("file:////HOST/path/file%20with%20whitespace.txt");
        assertEncoded(expectedUNC, uncURI);
    }

}
