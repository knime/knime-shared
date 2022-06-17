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

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.knime.shared.workflow.def.AnnotationDataDef;

/**
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class MapBuilderTest {
    private final DefaultCoordinateDef loc = new CoordinateDefBuilder().strict().setX(1).setY(2).build();

    final AnnotationDataDefBuilder builder =
        new AnnotationDataDefBuilder().strict().setLocation(loc).setWidth(100).setHeight(20);

    private final AnnotationDataDef anno1 = builder.setText("anno1").build();

    private final AnnotationDataDef anno2 = builder.setText("anno2").build();

    final Map<String, AnnotationDataDef> map = Map.of("id1", anno1, "id2", anno2);

    /**
     * Test the bulk setter for lists.
     */
    @Test
    void testBulkWithNull() {
        var def = new WorkflowDefBuilder().strict()//
            .setAnnotations((Map)null)//
            .build();
        assertThat(def.getAnnotations()).isEmpty();
    }

    /**
     * Test the bulk setter for lists.
     */
    @Test
    void testBulkWithEmpty() {
        var def = new WorkflowDefBuilder().strict()//
            .setAnnotations(Map.of())//
            .build();

        assertThat(def.getAnnotations().get().size()).isZero(); // NOSONAR
    }

    /**
     * Test the bulk setters for lists.
     */
    @Test
    void testBulkWithActual() {
        var def = new WorkflowDefBuilder().strict()//
            .setAnnotations(map)//
            .build();
        assertThat(def.getAnnotations()).contains(map);
    }

    /**
     * Test the individual and bulk setters for lists.
     */
    @Test
    void testIndividualElement() {
        var def = new WorkflowDefBuilder().strict()//
            .putToAnnotations("id1", anno1)//
            .build();
        assertThat(def.getAnnotations().get()).containsOnly(Map.entry("id1", anno1));
    }

    /**
     * Test the invdividual and bulk setters for lists.
     */
    @Test
    void testIndividualElements() {
        var def = new WorkflowDefBuilder().strict()//
            .putToAnnotations("id1",anno1)//
            .putToAnnotations("id2",anno2)//
            .build();
        assertThat(def.getAnnotations()).contains(map);
    }

    /**
     * Test the bulk setter for lists.
     */
    @Test
    void testBulkExceptional() {
        var def = new WorkflowDefBuilder()//
            .setAnnotations(() -> {throw new IOException("Bulk setter");})//
            .build();
        assertThat(def.getAnnotationsSupplyException()).isPresent();
        assertThat(def.getAnnotations()).isEmpty();
    }

    /**
     * Test the individual and bulk setters for lists.
     */
    @Test
    void testIndividualElementSingleExceptional() {
        var def = new WorkflowDefBuilder()//
            .putToAnnotations("id1",() -> {
                throw new IOException("Bulk setter");
            }, anno1)//
            .build();
        assertThat(def.getAnnotationsSupplyException()).isEmpty();
        assertThat(def.getAnnotationsExceptionTree().get().getExceptionalChildren().size()).isOne();
        assertThat(def.getAnnotations().get()).containsOnly(Map.entry("id1", anno1));
    }

    /**
     * Test the individual and bulk setters for lists.
     */
    @Test
    void testIndividualElementsExceptional() {
        var def = new WorkflowDefBuilder()//
            .putToAnnotations("id1", anno1)//
            .putToAnnotations("id2", () -> {
                throw new IOException("Bulk setter");
            }, anno2)//
            .build();
        assertThat(def.getAnnotationsSupplyException()).isEmpty();
        assertThat(def.getAnnotationsExceptionTree().get().getExceptionalChildren().size()).isOne();
        assertThat(def.getAnnotations().get()).containsOnly(//
            Map.entry("id1", anno1), //
            Map.entry("id2", anno2));
    }

    /**
     * Test combining bulk and individual setters for lists.
     */
    @Test
    void testMixedExceptional() {
        var def = new WorkflowDefBuilder()//
            .putToAnnotations("id1",anno1)//
            .setAnnotations(() -> {throw new IOException("Bulk setter");})//
            .putToAnnotations("id2",() -> {throw new IllegalArgumentException();}, anno2)//
            .build();
        assertThat(def.getAnnotationsSupplyException()).isPresent();
        assertThat(def.getAnnotationsExceptionTree().get().getExceptionalChildren().size()).isOne();
        assertThat(def.getAnnotations()).contains(map);
    }

}
