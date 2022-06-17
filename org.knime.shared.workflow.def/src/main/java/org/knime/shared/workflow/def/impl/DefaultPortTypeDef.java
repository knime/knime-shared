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


import org.knime.shared.workflow.def.PortTypeDef;



// for types that define enums
import org.knime.shared.workflow.def.PortTypeDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultPortTypeDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultPortTypeDef implements PortTypeDef, LoadExceptionTreeProvider {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<PortTypeDef.Attribute> instance. */
    private final LoadExceptionTree<?> m_exceptionTree;

    /** 
     * the class of the port object this port type is associated with 
     * 
     * Example value: org.knime.core.node.port.flowvariable.FlowVariablePortObject
     */
    @JsonProperty("portObjectClass")
    protected String m_portObjectClass;

    /** 
     * Returns the class of the port object spec. 
     */
    @JsonProperty("portObjectSpecClass")
    protected Optional<String> m_portObjectSpecClass;

    @JsonProperty("color")
    protected Optional<Integer> m_color;

    /** 
     * whether to short this port to users, e.g., in dialogs 
     */
    @JsonProperty("hidden")
    protected Boolean m_hidden;

    /** 
     * whether this port needs to be connected 
     */
    @JsonProperty("optional")
    protected Boolean m_optional;

    /** 
     * human-readable name. In case the port type is not registered at the extension point, the port object&#39;s class name is returned. 
     */
    @JsonProperty("name")
    protected Optional<String> m_name;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultPortTypeDef() {
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    /**
     * Builder constructor. Only for use in {@link PortTypeDefBuilder}.
     * @param builder source
     */
    DefaultPortTypeDef(PortTypeDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_portObjectClass = builder.m_portObjectClass;
        m_portObjectSpecClass = builder.m_portObjectSpecClass;
        m_color = builder.m_color;
        m_hidden = builder.m_hidden;
        m_optional = builder.m_optional;
        m_name = builder.m_name;

        m_exceptionTree = SimpleLoadExceptionTree.map(builder.m_exceptionalChildren);
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link PortTypeDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultPortTypeDef(PortTypeDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new PortTypeDefBuilder().build());
        
        m_portObjectClass = toCopy.getPortObjectClass();
        m_portObjectSpecClass = toCopy.getPortObjectSpecClass();
        m_color = toCopy.getColor();
        m_hidden = toCopy.isHidden();
        m_optional = toCopy.isOptional();
        m_name = toCopy.getName();
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
    public DefaultPortTypeDef(PortTypeDef toCopy) {
        m_portObjectClass = toCopy.getPortObjectClass();
        m_portObjectSpecClass = toCopy.getPortObjectSpecClass();
        m_color = toCopy.getColor();
        m_hidden = toCopy.isHidden();
        m_optional = toCopy.isOptional();
        m_name = toCopy.getName();
        
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing PortType
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultPortTypeDef withException(PortTypeDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(PortTypeDef.Attribute attribute){
        if (m_exceptionTree instanceof LoadException) {
            return Optional.empty();
        }
        // if the tree is not a leaf, it is typed to PortTypeDef.Attribute
        return ((LoadExceptionTree<PortTypeDef.Attribute>)m_exceptionTree).getExceptionTree(attribute);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getPortObjectClass() {
        return m_portObjectClass;
    }
    @Override
    public Optional<String> getPortObjectSpecClass() {
        return m_portObjectSpecClass;
    }
    @Override
    public Optional<Integer> getColor() {
        return m_color;
    }
    @Override
    public Boolean isHidden() {
        return m_hidden;
    }
    @Override
    public Boolean isOptional() {
        return m_optional;
    }
    @Override
    public Optional<String> getName() {
        return m_name;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to portObjectClass.
     */
    @JsonIgnore
    public Optional<LoadException> getPortObjectClassSupplyException(){
    	return getLoadExceptionTree(PortTypeDef.Attribute.PORT_OBJECT_CLASS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to portObjectSpecClass.
     */
    @JsonIgnore
    public Optional<LoadException> getPortObjectSpecClassSupplyException(){
    	return getLoadExceptionTree(PortTypeDef.Attribute.PORT_OBJECT_SPEC_CLASS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to color.
     */
    @JsonIgnore
    public Optional<LoadException> getColorSupplyException(){
    	return getLoadExceptionTree(PortTypeDef.Attribute.COLOR).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to hidden.
     */
    @JsonIgnore
    public Optional<LoadException> getHiddenSupplyException(){
    	return getLoadExceptionTree(PortTypeDef.Attribute.HIDDEN).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to optional.
     */
    @JsonIgnore
    public Optional<LoadException> getOptionalSupplyException(){
    	return getLoadExceptionTree(PortTypeDef.Attribute.OPTIONAL).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to name.
     */
    @JsonIgnore
    public Optional<LoadException> getNameSupplyException(){
    	return getLoadExceptionTree(PortTypeDef.Attribute.NAME).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultPortTypeDef other = (DefaultPortTypeDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_portObjectClass, other.m_portObjectClass);
        equalsBuilder.append(m_portObjectSpecClass, other.m_portObjectSpecClass);
        equalsBuilder.append(m_color, other.m_color);
        equalsBuilder.append(m_hidden, other.m_hidden);
        equalsBuilder.append(m_optional, other.m_optional);
        equalsBuilder.append(m_name, other.m_name);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_portObjectClass)
                .append(m_portObjectSpecClass)
                .append(m_color)
                .append(m_hidden)
                .append(m_optional)
                .append(m_name)
                .toHashCode();
    }

} 
