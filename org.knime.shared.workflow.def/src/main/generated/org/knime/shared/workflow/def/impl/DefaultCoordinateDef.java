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


import org.knime.shared.workflow.def.CoordinateDef;



// for types that define enums
import org.knime.shared.workflow.def.CoordinateDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.shared.workflow.def.LoadException;
import org.knime.shared.workflow.def.LoadExceptionTree;
import org.knime.shared.workflow.def.LoadExceptionTreeProvider;
import org.knime.shared.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultCoordinateDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultCoordinateDef implements CoordinateDef, LoadExceptionTreeProvider {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<CoordinateDef.Attribute> instance. */
    private final LoadExceptionTree<?> m_exceptionTree;

    /** 
     * Horizontal offset of an object. 
     */
    @JsonProperty("x")
    protected Integer m_x;

    /** 
     * Vertical offset of an object. 
     */
    @JsonProperty("y")
    protected Integer m_y;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultCoordinateDef() {
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    /**
     * Builder constructor. Only for use in {@link CoordinateDefBuilder}.
     * @param builder source
     */
    DefaultCoordinateDef(CoordinateDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_x = builder.m_x;
        m_y = builder.m_y;

        m_exceptionTree = SimpleLoadExceptionTree.map(builder.m_exceptionalChildren);
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link CoordinateDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultCoordinateDef(CoordinateDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new CoordinateDefBuilder().build());
        
        m_x = toCopy.getX();
        m_y = toCopy.getY();
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
    public DefaultCoordinateDef(CoordinateDef toCopy) {
        m_x = toCopy.getX();
        m_y = toCopy.getY();
        
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing Coordinate
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultCoordinateDef withException(CoordinateDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(CoordinateDef.Attribute attribute){
        if (m_exceptionTree instanceof LoadException) {
            return Optional.empty();
        }
        // if the tree is not a leaf, it is typed to CoordinateDef.Attribute
        return ((LoadExceptionTree<CoordinateDef.Attribute>)m_exceptionTree).getExceptionTree(attribute);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Integer getX() {
        return m_x;
    }
    @Override
    public Integer getY() {
        return m_y;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to x.
     */
    @JsonIgnore
    public Optional<LoadException> getXSupplyException(){
    	return getLoadExceptionTree(CoordinateDef.Attribute.X).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to y.
     */
    @JsonIgnore
    public Optional<LoadException> getYSupplyException(){
    	return getLoadExceptionTree(CoordinateDef.Attribute.Y).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultCoordinateDef other = (DefaultCoordinateDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_x, other.m_x);
        equalsBuilder.append(m_y, other.m_y);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_x)
                .append(m_y)
                .toHashCode();
    }

} 
