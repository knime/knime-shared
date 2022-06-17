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
import java.util.Optional;

import org.knime.shared.workflow.def.CoordinateDef;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.BoundsDef;
// for types that define enums
import org.knime.shared.workflow.def.BoundsDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * BoundsDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class BoundsDefBuilder {

    /**
     * @see #strict()
     */
    boolean m__failFast = false;

    /**
     * Enable fail-fast mode.
     * In fail-fast mode, all load exceptions will be immediately thrown.
     * This can be when invoking a setter with an illegal argument (e.g., null or out of range) or 
     * when invoking {@link #build()} without previously having called the setter for a required field.
     * By default, fail-fast mode is off and all exceptions will be caught instead of thrown and collected for later reference into a LoadExceptionTree.
     * @return this builder for fluent API.
     */
    public BoundsDefBuilder strict(){
        m__failFast = true;
        return this;
    }

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
    Map<BoundsDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(BoundsDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    CoordinateDef m_location;
    
    Integer m_width;
    

    Integer m_height;
    

    /**
     * Create a new builder.
     */
    public BoundsDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public BoundsDefBuilder(final BoundsDef toCopy) {
        setLocation(toCopy.getLocation());
        setWidth(toCopy.getWidth());
        setHeight(toCopy.getHeight());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for location
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param location  
     * @return this builder for fluent API.
     */ 
    public BoundsDefBuilder setLocation(final CoordinateDef location) {
        setLocation(() -> location, location);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(BoundsDef.Attribute.LOCATION)} will return true and and
     * {@code getExceptionalChildren().get(BoundsDef.Attribute.LOCATION)} will return the exception.
     * 
     * @param location see {@link BoundsDef#getLocation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocation(CoordinateDef)
     */
    public BoundsDefBuilder setLocation(final FallibleSupplier<CoordinateDef> location, CoordinateDef defaultValue) {
        java.util.Objects.requireNonNull(location, () -> "No supplier for location provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(BoundsDef.Attribute.LOCATION);
        try {
            var supplied = location.get();
            m_location = supplied;

            if(m_location == null) {
                throw new IllegalArgumentException("location is required and must not be null.");
            }
            if (m_location instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_location).hasExceptions()) {
                m_exceptionalChildren.put(BoundsDef.Attribute.LOCATION, (LoadExceptionTree<?>)m_location);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof LoadExceptionTreeProvider){
                var childTree = LoadExceptionTreeProvider.getTree(defaultValue);
                // if present, merge child tree with supply exception
                exceptionTree = childTree.hasExceptions() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree, supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_location = defaultValue;
            m_exceptionalChildren.put(BoundsDef.Attribute.LOCATION, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for width
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param width Width of an object in pixels. 
     * @return this builder for fluent API.
     */ 
    public BoundsDefBuilder setWidth(final Integer width) {
        setWidth(() -> width, width);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(BoundsDef.Attribute.WIDTH)} will return true and and
     * {@code getExceptionalChildren().get(BoundsDef.Attribute.WIDTH)} will return the exception.
     * 
     * @param width see {@link BoundsDef#getWidth}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWidth(Integer)
     */
    public BoundsDefBuilder setWidth(final FallibleSupplier<Integer> width, Integer defaultValue) {
        java.util.Objects.requireNonNull(width, () -> "No supplier for width provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(BoundsDef.Attribute.WIDTH);
        try {
            var supplied = width.get();
            m_width = supplied;

            if(m_width == null) {
                throw new IllegalArgumentException("width is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_width = defaultValue;
            m_exceptionalChildren.put(BoundsDef.Attribute.WIDTH, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for height
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param height Height of an object in pixels. 
     * @return this builder for fluent API.
     */ 
    public BoundsDefBuilder setHeight(final Integer height) {
        setHeight(() -> height, height);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(BoundsDef.Attribute.HEIGHT)} will return true and and
     * {@code getExceptionalChildren().get(BoundsDef.Attribute.HEIGHT)} will return the exception.
     * 
     * @param height see {@link BoundsDef#getHeight}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHeight(Integer)
     */
    public BoundsDefBuilder setHeight(final FallibleSupplier<Integer> height, Integer defaultValue) {
        java.util.Objects.requireNonNull(height, () -> "No supplier for height provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(BoundsDef.Attribute.HEIGHT);
        try {
            var supplied = height.get();
            m_height = supplied;

            if(m_height == null) {
                throw new IllegalArgumentException("height is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_height = defaultValue;
            m_exceptionalChildren.put(BoundsDef.Attribute.HEIGHT, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link BoundsDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultBoundsDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_location == null) setLocation( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_width == null) setWidth( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_height == null) setHeight( null);
        
    	
        return new DefaultBoundsDef(this);
    }    

}
