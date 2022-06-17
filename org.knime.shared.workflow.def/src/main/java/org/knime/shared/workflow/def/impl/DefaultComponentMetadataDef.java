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
import java.util.Objects; 

import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.knime.shared.workflow.def.PortMetadataDef;

import org.knime.shared.workflow.def.ComponentMetadataDef;



// for types that define enums
import org.knime.shared.workflow.def.ComponentMetadataDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultComponentMetadataDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultComponentMetadataDef implements ComponentMetadataDef, LoadExceptionTreeProvider {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<ComponentMetadataDef.Attribute> instance. */
    private final LoadExceptionTree<?> m_exceptionTree;

    @JsonProperty("description")
    protected Optional<String> m_description;

    @JsonProperty("inPortMetadata")
    protected Optional<java.util.List<PortMetadataDef>> m_inPortMetadata;

    @JsonProperty("outPortMetadata")
    protected Optional<java.util.List<PortMetadataDef>> m_outPortMetadata;

    @JsonProperty("icon")
    protected Optional<byte[]> m_icon;

    /** 
     * Summarizes the kind of functionality of the component. 
     */
    @JsonProperty("componentType")
    protected Optional<ComponentTypeEnum> m_componentType;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultComponentMetadataDef() {
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    /**
     * Builder constructor. Only for use in {@link ComponentMetadataDefBuilder}.
     * @param builder source
     */
    DefaultComponentMetadataDef(ComponentMetadataDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_description = builder.m_description;
        m_inPortMetadata = builder.m_inPortMetadata;
        m_outPortMetadata = builder.m_outPortMetadata;
        m_icon = builder.m_icon;
        m_componentType = builder.m_componentType;

        m_exceptionTree = SimpleLoadExceptionTree.map(builder.m_exceptionalChildren);
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link ComponentMetadataDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultComponentMetadataDef(ComponentMetadataDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new ComponentMetadataDefBuilder().build());
        
        m_description = toCopy.getDescription();
        m_inPortMetadata = toCopy.getInPortMetadata();
        m_outPortMetadata = toCopy.getOutPortMetadata();
        m_icon = toCopy.getIcon();
        m_componentType = toCopy.getComponentType();
        if(toCopy instanceof LoadExceptionTreeProvider){
            var childTree = ((LoadExceptionTreeProvider)toCopy).getLoadExceptionTree();                
            // if present, merge child tree with supply exception
            var merged = childTree.hasExceptions() ? SimpleLoadExceptionTree.tree(childTree, supplyException) : supplyException;
            m_exceptionTree = merged;
        } else {
            m_exceptionTree = supplyException;
        }
    }



    /**
     * Copy constructor.
     * @param toCopy source
     */
    public DefaultComponentMetadataDef(ComponentMetadataDef toCopy) {
        m_description = toCopy.getDescription();
        m_inPortMetadata = toCopy.getInPortMetadata();
        m_outPortMetadata = toCopy.getOutPortMetadata();
        m_icon = toCopy.getIcon();
        m_componentType = toCopy.getComponentType();
        
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
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
    static DefaultComponentMetadataDef withException(ComponentMetadataDef toCopy, final LoadException exception) {
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
    public LoadExceptionTree<?> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants.
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(ComponentMetadataDef.Attribute attribute){
        if (m_exceptionTree instanceof LoadException) {
            return Optional.empty();
        }
        // if the tree is not a leaf, it is typed to ComponentMetadataDef.Attribute
        return ((LoadExceptionTree<ComponentMetadataDef.Attribute>)m_exceptionTree).getExceptionTree(attribute);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<String> getDescription() {
        return m_description;
    }
    @Override
    public Optional<java.util.List<PortMetadataDef>> getInPortMetadata() {
        return m_inPortMetadata;
    }
    @Override
    public Optional<java.util.List<PortMetadataDef>> getOutPortMetadata() {
        return m_outPortMetadata;
    }
    @Override
    public Optional<byte[]> getIcon() {
        return m_icon;
    }
    @Override
    public Optional<ComponentTypeEnum> getComponentType() {
        return m_componentType;
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
     * @return The supply exception associated to inPortMetadata.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortMetadataSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.IN_PORT_METADATA).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getInPortMetadataExceptionTree(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.IN_PORT_METADATA).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to outPortMetadata.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortMetadataSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.OUT_PORT_METADATA).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getOutPortMetadataExceptionTree(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.OUT_PORT_METADATA).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to icon.
     */
    @JsonIgnore
    public Optional<LoadException> getIconSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.ICON).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to componentType.
     */
    @JsonIgnore
    public Optional<LoadException> getComponentTypeSupplyException(){
    	return getLoadExceptionTree(ComponentMetadataDef.Attribute.COMPONENT_TYPE).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultComponentMetadataDef other = (DefaultComponentMetadataDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_description, other.m_description);
        equalsBuilder.append(m_inPortMetadata, other.m_inPortMetadata);
        equalsBuilder.append(m_outPortMetadata, other.m_outPortMetadata);
        equalsBuilder.append(m_icon, other.m_icon);
        equalsBuilder.append(m_componentType, other.m_componentType);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_description)
                .append(m_inPortMetadata)
                .append(m_outPortMetadata)
                .append(m_icon)
                .append(m_componentType)
                .toHashCode();
    }

} 
