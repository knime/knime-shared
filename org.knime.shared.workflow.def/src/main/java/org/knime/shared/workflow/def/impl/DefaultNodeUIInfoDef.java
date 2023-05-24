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

import org.knime.shared.workflow.def.BoundsDef;

import org.knime.shared.workflow.def.NodeUIInfoDef;



// for types that define enums
import org.knime.shared.workflow.def.NodeUIInfoDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultNodeUIInfoDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultNodeUIInfoDef implements NodeUIInfoDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<NodeUIInfoDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    @JsonProperty("hasAbsoluteCoordinates")
    protected Boolean m_hasAbsoluteCoordinates;

    @JsonProperty("symbolRelative")
    protected Boolean m_symbolRelative;

    @JsonProperty("bounds")
    protected BoundsDef m_bounds;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultNodeUIInfoDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link NodeUIInfoDefBuilder}.
     * @param builder source
     */
    DefaultNodeUIInfoDef(NodeUIInfoDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_hasAbsoluteCoordinates = builder.m_hasAbsoluteCoordinates;
        m_symbolRelative = builder.m_symbolRelative;
        m_bounds = builder.m_bounds;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link NodeUIInfoDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultNodeUIInfoDef(NodeUIInfoDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new NodeUIInfoDefBuilder().build());
        
        m_hasAbsoluteCoordinates = toCopy.hasAbsoluteCoordinates();
        m_symbolRelative = toCopy.isSymbolRelative();
        m_bounds = toCopy.getBounds();
        if(toCopy instanceof DefaultNodeUIInfoDef){
            var childTree = ((DefaultNodeUIInfoDef)toCopy).getLoadExceptionTree();                
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
    public DefaultNodeUIInfoDef(NodeUIInfoDef toCopy) {
        m_hasAbsoluteCoordinates = toCopy.hasAbsoluteCoordinates();
        m_symbolRelative = toCopy.isSymbolRelative();
        m_bounds = toCopy.getBounds();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing NodeUIInfo
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultNodeUIInfoDef withException(NodeUIInfoDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(NodeUIInfoDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to NodeUIInfoDef.Attribute
            return ((LoadExceptionTree<NodeUIInfoDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Boolean hasAbsoluteCoordinates() {
        return m_hasAbsoluteCoordinates;
    }
    @Override
    public Boolean isSymbolRelative() {
        return m_symbolRelative;
    }
    @Override
    public BoundsDef getBounds() {
        return m_bounds;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to hasAbsoluteCoordinates.
     */
    @JsonIgnore
    public Optional<LoadException> getHasAbsoluteCoordinatesSupplyException(){
    	return getLoadExceptionTree(NodeUIInfoDef.Attribute.HAS_ABSOLUTE_COORDINATES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to symbolRelative.
     */
    @JsonIgnore
    public Optional<LoadException> getSymbolRelativeSupplyException(){
    	return getLoadExceptionTree(NodeUIInfoDef.Attribute.SYMBOL_RELATIVE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to bounds.
     */
    @JsonIgnore
    public Optional<LoadException> getBoundsSupplyException(){
    	return getLoadExceptionTree(NodeUIInfoDef.Attribute.BOUNDS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link BoundsDef} returned by {@link #getBounds()}, this
     * returns the bounds as DefaultBoundsDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultBoundsDef> getFaultyBounds(){
    	final var bounds = getBounds(); 
        if(bounds instanceof DefaultBoundsDef && ((DefaultBoundsDef)bounds).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultBoundsDef)bounds);
        }
    	return Optional.empty();
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
        DefaultNodeUIInfoDef other = (DefaultNodeUIInfoDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_hasAbsoluteCoordinates, other.m_hasAbsoluteCoordinates);
        equalsBuilder.append(m_symbolRelative, other.m_symbolRelative);
        equalsBuilder.append(m_bounds, other.m_bounds);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_hasAbsoluteCoordinates)
                .append(m_symbolRelative)
                .append(m_bounds)
                .toHashCode();
    }

} 
