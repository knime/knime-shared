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


import org.knime.core.workflow.def.VendorDef;



// for types that define enums
import org.knime.core.workflow.def.VendorDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * The author/provider of a native node. Describes bundles and features that contain the code to execute  a native node.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class FallibleVendorDef implements VendorDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<VendorDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    /** 
     * For a bundle, see the example value; for a feature it can be an empty string 
     * 
     * Example value: KNIME Base Nodes
     */
    @JsonProperty("name")
    protected String m_name;

    /** 
     * For a bundle, see the example value; for a feature it can be an empty string 
     * 
     * Example value: org.knime.base
     */
    @JsonProperty("symbolicName")
    protected String m_symbolicName;

    /** 
     * For a bundle, see the example value; for a feature it can be an empty string 
     * 
     * Example value: KNIME AG, Zurich, Switzerland
     */
    @JsonProperty("vendor")
    protected String m_vendor;

    /** 
     * For a bundle, see the example value; for a feature, it can be, e.g., \&quot;0.0.0\&quot; 
     * 
     * Example value: 4.6.0.v202201041551
     */
    @JsonProperty("version")
    protected String m_version;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    FallibleVendorDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link VendorDefBuilder}.
     * @param builder source
     */
    FallibleVendorDef(VendorDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_name = builder.m_name;
        m_symbolicName = builder.m_symbolicName;
        m_vendor = builder.m_vendor;
        m_version = builder.m_version;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link VendorDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    FallibleVendorDef(VendorDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new VendorDefBuilder().build());
        
        m_name = toCopy.getName();
        m_symbolicName = toCopy.getSymbolicName();
        m_vendor = toCopy.getVendor();
        m_version = toCopy.getVersion();
        if(toCopy instanceof FallibleVendorDef){
            var childTree = ((FallibleVendorDef)toCopy).getLoadExceptionTree();                
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
    public FallibleVendorDef(VendorDef toCopy) {
        m_name = toCopy.getName();
        m_symbolicName = toCopy.getSymbolicName();
        m_vendor = toCopy.getVendor();
        m_version = toCopy.getVersion();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing Vendor
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static FallibleVendorDef withException(VendorDef toCopy, final LoadException exception) {
        Objects.requireNonNull(exception);
        throw new IllegalArgumentException();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree implementation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @return the load exceptions for this instance and its descendants
     */
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(VendorDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to VendorDef.Attribute
            return ((LoadExceptionTree<VendorDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getName() {
        return m_name;
    }
    @Override
    public String getSymbolicName() {
        return m_symbolicName;
    }
    @Override
    public String getVendor() {
        return m_vendor;
    }
    @Override
    public String getVersion() {
        return m_version;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to name.
     */
    @JsonIgnore
    public Optional<LoadException> getNameSupplyException(){
    	return getLoadExceptionTree(VendorDef.Attribute.NAME).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to symbolicName.
     */
    @JsonIgnore
    public Optional<LoadException> getSymbolicNameSupplyException(){
    	return getLoadExceptionTree(VendorDef.Attribute.SYMBOLIC_NAME).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to vendor.
     */
    @JsonIgnore
    public Optional<LoadException> getVendorSupplyException(){
    	return getLoadExceptionTree(VendorDef.Attribute.VENDOR).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to version.
     */
    @JsonIgnore
    public Optional<LoadException> getVersionSupplyException(){
    	return getLoadExceptionTree(VendorDef.Attribute.VERSION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    

    // -----------------------------------------------------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FallibleVendorDef)) {
            return false;
        }
        FallibleVendorDef other = (FallibleVendorDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.appendSuper(super.equals(other));
        equalsBuilder.append(m_name, other.m_name);
        equalsBuilder.append(m_symbolicName, other.m_symbolicName);
        equalsBuilder.append(m_vendor, other.m_vendor);
        equalsBuilder.append(m_version, other.m_version);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_name)
                .append(m_symbolicName)
                .append(m_vendor)
                .append(m_version)
                .toHashCode();
    }

} 
