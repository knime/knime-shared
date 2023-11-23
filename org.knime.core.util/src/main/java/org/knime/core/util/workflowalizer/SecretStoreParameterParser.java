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
 *   Nov 23, 2023 (Sascha Wolke, KNIME GmbH, Berlin, Germany): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class to parse secret store parameter artifacts.
 *
 * @author Sascha Wolke, KNIME GmbH, Berlin, Germany
 */
final class SecretStoreParameterParser {

    private SecretStoreParameterParser() {
        // utility class with static methods
    }

    /**
     * Parse the secret store parameter artifact and return the stored secret ids.
     *
     * @param secretStoreParameters the secret store parameter artifact JSON
     * @return set of secret ids from the parameters artifact or empty set if input is empty
     */
    public static Set<String> parseSecretIds(final String secretStoreParameters) {
        if (StringUtils.isBlank(secretStoreParameters)) {
            return Collections.emptySet();
        }

        try {
            final var objectMapper = new ObjectMapper();
            final var json = objectMapper.readTree(secretStoreParameters);

            if (!json.has("secretIds") || !json.get("secretIds").isArray()) {
                throw new IllegalArgumentException("Expected secret ids array in secret store parameters artifact.");
            }

            final var secretIds = new HashSet<String>();
            final var idsIterator = json.get("secretIds").elements();
            while (idsIterator.hasNext()) {
                secretIds.add(idsIterator.next().asText());
            }

            return Collections.unmodifiableSet(secretIds);

        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException( //
                "Unable to parse secret store parameters artifact: " + ex.getMessage(), ex);
        }
    }

}
