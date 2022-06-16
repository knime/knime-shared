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

import org.knime.shared.workflow.def.impl.ConfigValueArrayDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.ConfigValueIntArrayDef;
// for types that define enums
import org.knime.shared.workflow.def.ConfigValueIntArrayDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * ConfigValueIntArrayDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class ConfigValueIntArrayDefBuilder {

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
    public ConfigValueIntArrayDefBuilder strict(){
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
    Map<ConfigValueIntArrayDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ConfigValueIntArrayDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_configType;
    

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    Optional<java.util.List<Integer>> m_array = Optional.of(new java.util.ArrayList<>());
    /** Temporarily holds onto elements set as a whole with setArray these are added to m_array in build */
    private Optional<java.util.List<Integer>> m_arrayBulkElements = Optional.of(new java.util.ArrayList<>());
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_arrayContainerSupplyException; 
    
    /** Associates an offset in {@link #m_array} to the supply exception that caused its addition to the list (i.e., it's a forced default value). Merged during {@link #build()} with {@link m_arrayContainerSupplyException} into a single {@link LoadExceptionTree} */
    private java.util.Map<Integer, LoadException> m_arrayElementSupplyExceptions = new java.util.HashMap<>();

    /**
     * Create a new builder.
     */
    public ConfigValueIntArrayDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ConfigValueIntArrayDefBuilder(final ConfigValueIntArrayDef toCopy) {
        m_configType = toCopy.getConfigType();
        m_array = toCopy.getArray();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for configType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param configType Discriminator for inheritance. Must be the base name of this type/schema. 
     * @return this builder for fluent API.
     */ 
    public ConfigValueIntArrayDefBuilder setConfigType(final String configType) {
        setConfigType(() -> configType, configType);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConfigValueIntArrayDef.Attribute.CONFIG_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(ConfigValueIntArrayDef.Attribute.CONFIG_TYPE)} will return the exception.
     * 
     * @param configType see {@link ConfigValueIntArrayDef#getConfigType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setConfigType(String)
     */
    public ConfigValueIntArrayDefBuilder setConfigType(final FallibleSupplier<String> configType, String defaultValue) {
        java.util.Objects.requireNonNull(configType, () -> "No supplier for configType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConfigValueIntArrayDef.Attribute.CONFIG_TYPE);
        try {
            m_configType = configType.get();

            if(m_configType == null) {
                throw new IllegalArgumentException("configType is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_configType = defaultValue;
            m_exceptionalChildren.put(ConfigValueIntArrayDef.Attribute.CONFIG_TYPE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for array
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the array list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToArray} will be inserted at the end of the list.
     * @param array 
     * @return this for fluent API
     */
    public ConfigValueIntArrayDefBuilder setArray(final java.util.List<Integer> array) {
        setArray(() -> array);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the array list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the array list). 
     * {@code hasExceptions(ConfigValueIntArrayDef.Attribute.ARRAY)} will return true and and
     * {@code getExceptionalChildren().get(ConfigValueIntArrayDef.Attribute.ARRAY)} will return the exception.
     * 
     * @param array see {@link ConfigValueIntArrayDef#getArray}
     * 
     * @return this builder for fluent API.
     * @see #setArray(java.util.List<Integer>)
     */
    public ConfigValueIntArrayDefBuilder setArray(final FallibleSupplier<java.util.List<Integer>> array) {
        java.util.Objects.requireNonNull(array, () -> "No supplier for array provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConfigValueIntArrayDef.Attribute.ARRAY);
        try {
            m_arrayBulkElements = Optional.ofNullable(array.get());
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_arrayBulkElements = Optional.of(java.util.List.of());
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_arrayContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the array list
     * @return this builder for fluent API
     */
    public ConfigValueIntArrayDefBuilder addToArray(Integer value){
    	addToArray(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ConfigValueIntArrayDef#getArray}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the array list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ConfigValueIntArrayDefBuilder addToArray(FallibleSupplier<Integer> value, Integer defaultValue) {
        Integer toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            m_arrayElementSupplyExceptions.put(m_array.get().size(), supplyException);
            toAdd = defaultValue;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_array.get().add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link ConfigValueIntArrayDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultConfigValueIntArrayDef build() {
        
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_configType == null) setConfigType( null);
        
    	
        // contains the elements set with #setArray (those added with #addToArray have already been inserted into m_array)
        m_arrayBulkElements = java.util.Objects.requireNonNullElse(m_arrayBulkElements, Optional.of(java.util.List.of()));
        m_array.get().addAll(0, m_arrayBulkElements.get());
        
        var arrayLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_arrayElementSupplyExceptions, m_arrayContainerSupplyException);
                if(arrayLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ConfigValueIntArrayDef.Attribute.ARRAY, arrayLoadExceptionTree);
        }
        
        return new DefaultConfigValueIntArrayDef(this);
    }    

}
