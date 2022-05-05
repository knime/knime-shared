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


// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.ComponentMetadataDef;
// for types that define enums
import org.knime.core.workflow.def.ComponentMetadataDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * ComponentMetadataDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class ComponentMetadataDefBuilder {

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
    String m_description;
    

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<String> m_inPortNames = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setInPortNames these are added to m_inPortNames in build */
    private java.util.List<String> m_inPortNamesBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_inPortNamesContainerSupplyException; 
    
    /** Associates an offset in {@link #m_inPortNames} to the supply exception that caused its addition to the list (i.e., it's a forced default value). Merged during {@link #build()} with {@link m_inPortNamesContainerSupplyException} into a single {@link LoadExceptionTree} */
    private java.util.Map<Integer, LoadException> m_inPortNamesElementSupplyExceptions = new java.util.HashMap<>();

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<String> m_outPortNames = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setOutPortNames these are added to m_outPortNames in build */
    private java.util.List<String> m_outPortNamesBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_outPortNamesContainerSupplyException; 
    
    /** Associates an offset in {@link #m_outPortNames} to the supply exception that caused its addition to the list (i.e., it's a forced default value). Merged during {@link #build()} with {@link m_outPortNamesContainerSupplyException} into a single {@link LoadExceptionTree} */
    private java.util.Map<Integer, LoadException> m_outPortNamesElementSupplyExceptions = new java.util.HashMap<>();

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<String> m_inPortDescriptions = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setInPortDescriptions these are added to m_inPortDescriptions in build */
    private java.util.List<String> m_inPortDescriptionsBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_inPortDescriptionsContainerSupplyException; 
    
    /** Associates an offset in {@link #m_inPortDescriptions} to the supply exception that caused its addition to the list (i.e., it's a forced default value). Merged during {@link #build()} with {@link m_inPortDescriptionsContainerSupplyException} into a single {@link LoadExceptionTree} */
    private java.util.Map<Integer, LoadException> m_inPortDescriptionsElementSupplyExceptions = new java.util.HashMap<>();

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<String> m_outPortDescriptions = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setOutPortDescriptions these are added to m_outPortDescriptions in build */
    private java.util.List<String> m_outPortDescriptionsBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_outPortDescriptionsContainerSupplyException; 
    
    /** Associates an offset in {@link #m_outPortDescriptions} to the supply exception that caused its addition to the list (i.e., it's a forced default value). Merged during {@link #build()} with {@link m_outPortDescriptionsContainerSupplyException} into a single {@link LoadExceptionTree} */
    private java.util.Map<Integer, LoadException> m_outPortDescriptionsElementSupplyExceptions = new java.util.HashMap<>();

    byte[] m_icon;
    

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
        m_inPortNames = toCopy.getInPortNames();
        m_outPortNames = toCopy.getOutPortNames();
        m_inPortDescriptions = toCopy.getInPortDescriptions();
        m_outPortDescriptions = toCopy.getOutPortDescriptions();
        m_icon = toCopy.getIcon();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for description
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param description 
     * @return this builder for fluent API.
     */ 
    public ComponentMetadataDefBuilder setDescription(final String description) {
        setDescription(() -> description, description);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_description = description.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_description = defaultValue;
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.DESCRIPTION, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for inPortNames
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the inPortNames list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToInPortNames} will be inserted at the end of the list.
     * @param inPortNames 
     * @return this for fluent API
     */
    public ComponentMetadataDefBuilder setInPortNames(final java.util.List<String> inPortNames) {
        setInPortNames(() -> inPortNames);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the inPortNames list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the inPortNames list). 
     * {@code hasExceptions(ComponentMetadataDef.Attribute.IN_PORT_NAMES)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.IN_PORT_NAMES)} will return the exception.
     * 
     * @param inPortNames see {@link ComponentMetadataDef#getInPortNames}
     * 
     * @return this builder for fluent API.
     * @see #setInPortNames(java.util.List<String>)
     */
    public ComponentMetadataDefBuilder setInPortNames(final FallibleSupplier<java.util.List<String>> inPortNames) {
        java.util.Objects.requireNonNull(inPortNames, () -> "No supplier for inPortNames provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.IN_PORT_NAMES);
        try {
            m_inPortNamesBulkElements = inPortNames.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_inPortNamesBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_inPortNamesContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the inPortNames list
     * @return this builder for fluent API
     */
    public ComponentMetadataDefBuilder addToInPortNames(String value){
    	addToInPortNames(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentMetadataDef#getInPortNames}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the inPortNames list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentMetadataDefBuilder addToInPortNames(FallibleSupplier<String> value, String defaultValue) {
        String toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            m_inPortNamesElementSupplyExceptions.put(m_inPortNames.size(), supplyException);
            toAdd = defaultValue;
        }
        m_inPortNames.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for outPortNames
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the outPortNames list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToOutPortNames} will be inserted at the end of the list.
     * @param outPortNames 
     * @return this for fluent API
     */
    public ComponentMetadataDefBuilder setOutPortNames(final java.util.List<String> outPortNames) {
        setOutPortNames(() -> outPortNames);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the outPortNames list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the outPortNames list). 
     * {@code hasExceptions(ComponentMetadataDef.Attribute.OUT_PORT_NAMES)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.OUT_PORT_NAMES)} will return the exception.
     * 
     * @param outPortNames see {@link ComponentMetadataDef#getOutPortNames}
     * 
     * @return this builder for fluent API.
     * @see #setOutPortNames(java.util.List<String>)
     */
    public ComponentMetadataDefBuilder setOutPortNames(final FallibleSupplier<java.util.List<String>> outPortNames) {
        java.util.Objects.requireNonNull(outPortNames, () -> "No supplier for outPortNames provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.OUT_PORT_NAMES);
        try {
            m_outPortNamesBulkElements = outPortNames.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_outPortNamesBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_outPortNamesContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the outPortNames list
     * @return this builder for fluent API
     */
    public ComponentMetadataDefBuilder addToOutPortNames(String value){
    	addToOutPortNames(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentMetadataDef#getOutPortNames}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the outPortNames list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentMetadataDefBuilder addToOutPortNames(FallibleSupplier<String> value, String defaultValue) {
        String toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            m_outPortNamesElementSupplyExceptions.put(m_outPortNames.size(), supplyException);
            toAdd = defaultValue;
        }
        m_outPortNames.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for inPortDescriptions
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the inPortDescriptions list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToInPortDescriptions} will be inserted at the end of the list.
     * @param inPortDescriptions 
     * @return this for fluent API
     */
    public ComponentMetadataDefBuilder setInPortDescriptions(final java.util.List<String> inPortDescriptions) {
        setInPortDescriptions(() -> inPortDescriptions);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the inPortDescriptions list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the inPortDescriptions list). 
     * {@code hasExceptions(ComponentMetadataDef.Attribute.IN_PORT_DESCRIPTIONS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.IN_PORT_DESCRIPTIONS)} will return the exception.
     * 
     * @param inPortDescriptions see {@link ComponentMetadataDef#getInPortDescriptions}
     * 
     * @return this builder for fluent API.
     * @see #setInPortDescriptions(java.util.List<String>)
     */
    public ComponentMetadataDefBuilder setInPortDescriptions(final FallibleSupplier<java.util.List<String>> inPortDescriptions) {
        java.util.Objects.requireNonNull(inPortDescriptions, () -> "No supplier for inPortDescriptions provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.IN_PORT_DESCRIPTIONS);
        try {
            m_inPortDescriptionsBulkElements = inPortDescriptions.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_inPortDescriptionsBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_inPortDescriptionsContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the inPortDescriptions list
     * @return this builder for fluent API
     */
    public ComponentMetadataDefBuilder addToInPortDescriptions(String value){
    	addToInPortDescriptions(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentMetadataDef#getInPortDescriptions}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the inPortDescriptions list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentMetadataDefBuilder addToInPortDescriptions(FallibleSupplier<String> value, String defaultValue) {
        String toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            m_inPortDescriptionsElementSupplyExceptions.put(m_inPortDescriptions.size(), supplyException);
            toAdd = defaultValue;
        }
        m_inPortDescriptions.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for outPortDescriptions
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the outPortDescriptions list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToOutPortDescriptions} will be inserted at the end of the list.
     * @param outPortDescriptions 
     * @return this for fluent API
     */
    public ComponentMetadataDefBuilder setOutPortDescriptions(final java.util.List<String> outPortDescriptions) {
        setOutPortDescriptions(() -> outPortDescriptions);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the outPortDescriptions list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the outPortDescriptions list). 
     * {@code hasExceptions(ComponentMetadataDef.Attribute.OUT_PORT_DESCRIPTIONS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentMetadataDef.Attribute.OUT_PORT_DESCRIPTIONS)} will return the exception.
     * 
     * @param outPortDescriptions see {@link ComponentMetadataDef#getOutPortDescriptions}
     * 
     * @return this builder for fluent API.
     * @see #setOutPortDescriptions(java.util.List<String>)
     */
    public ComponentMetadataDefBuilder setOutPortDescriptions(final FallibleSupplier<java.util.List<String>> outPortDescriptions) {
        java.util.Objects.requireNonNull(outPortDescriptions, () -> "No supplier for outPortDescriptions provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentMetadataDef.Attribute.OUT_PORT_DESCRIPTIONS);
        try {
            m_outPortDescriptionsBulkElements = outPortDescriptions.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_outPortDescriptionsBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_outPortDescriptionsContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the outPortDescriptions list
     * @return this builder for fluent API
     */
    public ComponentMetadataDefBuilder addToOutPortDescriptions(String value){
    	addToOutPortDescriptions(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentMetadataDef#getOutPortDescriptions}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the outPortDescriptions list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentMetadataDefBuilder addToOutPortDescriptions(FallibleSupplier<String> value, String defaultValue) {
        String toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            m_outPortDescriptionsElementSupplyExceptions.put(m_outPortDescriptions.size(), supplyException);
            toAdd = defaultValue;
        }
        m_outPortDescriptions.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for icon
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param icon 
     * @return this builder for fluent API.
     */ 
    public ComponentMetadataDefBuilder setIcon(final byte[] icon) {
        setIcon(() -> icon, icon);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_icon = icon.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_icon = defaultValue;
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.ICON, supplyException);
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
        
    	
        // contains the elements set with #setInPortNames (those added with #addToInPortNames have already been inserted into m_inPortNames)
        m_inPortNamesBulkElements = java.util.Objects.requireNonNullElse(m_inPortNamesBulkElements, java.util.List.of());
        m_inPortNames.addAll(0, m_inPortNamesBulkElements);
        
        var inPortNamesLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_inPortNamesElementSupplyExceptions, m_inPortNamesContainerSupplyException);
                if(inPortNamesLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.IN_PORT_NAMES, inPortNamesLoadExceptionTree);
        }
        
        // contains the elements set with #setOutPortNames (those added with #addToOutPortNames have already been inserted into m_outPortNames)
        m_outPortNamesBulkElements = java.util.Objects.requireNonNullElse(m_outPortNamesBulkElements, java.util.List.of());
        m_outPortNames.addAll(0, m_outPortNamesBulkElements);
        
        var outPortNamesLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_outPortNamesElementSupplyExceptions, m_outPortNamesContainerSupplyException);
                if(outPortNamesLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.OUT_PORT_NAMES, outPortNamesLoadExceptionTree);
        }
        
        // contains the elements set with #setInPortDescriptions (those added with #addToInPortDescriptions have already been inserted into m_inPortDescriptions)
        m_inPortDescriptionsBulkElements = java.util.Objects.requireNonNullElse(m_inPortDescriptionsBulkElements, java.util.List.of());
        m_inPortDescriptions.addAll(0, m_inPortDescriptionsBulkElements);
        
        var inPortDescriptionsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_inPortDescriptionsElementSupplyExceptions, m_inPortDescriptionsContainerSupplyException);
                if(inPortDescriptionsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.IN_PORT_DESCRIPTIONS, inPortDescriptionsLoadExceptionTree);
        }
        
        // contains the elements set with #setOutPortDescriptions (those added with #addToOutPortDescriptions have already been inserted into m_outPortDescriptions)
        m_outPortDescriptionsBulkElements = java.util.Objects.requireNonNullElse(m_outPortDescriptionsBulkElements, java.util.List.of());
        m_outPortDescriptions.addAll(0, m_outPortDescriptionsBulkElements);
        
        var outPortDescriptionsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_outPortDescriptionsElementSupplyExceptions, m_outPortDescriptionsContainerSupplyException);
                if(outPortDescriptionsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentMetadataDef.Attribute.OUT_PORT_DESCRIPTIONS, outPortDescriptionsLoadExceptionTree);
        }
        
        return new DefaultComponentMetadataDef(this);
    }    

}
