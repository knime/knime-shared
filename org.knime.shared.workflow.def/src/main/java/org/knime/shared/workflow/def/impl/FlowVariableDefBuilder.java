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

import org.knime.shared.workflow.def.ConfigDef;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.FlowVariableDef;
// for types that define enums
import org.knime.shared.workflow.def.FlowVariableDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * A value that is propagated along the connections between nodes.  Nodes can use incoming flow variables to control their behavior and export them to control downstream nodes or to add data to all outgoing port objects.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class FlowVariableDefBuilder {

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
    public FlowVariableDefBuilder strict(){
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
    Map<FlowVariableDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(FlowVariableDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_name;
    

    String m_propertyClass;
    

    ConfigDef m_value;
    
    /**
     * Create a new builder.
     */
    public FlowVariableDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public FlowVariableDefBuilder(final FlowVariableDef toCopy) {
        m_name = toCopy.getName();
        m_propertyClass = toCopy.getPropertyClass();
        m_value = toCopy.getValue();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for name
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param name Identifier for the flow variable. 
     * @return this builder for fluent API.
     */ 
    public FlowVariableDefBuilder setName(final String name) {
        setName(() -> name, name);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(FlowVariableDef.Attribute.NAME)} will return true and and
     * {@code getExceptionalChildren().get(FlowVariableDef.Attribute.NAME)} will return the exception.
     * 
     * @param name see {@link FlowVariableDef#getName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setName(String)
     */
    public FlowVariableDefBuilder setName(final FallibleSupplier<String> name, String defaultValue) {
        java.util.Objects.requireNonNull(name, () -> "No supplier for name provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(FlowVariableDef.Attribute.NAME);
        try {
            var supplied = name.get();
            m_name = supplied;

            if(m_name == null) {
                throw new IllegalArgumentException("name is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_name = defaultValue;
            m_exceptionalChildren.put(FlowVariableDef.Attribute.NAME, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for propertyClass
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param propertyClass If value is a ConfigValue (a simple type): the string representation of the Config ConfigTypeEnum enum value (e.g., INTEGER). If value is a ConfigMap (a custom/complex type): the qualified name of the java class used to instantiate the value (e.g., org.knime.filehandling.core.connections.FSLocationSpec) 
     * @return this builder for fluent API.
     */ 
    public FlowVariableDefBuilder setPropertyClass(final String propertyClass) {
        setPropertyClass(() -> propertyClass, propertyClass);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(FlowVariableDef.Attribute.CLASS)} will return true and and
     * {@code getExceptionalChildren().get(FlowVariableDef.Attribute.CLASS)} will return the exception.
     * 
     * @param propertyClass see {@link FlowVariableDef#getPropertyClass}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setPropertyClass(String)
     */
    public FlowVariableDefBuilder setPropertyClass(final FallibleSupplier<String> propertyClass, String defaultValue) {
        java.util.Objects.requireNonNull(propertyClass, () -> "No supplier for propertyClass provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(FlowVariableDef.Attribute.CLASS);
        try {
            var supplied = propertyClass.get();
            m_propertyClass = supplied;

            if(m_propertyClass == null) {
                throw new IllegalArgumentException("propertyClass is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_propertyClass = defaultValue;
            m_exceptionalChildren.put(FlowVariableDef.Attribute.CLASS, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for value
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param value  
     * @return this builder for fluent API.
     */ 
    public FlowVariableDefBuilder setValue(final ConfigDef value) {
        setValue(() -> value, value);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(FlowVariableDef.Attribute.VALUE)} will return true and and
     * {@code getExceptionalChildren().get(FlowVariableDef.Attribute.VALUE)} will return the exception.
     * 
     * @param value see {@link FlowVariableDef#getValue}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setValue(ConfigDef)
     */
    public FlowVariableDefBuilder setValue(final FallibleSupplier<ConfigDef> value, ConfigDef defaultValue) {
        java.util.Objects.requireNonNull(value, () -> "No supplier for value provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(FlowVariableDef.Attribute.VALUE);
        try {
            var supplied = value.get();
            m_value = supplied;

            if(m_value == null) {
                throw new IllegalArgumentException("value is required and must not be null.");
            }
            if (m_value instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_value).hasExceptions()) {
                m_exceptionalChildren.put(FlowVariableDef.Attribute.VALUE, (LoadExceptionTree<?>)m_value);
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
            m_value = defaultValue;
            m_exceptionalChildren.put(FlowVariableDef.Attribute.VALUE, exceptionTree);
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
	 * @return the {@link FlowVariableDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultFlowVariableDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_name == null) setName( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_propertyClass == null) setPropertyClass( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_value == null) setValue( null);
        
    	
        return new DefaultFlowVariableDef(this);
    }    

}
