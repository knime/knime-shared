/*
 * ------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------
 */
package org.knime.shared.workflow.def.impl;

import java.util.Map;




// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.CoordinateDef;
// for types that define enums
import org.knime.shared.workflow.def.CoordinateDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * CoordinateDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class CoordinateDefBuilder {

    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree data
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Contains the load exception or load exception tree for each attribute with load exceptions. Def-type objects
     * provide both the data and the exceptions, so the instances referenced in this map are identical to the ones
     * returned by the getters. Non-Def-type objects (that includes lists and maps of Def types) do not provide the
     * {@link LoadException}s associated to their loading. Instead, separate {@link LoadExceptionTree} instances are
     * referenced in this map.
     */
    Map<CoordinateDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(CoordinateDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Integer m_x;
    

    Integer m_y;
    

    /**
     * Create a new builder.
     */
    public CoordinateDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public CoordinateDefBuilder(final CoordinateDef toCopy) {
        m_x = toCopy.getX();
        m_y = toCopy.getY();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for x
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param x Horizontal offset of an object.
     * @return this builder for fluent API.
     */ 
    public CoordinateDefBuilder setX(final Integer x) {
        setX(() -> x, x);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(CoordinateDef.Attribute.X)} will return true and and
     * {@code getExceptionalChildren().get(CoordinateDef.Attribute.X)} will return the exception.
     * 
     * @param x see {@link CoordinateDef#getX}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setX(Integer)
     */
    public CoordinateDefBuilder setX(final FallibleSupplier<Integer> x, Integer defaultValue) {
        java.util.Objects.requireNonNull(x, () -> "No supplier for x provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(CoordinateDef.Attribute.X);
        try {
            m_x = x.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_x = defaultValue;
            m_exceptionalChildren.put(CoordinateDef.Attribute.X, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for y
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param y Vertical offset of an object.
     * @return this builder for fluent API.
     */ 
    public CoordinateDefBuilder setY(final Integer y) {
        setY(() -> y, y);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(CoordinateDef.Attribute.Y)} will return true and and
     * {@code getExceptionalChildren().get(CoordinateDef.Attribute.Y)} will return the exception.
     * 
     * @param y see {@link CoordinateDef#getY}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setY(Integer)
     */
    public CoordinateDefBuilder setY(final FallibleSupplier<Integer> y, Integer defaultValue) {
        java.util.Objects.requireNonNull(y, () -> "No supplier for y provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(CoordinateDef.Attribute.Y);
        try {
            m_y = y.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_y = defaultValue;
            m_exceptionalChildren.put(CoordinateDef.Attribute.Y, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link CoordinateDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultCoordinateDef build() {
        
    	
        return new DefaultCoordinateDef(this);
    }    

}
