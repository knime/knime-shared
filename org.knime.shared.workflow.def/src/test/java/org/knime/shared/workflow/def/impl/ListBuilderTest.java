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
import java.util.List;

import org.junit.jupiter.api.Test;
import org.knime.shared.workflow.def.CoordinateDef;

/**
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class ListBuilderTest {

    private final DefaultCoordinateDef coordinate1 = new CoordinateDefBuilder().strict().setX(1).setY(2).build();

    private final DefaultCoordinateDef coordinate2 = new CoordinateDefBuilder().strict().setX(10).setY(20).build();

    /**
     * Test the bulk setter for lists.
     */
    @Test
    void testBulkWithNull() {
        assertThatThrownBy(() -> {
            new ConnectionUISettingsDefBuilder().strict()//
                .setBendPoints((List<CoordinateDef>)null)// not allowed on required field
                .build();
        }).isInstanceOf(IllegalStateException.class).hasCauseInstanceOf(IllegalArgumentException.class);

    }

    /**
     * Test the bulk setter for lists.
     */
    @Test
    void testBulkWithEmpty() {
        var def = new ConnectionUISettingsDefBuilder().strict()//
            .setBendPoints(List.of())//
            .build();

        assertThat(def.getBendPoints()).isEmpty();
    }

    /**
     * Test the bulk setters for lists.
     */
    @Test
    void testBulkWithActual() {
        var def = new ConnectionUISettingsDefBuilder().strict()//
            .setBendPoints(List.of(coordinate1, coordinate2))//
            .build();
        assertThat(def.getBendPoints()).containsExactly(coordinate1, coordinate2);

    }

    /**
     * Test the individual and bulk setters for lists.
     */
    @Test
    void testIndividualElement() {
        var def = new ConnectionUISettingsDefBuilder().strict()//
            .addToBendPoints(coordinate1)//
            .build();
        assertThat(def.getBendPoints()).containsExactly(coordinate1);
    }

    /**
     * Test the invdividual and bulk setters for lists.
     */
    @Test
    void testIndividualElements() {
        var def = new ConnectionUISettingsDefBuilder().strict()//
            .addToBendPoints(coordinate1)//
            .addToBendPoints(coordinate2)//
            .build();
        assertThat(def.getBendPoints()).containsExactly(coordinate1, coordinate2);
    }

    /**
     * Test the bulk setter for lists.
     */
    @Test
    void testBulkExceptional() {
        var def = new ConnectionUISettingsDefBuilder()//
            .setBendPoints(() -> {throw new IOException("Bulk setter");})//
            .build();
        assertThat(def.getBendPointsSupplyException()).isPresent();
        assertThat(def.getBendPoints().size()).isZero(); // stuff should not be null
    }

    /**
     * Test the individual and bulk setters for lists.
     */
    @Test
    void testIndividualElementSingleExceptional() {
        var def = new ConnectionUISettingsDefBuilder()//
            .addToBendPoints(() -> {
                throw new IOException("Bulk setter");
            }, coordinate1)//
            .build();
        assertThat(def.getBendPointsSupplyException()).isEmpty();
        assertThat(def.getBendPointsExceptionTree().get().getExceptionalChildren().size()).isOne();
        assertThat(def.getBendPoints()).containsExactly(coordinate1); // contains only the provided default value

    }

    /**
     * Test the individual and bulk setters for lists.
     */
    @Test
    void testIndividualElementsExceptional() {
        var def = new ConnectionUISettingsDefBuilder()//
            .addToBendPoints(coordinate1).addToBendPoints(() -> {
                throw new IOException("Bulk setter");
            }, coordinate2)//
            .addToBendPoints(null)//
            .build();
        assertThat(def.getBendPointsSupplyException()).isEmpty();
        assertThat(def.getBendPointsExceptionTree().get().getExceptionalChildren().size()).isOne();
        assertThat(def.getBendPoints()).containsExactly(coordinate1, coordinate2, null); // contains only the provided default value
    }

    /**
     * Test combining bulk and individual setters for lists.
     */
    @Test
    void testMixedExceptional() {
        var def = new ConnectionUISettingsDefBuilder()//
            .addToBendPoints(coordinate1)//
            .setBendPoints(() -> {throw new IOException("Bulk setter");})//
            .addToBendPoints(() -> {throw new IllegalArgumentException();}, coordinate2)//
            .build();
        assertThat(def.getBendPointsSupplyException()).isPresent();
        assertThat(def.getBendPointsExceptionTree().get().getExceptionalChildren().size()).isOne();
        assertThat(def.getBendPoints()).containsExactly(coordinate1, coordinate2);
    }

}
