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
import org.knime.shared.workflow.def.impl.ConfigDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.ConfigMapDef;
// for types that define enums
import org.knime.shared.workflow.def.ConfigMapDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * ConfigMapDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class ConfigMapDefBuilder {

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
    public ConfigMapDefBuilder strict(){
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
    Map<ConfigMapDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ConfigMapDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_configType;
    

    String m_key;
    

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this map.
     */
    java.util.Map<String, ConfigDef> m_children = new java.util.HashMap<>();
    /** Temporarily holds onto elements set as a whole with setChildren these are added to m_children in build */
    private java.util.Map<String, ConfigDef> m_childrenBulkElements = new java.util.HashMap<>();
    /** This exception is merged with the exceptions of the elements of this map into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_childrenContainerSupplyException; 
    
    /**
     * Create a new builder.
     */
    public ConfigMapDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ConfigMapDefBuilder(final ConfigMapDef toCopy) {
        m_configType = toCopy.getConfigType();
        m_key = toCopy.getKey();
        m_children = toCopy.getChildren();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for configType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param configType Discriminator for inheritance. Must be the base name of this type/schema. 
     * @return this builder for fluent API.
     */ 
    public ConfigMapDefBuilder setConfigType(final String configType) {
        setConfigType(() -> configType, configType);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConfigMapDef.Attribute.CONFIG_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(ConfigMapDef.Attribute.CONFIG_TYPE)} will return the exception.
     * 
     * @param configType see {@link ConfigMapDef#getConfigType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setConfigType(String)
     */
    public ConfigMapDefBuilder setConfigType(final FallibleSupplier<String> configType, String defaultValue) {
        java.util.Objects.requireNonNull(configType, () -> "No supplier for configType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConfigMapDef.Attribute.CONFIG_TYPE);
        try {
            m_configType = configType.get();

            if(m_configType == null) {
                throw new IllegalArgumentException("configType is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_configType = defaultValue;
            m_exceptionalChildren.put(ConfigMapDef.Attribute.CONFIG_TYPE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for key
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param key  
     * @return this builder for fluent API.
     */ 
    public ConfigMapDefBuilder setKey(final String key) {
        setKey(() -> key, key);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConfigMapDef.Attribute.KEY)} will return true and and
     * {@code getExceptionalChildren().get(ConfigMapDef.Attribute.KEY)} will return the exception.
     * 
     * @param key see {@link ConfigMapDef#getKey}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setKey(String)
     */
    public ConfigMapDefBuilder setKey(final FallibleSupplier<String> key, String defaultValue) {
        java.util.Objects.requireNonNull(key, () -> "No supplier for key provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConfigMapDef.Attribute.KEY);
        try {
            m_key = key.get();

            if(m_key == null) {
                throw new IllegalArgumentException("key is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_key = defaultValue;
            m_exceptionalChildren.put(ConfigMapDef.Attribute.KEY, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for children
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the children map. 
     * Calling this method again will undo the previous call (it is not additive).
     * A mapping previously or subsequently set with {@link #putToChildren} will replace a mapping added with this method
     * if they have the same key.
     * @param children 
     * @return this for fluent API
     */
    public ConfigMapDefBuilder setChildren(final java.util.Map<String, ConfigDef> children) {
        setChildren(() -> children);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the children map,
     * whereas exceptions thrown in putTo allows to register a {@link LoadException} 
     * for an individual element of the children map). 
     * {@code hasExceptions(ConfigMapDef.Attribute.CHILDREN)} will return true and and
     * {@code getExceptionalChildren().get(ConfigMapDef.Attribute.CHILDREN)} will return the exception.
     * 
     * @param children see {@link ConfigMapDef#getChildren}
     * 
     * @return this builder for fluent API.
     * @see #setChildren(java.util.Map<String, ConfigDef>)
     */
    public ConfigMapDefBuilder setChildren(final FallibleSupplier<java.util.Map<String, ConfigDef>> children) {
        java.util.Objects.requireNonNull(children, () -> "No supplier for children provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConfigMapDef.Attribute.CHILDREN);
        try {
            m_childrenBulkElements = children.get();

            if(m_children == null) {
                throw new IllegalArgumentException("children is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_childrenBulkElements = java.util.Map.of();
            // merged together with map element exceptions into a single LoadExceptionTree in #build()
            m_childrenContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param key the key of the entry to add to the children map
     * @param value the value of the entry to add to the children map
     * @return this builder for fluent API
     */
    public ConfigMapDefBuilder putToChildren(String key, ConfigDef value){
    	putToChildren(key, () -> value, (ConfigDef)null);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the map returned by {@link ConfigMapDef#getChildren}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the given key.
     *
     * @param key the key for the entry added value in the map returned by {@link ConfigMapDef#getChildren}
     * @param value the value of the entry to add to the children map
     * @param defaultValue is added to the map as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ConfigMapDefBuilder putToChildren(String key, FallibleSupplier<ConfigDef> value, ConfigDef defaultValue) {
        ConfigDef toPut = null;
        try {
            toPut = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            // copies values to a new def (of the appropriate subtype, if any) and adds the load exception
            toPut = DefaultConfigDef.withException(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_children.put(key, toPut);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link ConfigMapDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultConfigMapDef build() {
        
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_configType == null) setConfigType( null);
        
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_key == null) setKey( null);
        
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_children == null) setChildren(() ->  null);
        
    	
        // contains the elements set with #setChildren (those added with #addToChildren have already been inserted into m_children)
        m_childrenBulkElements = java.util.Objects.requireNonNullElse(m_childrenBulkElements, java.util.Map.of());
        final java.util.Map<String, ConfigDef> childrenMerged = new java.util.HashMap<>();
        // in rough analogy to list containers, the bulk elements go first and then the individual elements are added
        childrenMerged.putAll(m_childrenBulkElements);
        childrenMerged.putAll(m_children);
        m_children = childrenMerged;
                
        var childrenLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .map(m_children, m_childrenContainerSupplyException);
        if(childrenLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ConfigMapDef.Attribute.CHILDREN, childrenLoadExceptionTree);
        }
        
        return new DefaultConfigMapDef(this);
    }    

}
