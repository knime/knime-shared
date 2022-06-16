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

import org.knime.shared.workflow.def.PortMetadataDef;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.ComponentMetadataDef;
// for types that define enums
import org.knime.shared.workflow.def.ComponentMetadataDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * ComponentMetadataDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class ComponentMetadataDefBuilder {

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
    public ComponentMetadataDefBuilder strict(){
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
    Map<ComponentMetadataDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ComponentMetadataDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Optional<String> m_description = Optional.empty();
    

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    Optional<java.util.List<PortMetadataDef>> m_inPortMetadata = Optional.of(new java.util.ArrayList<>());
    /** Temporarily holds onto elements set as a whole with setInPortMetadata these are added to m_inPortMetadata in build */
    private Optional<java.util.List<PortMetadataDef>> m_inPortMetadataBulkElements = Optional.of(new java.util.ArrayList<>());
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_inPortMetadataContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    Optional<java.util.List<PortMetadataDef>> m_outPortMetadata = Optional.of(new java.util.ArrayList<>());
    /** Temporarily holds onto elements set as a whole with setOutPortMetadata these are added to m_outPortMetadata in build */
    private Optional<java.util.List<PortMetadataDef>> m_outPortMetadataBulkElements = Optional.of(new java.util.ArrayList<>());
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_outPortMetadataContainerSupplyException; 
    
    Optional<byte[]> m_icon = Optional.empty();
    

    Optional<ComponentTypeEnum> m_componentType = Optional.empty();
    

    /**
     * Create a new builder.
     */
    public ComponentMetadataDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ComponentMetadataDefBuilder(final ComponentMetadataDef toCopy) {
        m_description = toCopy.getDescription();
        m_inPortMetadata = toCopy.getInPortMetadata();
        m_outPortMetadata = toCopy.getOutPortMetadata();
        m_icon = toCopy.getIcon();
        m_componentType = toCopy.getComponentType();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for description
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param description  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentMetadataDefBuilder setDescription(final String description) {
        setDescription(() -> description, description);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentMetadataDef.Attribute.DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.DESCRIPTION)} will return the exception.
     * 
     * @param description see {@link ComponentMetadataDef#getDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDescription(String)
     */
    public ComponentMetadataDefBuilder setDescription(final FallibleSupplier<String> description) {
        setDescription(description, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentMetadataDef.Attribute.DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.DESCRIPTION)} will return the exception.
     * 
     * @param description see {@link ComponentMetadataDef#getDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDescription(String)
     */
    public ComponentMetadataDefBuilder setDescription(final FallibleSupplier<String> description, String defaultValue) {
        java.util.Objects.requireNonNull(description, () -> "No supplier for description provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.DESCRIPTION);
        try {
            m_description = Optional.ofNullable(description.get());
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_description = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.DESCRIPTION, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for inPortMetadata
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the inPortMetadata list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToInPortMetadata} will be inserted at the end of the list.
     * @param inPortMetadata 
     * @return this for fluent API
     */
    public ComponentMetadataDefBuilder setInPortMetadata(final java.util.List<PortMetadataDef> inPortMetadata) {
        setInPortMetadata(() -> inPortMetadata);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the inPortMetadata list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the inPortMetadata list). 
     * {@code hasExceptions(ComponentMetadataDef.Attribute.IN_PORT_METADATA)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.IN_PORT_METADATA)} will return the exception.
     * 
     * @param inPortMetadata see {@link ComponentMetadataDef#getInPortMetadata}
     * 
     * @return this builder for fluent API.
     * @see #setInPortMetadata(java.util.List<PortMetadataDef>)
     */
    public ComponentMetadataDefBuilder setInPortMetadata(final FallibleSupplier<java.util.List<PortMetadataDef>> inPortMetadata) {
        java.util.Objects.requireNonNull(inPortMetadata, () -> "No supplier for inPortMetadata provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.IN_PORT_METADATA);
        try {
            m_inPortMetadataBulkElements = Optional.ofNullable(inPortMetadata.get());
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_inPortMetadataBulkElements = Optional.of(java.util.List.of());
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_inPortMetadataContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the inPortMetadata list
     * @return this builder for fluent API
     */
    public ComponentMetadataDefBuilder addToInPortMetadata(PortMetadataDef value){
    	addToInPortMetadata(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentMetadataDef#getInPortMetadata}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the inPortMetadata list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentMetadataDefBuilder addToInPortMetadata(FallibleSupplier<PortMetadataDef> value, PortMetadataDef defaultValue) {
        PortMetadataDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultPortMetadataDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_inPortMetadata.get().add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for outPortMetadata
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the outPortMetadata list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToOutPortMetadata} will be inserted at the end of the list.
     * @param outPortMetadata 
     * @return this for fluent API
     */
    public ComponentMetadataDefBuilder setOutPortMetadata(final java.util.List<PortMetadataDef> outPortMetadata) {
        setOutPortMetadata(() -> outPortMetadata);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the outPortMetadata list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the outPortMetadata list). 
     * {@code hasExceptions(ComponentMetadataDef.Attribute.OUT_PORT_METADATA)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.OUT_PORT_METADATA)} will return the exception.
     * 
     * @param outPortMetadata see {@link ComponentMetadataDef#getOutPortMetadata}
     * 
     * @return this builder for fluent API.
     * @see #setOutPortMetadata(java.util.List<PortMetadataDef>)
     */
    public ComponentMetadataDefBuilder setOutPortMetadata(final FallibleSupplier<java.util.List<PortMetadataDef>> outPortMetadata) {
        java.util.Objects.requireNonNull(outPortMetadata, () -> "No supplier for outPortMetadata provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.OUT_PORT_METADATA);
        try {
            m_outPortMetadataBulkElements = Optional.ofNullable(outPortMetadata.get());
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_outPortMetadataBulkElements = Optional.of(java.util.List.of());
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_outPortMetadataContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the outPortMetadata list
     * @return this builder for fluent API
     */
    public ComponentMetadataDefBuilder addToOutPortMetadata(PortMetadataDef value){
    	addToOutPortMetadata(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentMetadataDef#getOutPortMetadata}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the outPortMetadata list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentMetadataDefBuilder addToOutPortMetadata(FallibleSupplier<PortMetadataDef> value, PortMetadataDef defaultValue) {
        PortMetadataDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultPortMetadataDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_outPortMetadata.get().add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for icon
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param icon  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentMetadataDefBuilder setIcon(final byte[] icon) {
        setIcon(() -> icon, icon);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentMetadataDef.Attribute.ICON)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.ICON)} will return the exception.
     * 
     * @param icon see {@link ComponentMetadataDef#getIcon}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setIcon(byte[])
     */
    public ComponentMetadataDefBuilder setIcon(final FallibleSupplier<byte[]> icon) {
        setIcon(icon, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentMetadataDef.Attribute.ICON)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.ICON)} will return the exception.
     * 
     * @param icon see {@link ComponentMetadataDef#getIcon}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setIcon(byte[])
     */
    public ComponentMetadataDefBuilder setIcon(final FallibleSupplier<byte[]> icon, byte[] defaultValue) {
        java.util.Objects.requireNonNull(icon, () -> "No supplier for icon provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.ICON);
        try {
            m_icon = Optional.ofNullable(icon.get());
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_icon = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.ICON, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for componentType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param componentType Summarizes the kind of functionality of the component. This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentMetadataDefBuilder setComponentType(final ComponentTypeEnum componentType) {
        setComponentType(() -> componentType, componentType);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentMetadataDef.Attribute.COMPONENT_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.COMPONENT_TYPE)} will return the exception.
     * 
     * @param componentType see {@link ComponentMetadataDef#getComponentType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setComponentType(ComponentTypeEnum)
     */
    public ComponentMetadataDefBuilder setComponentType(final FallibleSupplier<ComponentTypeEnum> componentType) {
        setComponentType(componentType, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentMetadataDef.Attribute.COMPONENT_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.COMPONENT_TYPE)} will return the exception.
     * 
     * @param componentType see {@link ComponentMetadataDef#getComponentType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setComponentType(ComponentTypeEnum)
     */
    public ComponentMetadataDefBuilder setComponentType(final FallibleSupplier<ComponentTypeEnum> componentType, ComponentTypeEnum defaultValue) {
        java.util.Objects.requireNonNull(componentType, () -> "No supplier for componentType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.COMPONENT_TYPE);
        try {
            m_componentType = Optional.ofNullable(componentType.get());
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_componentType = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.COMPONENT_TYPE, supplyException);
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
	 * @return the {@link ComponentMetadataDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultComponentMetadataDef build() {
        
    	
        // contains the elements set with #setInPortMetadata (those added with #addToInPortMetadata have already been inserted into m_inPortMetadata)
        m_inPortMetadataBulkElements = java.util.Objects.requireNonNullElse(m_inPortMetadataBulkElements, Optional.of(java.util.List.of()));
        m_inPortMetadata.get().addAll(0, m_inPortMetadataBulkElements.get());
                
        var inPortMetadataLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_inPortMetadata.get(), m_inPortMetadataContainerSupplyException);
        if(inPortMetadataLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.IN_PORT_METADATA, inPortMetadataLoadExceptionTree);
        }
        m_inPortMetadata = m_inPortMetadata.get().isEmpty() ? Optional.empty() : m_inPortMetadata;
        
        // contains the elements set with #setOutPortMetadata (those added with #addToOutPortMetadata have already been inserted into m_outPortMetadata)
        m_outPortMetadataBulkElements = java.util.Objects.requireNonNullElse(m_outPortMetadataBulkElements, Optional.of(java.util.List.of()));
        m_outPortMetadata.get().addAll(0, m_outPortMetadataBulkElements.get());
                
        var outPortMetadataLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_outPortMetadata.get(), m_outPortMetadataContainerSupplyException);
        if(outPortMetadataLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.OUT_PORT_METADATA, outPortMetadataLoadExceptionTree);
        }
        m_outPortMetadata = m_outPortMetadata.get().isEmpty() ? Optional.empty() : m_outPortMetadata;
        
        return new DefaultComponentMetadataDef(this);
    }    

}
