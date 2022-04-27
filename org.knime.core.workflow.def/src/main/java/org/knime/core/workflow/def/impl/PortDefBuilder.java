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

import org.knime.core.workflow.def.PortTypeDef;

// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.PortDef;
// for types that define enums
import org.knime.core.workflow.def.PortDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * PortDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class PortDefBuilder {

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
    Map<PortDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(PortDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Integer m_index;
    

    PortTypeDef m_portType;
    
    String m_name;
    

    /**
     * Create a new builder.
     */
    public PortDefBuilder() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for index
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param index the offset of the port relative to its siblings
     * @return this builder for fluent API.
     */ 
    public PortDefBuilder setIndex(final Integer index) {
        setIndex(() -> index, index);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortDef.Attribute.INDEX)} will return true and and
     * {@code getExceptionalChildren().get(PortDef.Attribute.INDEX)} will return the exception.
     * 
     * @param index see {@link PortDef#getIndex}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setIndex(Integer)
     */
    public PortDefBuilder setIndex(final FallibleSupplier<Integer> index, Integer defaultValue) {
        java.util.Objects.requireNonNull(index, () -> "No supplier for index provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortDef.Attribute.INDEX);
        try {
            m_index = index.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_index = defaultValue;
            m_exceptionalChildren.put(PortDef.Attribute.INDEX, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for portType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param portType 
     * @return this builder for fluent API.
     */ 
    public PortDefBuilder setPortType(final PortTypeDef portType) {
        setPortType(() -> portType, portType);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortDef.Attribute.PORT_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(PortDef.Attribute.PORT_TYPE)} will return the exception.
     * 
     * @param portType see {@link PortDef#getPortType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setPortType(PortTypeDef)
     */
    public PortDefBuilder setPortType(final FallibleSupplier<PortTypeDef> portType, PortTypeDef defaultValue) {
        java.util.Objects.requireNonNull(portType, () -> "No supplier for portType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortDef.Attribute.PORT_TYPE);
        try {
            m_portType = portType.get();
            if (m_portType instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_portType).hasExceptions()) {
                m_exceptionalChildren.put(PortDef.Attribute.PORT_TYPE, (LoadExceptionTree<?>)m_portType);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FalliblePortTypeDef){
                var childTree = ((FalliblePortTypeDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_portType = defaultValue;
            m_exceptionalChildren.put(PortDef.Attribute.PORT_TYPE, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for name
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param name 
     * @return this builder for fluent API.
     */ 
    public PortDefBuilder setName(final String name) {
        setName(() -> name, name);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortDef.Attribute.NAME)} will return true and and
     * {@code getExceptionalChildren().get(PortDef.Attribute.NAME)} will return the exception.
     * 
     * @param name see {@link PortDef#getName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setName(String)
     */
    public PortDefBuilder setName(final FallibleSupplier<String> name, String defaultValue) {
        java.util.Objects.requireNonNull(name, () -> "No supplier for name provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortDef.Attribute.NAME);
        try {
            m_name = name.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_name = defaultValue;
            m_exceptionalChildren.put(PortDef.Attribute.NAME, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link PortDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public FalliblePortDef build() {
        
    	
        return new FalliblePortDef(this);
    }    

}
