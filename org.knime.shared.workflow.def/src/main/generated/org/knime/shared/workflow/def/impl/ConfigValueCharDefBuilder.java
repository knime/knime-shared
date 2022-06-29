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

import org.knime.shared.workflow.def.impl.ConfigValueDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.ConfigValueCharDef;
// for types that define enums
import org.knime.shared.workflow.def.ConfigValueCharDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.shared.workflow.def.FallibleSupplier;
import org.knime.shared.workflow.def.LoadException;
import org.knime.shared.workflow.def.LoadExceptionTree;
import org.knime.shared.workflow.def.LoadExceptionTreeProvider;

/**
 * ConfigValueCharDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class ConfigValueCharDefBuilder {

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
    public ConfigValueCharDefBuilder strict(){
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
    Map<ConfigValueCharDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ConfigValueCharDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_configType;
    

    Integer m_value;
    

    /**
     * Create a new builder.
     */
    public ConfigValueCharDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ConfigValueCharDefBuilder(final ConfigValueCharDef toCopy) {
        setConfigType(toCopy.getConfigType());
        setValue(toCopy.getValue());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for configType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param configType Discriminator for inheritance. Must be the base name of this type/schema. 
     * @return this builder for fluent API.
     */ 
    public ConfigValueCharDefBuilder setConfigType(final String configType) {
        setConfigType(() -> configType, configType);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConfigValueCharDef.Attribute.CONFIG_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(ConfigValueCharDef.Attribute.CONFIG_TYPE)} will return the exception.
     * 
     * @param configType see {@link ConfigValueCharDef#getConfigType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setConfigType(String)
     */
    public ConfigValueCharDefBuilder setConfigType(final FallibleSupplier<String> configType, String defaultValue) {
        java.util.Objects.requireNonNull(configType, () -> "No supplier for configType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConfigValueCharDef.Attribute.CONFIG_TYPE);
        try {
            var supplied = configType.get();
            m_configType = supplied;

            if(m_configType == null) {
                throw new IllegalArgumentException("configType is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_configType = defaultValue;
            m_exceptionalChildren.put(ConfigValueCharDef.Attribute.CONFIG_TYPE, supplyException);
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
    public ConfigValueCharDefBuilder setValue(final Integer value) {
        setValue(() -> value, value);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConfigValueCharDef.Attribute.VALUE)} will return true and and
     * {@code getExceptionalChildren().get(ConfigValueCharDef.Attribute.VALUE)} will return the exception.
     * 
     * @param value see {@link ConfigValueCharDef#getValue}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setValue(Integer)
     */
    public ConfigValueCharDefBuilder setValue(final FallibleSupplier<Integer> value, Integer defaultValue) {
        java.util.Objects.requireNonNull(value, () -> "No supplier for value provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConfigValueCharDef.Attribute.VALUE);
        try {
            var supplied = value.get();
            m_value = supplied;
            if(m_value != null && m_value < 0) {
                throw new IllegalArgumentException("value must not be smaller than 0, but was given: " + m_value);
            }
            if(m_value != null && m_value > 65535) {
                throw new IllegalArgumentException("value must not be larger than 65535, but was given: " + m_value);
            }
            
            if(m_value == null) {
                throw new IllegalArgumentException("value is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_value = defaultValue;
            m_exceptionalChildren.put(ConfigValueCharDef.Attribute.VALUE, supplyException);
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
	 * @return the {@link ConfigValueCharDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultConfigValueCharDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_configType == null) setConfigType( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_value == null) setValue( null);
        
    	
        return new DefaultConfigValueCharDef(this);
    }    

}
