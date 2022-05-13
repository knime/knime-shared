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
 *   13 May 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.text.util;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.knime.core.node.util.CheckUtils;
import org.knime.shared.workflow.def.WorkflowDef;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Used to annotate copy & paste payload with version and payload identification metadata.
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
@JsonPropertyOrder(alphabetic = true)
public class DefClipboardContent {

    /**
     * The version of the clipboard payload metadata format. Will be increased if additional information is added.
     *
     * @author Carl Witt, KNIME AG, Zurich, Switzerland
     */
    public enum Version {
            /**
             * First version has three fields:
             * <ul>
             * <li>version: signifies this schema.</li>
             * <li>payloadIdentifier: A universally unique identifier for the payload.</li>
             * <li>payload: a {@link WorkflowDef} that is the copy payload.</li>
             * </ul>
             */
            KNIME_CLIPBOARD_CONTENT_V1;

        /**
         * @return the most recent version
         */
        public static Version latest() {
            final Version[] values = Version.values();
            return values[values.length - 1];
        }
    }

    /**
     * @param string any string
     * @return an empty optional if the string is null or cannot be parsed as instance of this class.
     */
    public static Optional<DefClipboardContent> valueOf(final String string){
        if(string == null) {
            return Optional.empty();
        }
        try {
            DefClipboardContent deserialized = ObjectMapperUtil.fromString(string, DefClipboardContent.class);
            return Optional.of(deserialized);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DefClipboardContent.class.getName()).info(ex.toString());
            return Optional.empty();
        }
    }

    /**
     * Every new instance of this class should have the latest version.
     *
     * Conversion of old metadata to the current format should happen in the deserialization constructor.
     */
    @JsonProperty("version")
    final Version m_version = Version.latest();

    /**
     * Correlates redacted and original clipboard payloads. Never null.
     */
    @JsonProperty("payloadIdentifier")
    private final UUID m_payloadIdentifier;

    /**
     * The contents to paste. Never null.
     */
    @JsonProperty("payload")
    final WorkflowDef m_payload;

    /**
     * @param payloadIdentifier correlates equivalent contents
     * @param payload the clipboard content to attach metadata to
     */
    public DefClipboardContent(final UUID payloadIdentifier, final WorkflowDef payload) {
        CheckUtils.checkArgumentNotNull(payloadIdentifier);
        CheckUtils.checkArgumentNotNull(payload);
        m_payloadIdentifier = payloadIdentifier;
        m_payload = payload;
    }

    /**
     * Automatically generates a random identifier for the payload.
     *
     * @param payload the non-null clipboard content to attach metadata to
     */
    public DefClipboardContent(final WorkflowDef payload) {
        this(UUID.randomUUID(), payload);
    }

    /**
     * Only for Jackson deserialization.
     *
     * @param version format version
     * @param payloadIdentifier correlates equivalent contents
     * @param payload the clipboard content to attach metadata to
     */
    public DefClipboardContent(final @JsonProperty("version") Version version,
        final @JsonProperty("payloadIdentifier") UUID payloadIdentifier,
        final @JsonProperty("payload") WorkflowDef payload) {
        this(payloadIdentifier, payload);
        CheckUtils.checkArgument(version == Version.latest(), "Unsupported KNIME clipboard content version.");
    }

    /**
     * @return the version that defines the schema of this clipboard content
     */
    public Version getVersion() {
        return m_version;
    }

    /**
     * For redacted clipboard payloads.
     *
     * Consider two clipboard payloads that have been created by copying the same nodes and annotations but one is
     * redacted to exclude passwords and the other is not.
     *
     * If the content of the system clipboard has the same payload identifier as the internal clipboard's contents, we
     * prefer the internal, non-redacted payload, e.g., to enable copy & paste of nodes with passwords within the same
     * AP but avoid exposing those passwords via the system clipboard.
     *
     * @return an identifier that correlates the redacted and non-redacted version of a clipboard payloads.
     */
    public UUID getPayloadIdentifier() {
        return m_payloadIdentifier;
    }

    /**
     * @return the payload of the clipboard content
     */
    public WorkflowDef getPayload() {
        return m_payload;
    }
}
