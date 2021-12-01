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
 *   17.11.2021 (thor): created
 */
package org.knime.core.util.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.UUID;

import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Testcases for {@link Encrypter}.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 */
public class EncrypterTest {
    /**
     * Test round tripping with the current version.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRoundTripping() throws Exception {
        var encrypter = new Encrypter("some random password");

        String plain;
        try (var is = getClass().getResourceAsStream("plain.txt")) {
            plain = IOUtils.toString(is, StandardCharsets.UTF_8);
        }
        var encrypted = encrypter.encrypt(plain);
        assertThat(encrypter.decrypt(encrypted)).as("Round tripping failed").isEqualTo(plain);

        encrypted = encrypter.encrypt(plain, 42);
        assertThat(encrypter.decrypt(encrypted)).as("Round tripping with explicit salt failed").isEqualTo(plain);
    }

    /**
     * Checks that random salts are used.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testNull() throws Exception {
        var encrypter = new Encrypter("some random password");

        assertThat(encrypter.encrypt(null)).as("Null not encrypted correctly").isNull();
        assertThat(encrypter.decrypt(null)).as("Null not decrypted correctly").isNull();
        assertThat(encrypter.decrypt("")).as("Empty string not decrypted correctly").isEmpty();
        assertThat(encrypter.encrypt("")).as("Empty string not encrypted correctly").isNotEmpty();

        assertThrows(IllegalArgumentException.class, () -> new Encrypter(null));
        assertThrows(IllegalArgumentException.class, () -> new Encrypter(""));
    }

    /**
     * Checks handling of <code>null</code> values.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testSalt() throws Exception {
        var encrypter = new Encrypter("some random password");

        var plain = UUID.randomUUID().toString();

        assertThat(encrypter.encrypt(plain)).as("No salt used").isNotEqualTo(encrypter.encrypt(plain));
    }


    /**
     * Tests behaviour of secret key is wrong.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testWrongKey() throws Exception {
        var encrypter = new Encrypter("some random password");

        var plain = UUID.randomUUID().toString();
        var encrypted = encrypter.encrypt(plain);

        var encrypter2 = new Encrypter("some other key");
        assertThrows(InvalidKeyException.class, () -> encrypter2.decrypt(encrypted));
    }

    /**
     * Tests runtime behavior when using small key iteration count, accepting weak encrypting (came up as part of
     * https://knime-com.atlassian.net/browse/AP-17592)
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 1000)
    public void testSpeedyWeakEncryption() throws Exception { // NOSONAR timeout is checked in method annotation
        for (int i = 0; i < 1000; i++) {
            var encrypter = new Encrypter("key " + i, 100);
            encrypter.encrypt("some string", 42);
        }
    }

    /**
     * Test decryption of data encrypted with a previos version.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testOldVersions() throws Exception {
        var encrypter = new Encrypter("some random password");

        String plain;
        try (var is = getClass().getResourceAsStream("plain.txt")) {
            plain = IOUtils.toString(is, StandardCharsets.UTF_8);
        }

        for (int i = 1; i <= 2; i++) {
            String enc;
            try (var is = getClass().getResourceAsStream("encrypted-v" + i + ".txt")) {
                enc = IOUtils.toString(is, StandardCharsets.UTF_8);
            }

            assertThat(encrypter.decrypt(enc)).as("Reading version %d failed", i).isEqualTo(plain);
        }
    }

    /**
     * Test that passing invalid inputs to the decrypt method is handled gracefully.
     * @throws Exception
     */
    @Test
    public void invalidDecryptionInput() throws Exception {
        IEncrypter enc = new Encrypter("AKeyForTestingTheEncrypter");
        assertThrows(IllegalArgumentException.class, () -> enc.decrypt("z"));
        assertThrows(IllegalArgumentException.class, () -> enc.decrypt("something"));
        assertThrows(IllegalBlockSizeException.class, () -> enc.decrypt("01something"));
        assertThrows(IllegalArgumentException.class, () -> enc.decrypt("02something"));
    }

    /**
     * Test whether encryption with fixed salt produces the same encrypted string.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFixSalt_Bug5813() throws Exception {
        IEncrypter enc = new Encrypter("AKeyForTestingTheEncrypter");

        String text = null;
        String encryptedText = enc.encrypt(text, 27);
        String encryptedText2 = enc.encrypt(text, 27);
        assertThat(encryptedText2).as("Unexpected result for null text").isEqualTo(encryptedText);

        text = "";
        encryptedText = enc.encrypt(text, 27);
        encryptedText2 = enc.encrypt(text, 27);
        assertThat(encryptedText2).as("Unexpected result for empty text").isEqualTo(encryptedText);

        text = "äüößjjjhdshvoihgudfhgdfbv";
        encryptedText = enc.encrypt(text, 27);
        encryptedText2 = enc.encrypt(text, 27);
        assertThat(encryptedText2).as("Unexpected result for real text").isEqualTo(encryptedText);

        text = "äüößjjjhdshvoihgudfhgdfbv";
        encryptedText = enc.encrypt(text, 27);
        encryptedText2 = enc.encrypt(text, 28);
        assertThat(encryptedText2).as("Unexpected result for different salts").isNotEqualTo(encryptedText);
    }
}
