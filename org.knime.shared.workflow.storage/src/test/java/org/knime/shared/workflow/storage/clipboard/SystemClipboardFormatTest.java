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
 *   9 Sep 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.clipboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.knime.shared.workflow.def.impl.AnnotationDataDefBuilder;
import org.knime.shared.workflow.def.impl.DefaultAnnotationDataDef;
import org.knime.shared.workflow.def.impl.DefaultWorkflowDef;
import org.knime.shared.workflow.def.impl.WorkflowDefBuilder;
import org.knime.shared.workflow.storage.clipboard.DefClipboardContent.Version;
import org.knime.shared.workflow.storage.clipboard.SystemClipboardFormat.ObfuscatorException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class SystemClipboardFormatTest {

    /**
     * Test method for
     * {@link org.knime.shared.workflow.storage.clipboard.SystemClipboardFormat#serialize(org.knime.shared.workflow.storage.clipboard.DefClipboardContent)}
     * and {@link org.knime.shared.workflow.storage.clipboard.SystemClipboardFormat#deserialize(java.lang.String)}.
     */
    @Test
    void testSerialize() throws JsonProcessingException, ObfuscatorException, IllegalArgumentException,
        InvalidDefClipboardContentVersionException {
        final UUID payloadIdentifier = UUID.randomUUID();
        final DefaultAnnotationDataDef annotation = new AnnotationDataDefBuilder().setText("🤑㋉").build();
        final DefaultWorkflowDef workflowDef = new WorkflowDefBuilder().putToAnnotations("1", annotation).build();
        var original = new DefClipboardContent(Version.latest(), payloadIdentifier, workflowDef);

        var serialized = SystemClipboardFormat.serialize(original);
        var deserialized = SystemClipboardFormat.deserialize(serialized);

        assertThat(deserialized.getPayloadIdentifier())
            .as("Serialize-Deserialize of workflow content changed the payload identifier.")
            .isEqualTo(original.getPayloadIdentifier());
        assertThat(deserialized.getVersion()).as("Serialize-Deserialize of workflow content changed the version.")
            .isEqualTo(original.getVersion());
        assertThat(deserialized.getPayload()).as("Serialize-Deserialize of workflow content changed the payload.")
            .isEqualTo(original.getPayload());
    }

    @Test
    void testSerializeNull() {
        assertThatExceptionOfType(ObfuscatorException.class).isThrownBy(() -> SystemClipboardFormat.serialize(null));
    }

    @Test
    void testDeserializeFail()
        throws IllegalArgumentException, InvalidDefClipboardContentVersionException, ObfuscatorException {

        // cannot deserialize null
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> SystemClipboardFormat.deserialize(null));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> SystemClipboardFormat.deserialize("garbage"));
        assertThatExceptionOfType(ObfuscatorException.class)
            .isThrownBy(() -> SystemClipboardFormat.deserialize("01garbage"));
    }

}
