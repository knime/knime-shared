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
import java.util.Optional;
import java.util.Objects; 

import org.apache.commons.lang3.builder.HashCodeBuilder;


import org.knime.core.workflow.def.ComponentMetadataDef;



// for types that define enums
import org.knime.core.workflow.def.ComponentMetadataDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * FallibleComponentMetadataDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class FallibleComponentMetadataDef implements ComponentMetadataDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<ComponentMetadataDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    @JsonProperty("description")
    protected String m_description;

    @JsonProperty("inPortNames")
    protected java.util.List<String> m_inPortNames;

    @JsonProperty("outPortNames")
    protected java.util.List<String> m_outPortNames;

    @JsonProperty("inPortDescriptions")
    protected java.util.List<String> m_inPortDescriptions;

    @JsonProperty("outPortDescriptions")
    protected java.util.List<String> m_outPortDescriptions;

    @JsonProperty("icon")
    protected byte[] m_icon;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    FallibleComponentMetadataDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link ComponentMetadataDefBuilder}.
     * @param builder source
     */
    FallibleComponentMetadataDef(ComponentMetadataDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_description = builder.m_description;
        m_inPortNames = builder.m_inPortNames;
        m_outPortNames = builder.m_outPortNames;
        m_inPortDescriptions = builder.m_inPortDescriptions;
        m_outPortDescriptions = builder.m_outPortDescriptions;
        m_icon = builder.m_icon;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link ComponentMetadataDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    FallibleComponentMetadataDef(ComponentMetadataDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new ComponentMetadataDefBuilder().build());
        
        m_description = toCopy.getDescription();
        m_inPortNames = toCopy.getInPortNames();
        m_outPortNames = toCopy.getOutPortNames();
        m_inPortDescriptions = toCopy.getInPortDescriptions();
        m_outPortDescriptions = toCopy.getOutPortDescriptions();
        m_icon = toCopy.getIcon();
        if(toCopy instanceof FallibleComponentMetadataDef){
            var childTree = ((FallibleComponentMetadataDef)toCopy).getLoadExceptionTree();                
            // if present, merge child tree with supply exception
            var merged = childTree.isEmpty() ? supplyException : SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            m_exceptionTree = Optional.of(merged);
        } else {
            m_exceptionTree = Optional.of(supplyException);
        }
    }



    /**
     * Copy constructor.
     * @param toCopy source
     */
    public FallibleComponentMetadataDef(ComponentMetadataDef toCopy) {
        m_description = toCopy.getDescription();
        m_inPortNames = toCopy.getInPortNames();
        m_outPortNames = toCopy.getOutPortNames();
        m_inPortDescriptions = toCopy.getInPortDescriptions();
        m_outPortDescriptions = toCopy.getOutPortDescriptions();
        m_icon = toCopy.getIcon();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing ComponentMetadata
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static FallibleComponentMetadataDef withException(ComponentMetadataDef toCopy, final LoadException exception) {
        Objects.requireNonNull(exception);
        throw new IllegalArgumentException();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree implementation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @return the load exceptions for this instance and its descendants
     */
    @JsonIgnore
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(ComponentMetadataDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to ComponentMetadataDef.Attribute
            return ((LoadExceptionTree<ComponentMetadataDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getDescription() {
        return m_description;
    }
    @Override
    public java.util.List<String> getInPortNames() {
        return m_inPortNames;
    }
    @Override
    public java.util.List<String> getOutPortNames() {
        return m_outPortNames;
    }
    @Override
    public java.util.List<String> getInPortDescriptions() {
        return m_inPortDescriptions;
    }
    @Override
    public java.util.List<String> getOutPortDescriptions() {
        return m_outPortDescriptions;
    }
    @Override
    public byte[] getIcon() {
        return m_icon;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to description.
     */
    @JsonIgnore
    public Optional<LoadException> getDescriptionSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.DESCRIPTION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to inPortNames.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortNamesSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.IN_PORT_NAMES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
    /**
     * @return the InPortNames that have load exceptions or descendants with load exceptions.
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Optional<LoadExceptionTree<Integer>> getInPortNamesExceptionTree(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.IN_PORT_NAMES).map(les -> (LoadExceptionTree<Integer>)les);
    }

    /**
     * @param childIdentifier the element in the list to get the exceptions for
     * @return the load exception associated to the given element of the InPortNames container.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortNamesElementSupplyException(Integer childIdentifier){
    	return getInPortNamesExceptionTree()//
            // optional load exceptions associated to the inPortNames
            .flatMap(iles -> iles.getExceptionTree(childIdentifier))//
            // if present, it is a LoadExceptionTree, but since this is a type without children, just return the terminal supply exception
            .flatMap(LoadExceptionTree::getSupplyException);
    }
     
    /**
     * @return The supply exception associated to outPortNames.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortNamesSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.OUT_PORT_NAMES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
    /**
     * @return the OutPortNames that have load exceptions or descendants with load exceptions.
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Optional<LoadExceptionTree<Integer>> getOutPortNamesExceptionTree(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.OUT_PORT_NAMES).map(les -> (LoadExceptionTree<Integer>)les);
    }

    /**
     * @param childIdentifier the element in the list to get the exceptions for
     * @return the load exception associated to the given element of the OutPortNames container.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortNamesElementSupplyException(Integer childIdentifier){
    	return getOutPortNamesExceptionTree()//
            // optional load exceptions associated to the outPortNames
            .flatMap(iles -> iles.getExceptionTree(childIdentifier))//
            // if present, it is a LoadExceptionTree, but since this is a type without children, just return the terminal supply exception
            .flatMap(LoadExceptionTree::getSupplyException);
    }
     
    /**
     * @return The supply exception associated to inPortDescriptions.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortDescriptionsSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.IN_PORT_DESCRIPTIONS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
    /**
     * @return the InPortDescriptions that have load exceptions or descendants with load exceptions.
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Optional<LoadExceptionTree<Integer>> getInPortDescriptionsExceptionTree(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.IN_PORT_DESCRIPTIONS).map(les -> (LoadExceptionTree<Integer>)les);
    }

    /**
     * @param childIdentifier the element in the list to get the exceptions for
     * @return the load exception associated to the given element of the InPortDescriptions container.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortDescriptionsElementSupplyException(Integer childIdentifier){
    	return getInPortDescriptionsExceptionTree()//
            // optional load exceptions associated to the inPortDescriptions
            .flatMap(iles -> iles.getExceptionTree(childIdentifier))//
            // if present, it is a LoadExceptionTree, but since this is a type without children, just return the terminal supply exception
            .flatMap(LoadExceptionTree::getSupplyException);
    }
     
    /**
     * @return The supply exception associated to outPortDescriptions.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortDescriptionsSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.OUT_PORT_DESCRIPTIONS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
    /**
     * @return the OutPortDescriptions that have load exceptions or descendants with load exceptions.
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Optional<LoadExceptionTree<Integer>> getOutPortDescriptionsExceptionTree(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.OUT_PORT_DESCRIPTIONS).map(les -> (LoadExceptionTree<Integer>)les);
    }

    /**
     * @param childIdentifier the element in the list to get the exceptions for
     * @return the load exception associated to the given element of the OutPortDescriptions container.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortDescriptionsElementSupplyException(Integer childIdentifier){
    	return getOutPortDescriptionsExceptionTree()//
            // optional load exceptions associated to the outPortDescriptions
            .flatMap(iles -> iles.getExceptionTree(childIdentifier))//
            // if present, it is a LoadExceptionTree, but since this is a type without children, just return the terminal supply exception
            .flatMap(LoadExceptionTree::getSupplyException);
    }
     
    /**
     * @return The supply exception associated to icon.
     */
    @JsonIgnore
    public Optional<LoadException> getIconSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.ICON).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    

    // -----------------------------------------------------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o.getClass().equals(this.getClass()))) {
            return false;
        }
        FallibleComponentMetadataDef other = (FallibleComponentMetadataDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_description, other.m_description);
        equalsBuilder.append(m_inPortNames, other.m_inPortNames);
        equalsBuilder.append(m_outPortNames, other.m_outPortNames);
        equalsBuilder.append(m_inPortDescriptions, other.m_inPortDescriptions);
        equalsBuilder.append(m_outPortDescriptions, other.m_outPortDescriptions);
        equalsBuilder.append(m_icon, other.m_icon);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_description)
                .append(m_inPortNames)
                .append(m_outPortNames)
                .append(m_inPortDescriptions)
                .append(m_outPortDescriptions)
                .append(m_icon)
                .toHashCode();
    }

} 
