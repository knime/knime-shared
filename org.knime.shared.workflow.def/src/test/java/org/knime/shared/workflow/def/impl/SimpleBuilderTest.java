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
 *   9 May 2022 (jasper): created
 */
package org.knime.shared.workflow.def.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.knime.core.util.workflow.def.LoadException;

/**
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class SimpleBuilderTest {

    /**
     * Test that constructing an instance without setting the required fields will mark the instance as flawed.
     */
    @Test
    void testMissingValuesDeferredErrorCollection() {
        var def = new TemplateLinkDefBuilder().build();

        // has exceptions
        assertThat(def.getLoadExceptionTree().hasExceptions()).isTrue();

        // URI is required
        assertThat(def.getUriSupplyException()).isPresent();
        // update time is also required
        assertThat(def.getVersionSupplyException()).isPresent();
    }

    /**
     * Test that constructing an instance without setting the required fields will throw an exception in strict mode.
     */
    @Test
    void testMissingValuesStrict() {
        // when using fail-fast mode, the builder will not allow building without providing the required fields.
        assertThatThrownBy(() -> {
            new TemplateMetadataDefBuilder().strict().build();
        }).isInstanceOf(IllegalStateException.class).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test that a supply exception on a required field is reported.
     */
    @Test
    void supplyException() {
        var def = new StyleRangeDefBuilder()//
            .setStart(() -> {
                throw new IOException("Something went wrong.");
            }, -1).build();
        assertThat(def.getStartSupplyException().get().getCause()).hasMessage("Something went wrong.");
    }

    /**
     * Test that a supply exception on an optional field is still reported.
     */
    @Test
    void supplyExceptionOnOptional() {
        var def = new ConfigValueStringArrayDefBuilder()//
            .setConfigType("ConfigValueStringArray")//
            .setArray(() -> {
                throw new IOException("Something went wrong.");
            }).build();
        assertThat(def.getArraySupplyException()).containsInstanceOf(LoadException.class);
        assertThat(def.getArraySupplyException().get().getCause()).hasMessage("Something went wrong.");
    }
}
