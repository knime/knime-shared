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
package org.knime.core.workflow.def.impl;

import java.util.Map;

import org.knime.core.workflow.def.BoundsDef;

// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.NodeUIInfoDef;
// for types that define enums
import org.knime.core.workflow.def.NodeUIInfoDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * NodeUIInfoDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class NodeUIInfoDefBuilder {

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
    Map<NodeUIInfoDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(NodeUIInfoDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Boolean m_hasAbsoluteCoordinates;
    

    Boolean m_symbolRelative;
    

    BoundsDef m_bounds;
    
    /**
     * Create a new builder.
     */
    public NodeUIInfoDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public NodeUIInfoDefBuilder(final NodeUIInfoDef toCopy) {
        m_hasAbsoluteCoordinates = toCopy.hasAbsoluteCoordinates();
        m_symbolRelative = toCopy.isSymbolRelative();
        m_bounds = toCopy.getBounds();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for hasAbsoluteCoordinates
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param hasAbsoluteCoordinates 
     * @return this builder for fluent API.
     */ 
    public NodeUIInfoDefBuilder setHasAbsoluteCoordinates(final Boolean hasAbsoluteCoordinates) {
        setHasAbsoluteCoordinates(() -> hasAbsoluteCoordinates, hasAbsoluteCoordinates);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NodeUIInfoDef.Attribute.HAS_ABSOLUTE_COORDINATES)} will return true and and
     * {@code getExceptionalChildren().get(NodeUIInfoDef.Attribute.HAS_ABSOLUTE_COORDINATES)} will return the exception.
     * 
     * @param hasAbsoluteCoordinates see {@link NodeUIInfoDef#hasAbsoluteCoordinates}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHasAbsoluteCoordinates(Boolean)
     */
    public NodeUIInfoDefBuilder setHasAbsoluteCoordinates(final FallibleSupplier<Boolean> hasAbsoluteCoordinates, Boolean defaultValue) {
        java.util.Objects.requireNonNull(hasAbsoluteCoordinates, () -> "No supplier for hasAbsoluteCoordinates provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NodeUIInfoDef.Attribute.HAS_ABSOLUTE_COORDINATES);
        try {
            m_hasAbsoluteCoordinates = hasAbsoluteCoordinates.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_hasAbsoluteCoordinates = defaultValue;
            m_exceptionalChildren.put(NodeUIInfoDef.Attribute.HAS_ABSOLUTE_COORDINATES, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for symbolRelative
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param symbolRelative 
     * @return this builder for fluent API.
     */ 
    public NodeUIInfoDefBuilder setSymbolRelative(final Boolean symbolRelative) {
        setSymbolRelative(() -> symbolRelative, symbolRelative);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NodeUIInfoDef.Attribute.SYMBOL_RELATIVE)} will return true and and
     * {@code getExceptionalChildren().get(NodeUIInfoDef.Attribute.SYMBOL_RELATIVE)} will return the exception.
     * 
     * @param symbolRelative see {@link NodeUIInfoDef#isSymbolRelative}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setSymbolRelative(Boolean)
     */
    public NodeUIInfoDefBuilder setSymbolRelative(final FallibleSupplier<Boolean> symbolRelative, Boolean defaultValue) {
        java.util.Objects.requireNonNull(symbolRelative, () -> "No supplier for symbolRelative provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NodeUIInfoDef.Attribute.SYMBOL_RELATIVE);
        try {
            m_symbolRelative = symbolRelative.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_symbolRelative = defaultValue;
            m_exceptionalChildren.put(NodeUIInfoDef.Attribute.SYMBOL_RELATIVE, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for bounds
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param bounds 
     * @return this builder for fluent API.
     */ 
    public NodeUIInfoDefBuilder setBounds(final BoundsDef bounds) {
        setBounds(() -> bounds, bounds);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NodeUIInfoDef.Attribute.BOUNDS)} will return true and and
     * {@code getExceptionalChildren().get(NodeUIInfoDef.Attribute.BOUNDS)} will return the exception.
     * 
     * @param bounds see {@link NodeUIInfoDef#getBounds}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBounds(BoundsDef)
     */
    public NodeUIInfoDefBuilder setBounds(final FallibleSupplier<BoundsDef> bounds, BoundsDef defaultValue) {
        java.util.Objects.requireNonNull(bounds, () -> "No supplier for bounds provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NodeUIInfoDef.Attribute.BOUNDS);
        try {
            m_bounds = bounds.get();
            if (m_bounds instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_bounds).hasExceptions()) {
                m_exceptionalChildren.put(NodeUIInfoDef.Attribute.BOUNDS, (LoadExceptionTree<?>)m_bounds);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleBoundsDef){
                var childTree = ((FallibleBoundsDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_bounds = defaultValue;
            m_exceptionalChildren.put(NodeUIInfoDef.Attribute.BOUNDS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link NodeUIInfoDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public FallibleNodeUIInfoDef build() {
        
    	
        return new FallibleNodeUIInfoDef(this);
    }    

}
