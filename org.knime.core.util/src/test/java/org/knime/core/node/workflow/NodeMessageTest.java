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
 *   Feb 2, 2017 (bjoern): created
 */
package org.knime.core.node.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.knime.core.node.workflow.NodeMessage.Type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Tests {@link NodeMessage}.
 *
 * @author Bjoern Lohrmann, KNIME.com GmbH
 */
class NodeMessageTest {

    /**
     * Tests the JSON (de)serialization of node messages, warning with all sorts of fields set.
     *
     * @throws IOException
     */
    @Test
    void testNodeMessageComplexWarning() throws IOException {
        ObjectMapper mapper = createObjectMapper();

        NodeMessage orig1 = new NodeMessage(Type.WARNING, "a warning text", "some issue",
            Arrays.asList("resolution 1", "resolution 2"));

        String serialized1 = mapper.writeValueAsString(orig1);
        NodeMessage deserialized1 = mapper.readValue(serialized1, NodeMessage.class);
        assertThat(deserialized1) //
            .as("Message is equal").isEqualTo(orig1) //
            .as("Message is not the same").isNotSameAs(orig1) //
            .as("Has same hash code").hasSameHashCodeAs(orig1);
        assertThat(deserialized1.getMessage()).isEqualTo("a warning text");
        assertThat(deserialized1.getMessageType()).isEqualTo(Type.WARNING);
        assertThat(deserialized1.getIssue()).get().isEqualTo("some issue");
        assertThat(deserialized1.getResolutions()).containsExactly("resolution 1", "resolution 2");
    }

    /**
     * Tests {@link NodeMessage#toStringWithDetails()}
     */
    @Test
    void testToStringWithDetails() throws IOException {
        NodeMessage orig1 = new NodeMessage(Type.WARNING, "a warning text", "some issue",
            Arrays.asList("resolution 1", "resolution 2"));

        assertThat(orig1.toStringWithDetails()).as("String with details").isEqualTo("WARNING: a warning text\n" //
            + " some issue\n" //
            + "\n" //
            + "Possibly resolution(s)\n" //
            + "-resolution 1\n" //
            + "-resolution 2");
    }

    /**
     * Same, just with an error.
     *
     * @throws IOException
     */
    @Test
    void testNodeMessageError() throws IOException {
        ObjectMapper mapper = createObjectMapper();

        NodeMessage orig2 = NodeMessage.newError("an error text");
        assertThat(orig2.getIssue()).isNotPresent();
        assertThat(orig2.getResolutions()).isEmpty();

        String serialized2 = mapper.writeValueAsString(orig2);
        NodeMessage deserialized2 = mapper.readValue(serialized2, NodeMessage.class);
        assertThat(deserialized2) //
            .as("Message is equal").isEqualTo(orig2) //
            .as("Message is not the same").isNotSameAs(orig2);
        assertThat(deserialized2.getIssue()).isNotPresent();
        assertThat(deserialized2.getResolutions()).isEmpty();
    }

    /**
     * Tests {@link NodeMessage#merge(NodeMessage, NodeMessage)}
     *
     */
    @Test
    void testNodeMessageMerge() throws IOException {

        NodeMessage error = NodeMessage.newError("an error text");
        NodeMessage warning = NodeMessage.newWarning("a warning text");
        NodeMessage merge = NodeMessage.merge(error, warning);

        assertThat(merge).extracting(m -> m.getMessageType()).isEqualTo(Type.ERROR);
        assertThat(merge).extracting(m -> m.getMessage()).asString().contains("an error text")
            .contains("a warning text");
    }

    /**
     * Tests {@link NodeMessage#merge(NodeMessage, NodeMessage)}
     *
     */
    @Test
    void testNodeMessageMergeWithReset() throws IOException {

        NodeMessage error = NodeMessage.newError("an error text");
        NodeMessage warning = NodeMessage.newWarning("a warning text");
        NodeMessage reset = NodeMessage.NONE;
        NodeMessage mergeWarnError = NodeMessage.merge(error, warning);

        assertThat(mergeWarnError).extracting(m -> m.getMessageType()).isEqualTo(Type.ERROR);
        assertThat(mergeWarnError).extracting(m -> m.getMessage()).asString().contains("an error text")
            .contains("a warning text");

        NodeMessage mergeResetError = NodeMessage.merge(reset, error);
        assertThat(mergeResetError).as("merged with REST message").isSameAs(error);

        NodeMessage mergeWarnReset = NodeMessage.merge(warning, reset);
        assertThat(mergeWarnReset).as("merged with REST message").isSameAs(warning);
    }

    /**
     * Tests {@link NodeMessage#newConfigurationError(String)}
     */
    @Test
    void testConfigurationError() throws IOException {
        NodeMessage configError = NodeMessage.newConfigurationError("Invalid settings");

        assertThat(configError.getMessageType()).isEqualTo(Type.WARNING);
        assertThat(configError.isConfigurationError()).isTrue();
        assertThat(configError.getMessage()).isEqualTo("Invalid settings");
    }

    /**
     * Tests {@link NodeMessage#newConfigurationError(String, String, List)}
     */
    @Test
    void testConfigurationErrorWithDetails() throws IOException {
        NodeMessage configError = NodeMessage.newConfigurationError("Invalid settings", "Missing column",
            Arrays.asList("Add input column", "Change configuration"));

        assertThat(configError.getMessageType()).isEqualTo(Type.WARNING);
        assertThat(configError.isConfigurationError()).isTrue();
        assertThat(configError.getMessage()).isEqualTo("Invalid settings");
        assertThat(configError.getIssue()).get().isEqualTo("Missing column");
        assertThat(configError.getResolutions()).containsExactly("Add input column", "Change configuration");
    }

    /**
     * Tests that regular warnings and errors are not marked as configuration errors
     */
    @Test
    void testRegularMessagesNotConfigurationErrors() {
        NodeMessage warning = NodeMessage.newWarning("a warning");
        NodeMessage error = NodeMessage.newError("an error");

        assertThat(warning.isConfigurationError()).isFalse();
        assertThat(error.isConfigurationError()).isFalse();
    }

    /**
     * Tests JSON serialization/deserialization of configuration error flag
     */
    @Test
    void testConfigurationErrorSerialization() throws IOException {
        ObjectMapper mapper = createObjectMapper();

        NodeMessage configError = NodeMessage.newConfigurationError("Config error");
        String serialized = mapper.writeValueAsString(configError);
        NodeMessage deserialized = mapper.readValue(serialized, NodeMessage.class);

        assertThat(deserialized).isEqualTo(configError);
        assertThat(deserialized.isConfigurationError()).isTrue();
        assertThat(deserialized.getMessageType()).isEqualTo(Type.WARNING);
    }

    /**
     * Tests that merge preserves configuration error flag
     */
    @Test
    void testMergePreservesConfigurationError() {
        NodeMessage configError = NodeMessage.newConfigurationError("Config error");
        NodeMessage warning = NodeMessage.newWarning("Regular warning");

        NodeMessage merged1 = NodeMessage.merge(configError, warning);
        assertThat(merged1.isConfigurationError()).isTrue();

        NodeMessage merged2 = NodeMessage.merge(warning, configError);
        assertThat(merged2.isConfigurationError()).isTrue();
    }

    /**
     * Tests that merging two regular messages doesn't result in a configuration error
     */
    @Test
    void testMergeWithoutConfigurationError() {
        NodeMessage warning1 = NodeMessage.newWarning("Warning 1");
        NodeMessage warning2 = NodeMessage.newWarning("Warning 2");

        NodeMessage merged = NodeMessage.merge(warning1, warning2);
        assertThat(merged.isConfigurationError()).isFalse();
    }

    /**
     * Tests equals/hashCode with configuration error flag
     */
    @Test
    void testEqualsHashCodeWithConfigurationError() {
        NodeMessage msg1 = NodeMessage.newConfigurationError("Config error");
        NodeMessage msg2 = NodeMessage.newConfigurationError("Config error");
        NodeMessage msg3 = NodeMessage.newWarning("Config error"); // Same text but not a config error

        assertThat(msg1).isEqualTo(msg2);
        assertThat(msg1.hashCode()).isEqualTo(msg2.hashCode());
        assertThat(msg1).isNotEqualTo(msg3);
        assertThat(msg1.hashCode()).isNotEqualTo(msg3.hashCode());
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper().registerModule(new Jdk8Module()); // new in 5.0 due to AP-19914
    }

}
