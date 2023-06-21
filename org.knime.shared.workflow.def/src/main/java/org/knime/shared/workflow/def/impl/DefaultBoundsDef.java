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

import org.knime.shared.workflow.def.BoundsDef;



// for types that define enums
import org.knime.shared.workflow.def.BoundsDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultBoundsDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @noextend This class is not intended to be subclassed by clients.
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultBoundsDef implements BoundsDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<BoundsDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    /**
     */
    @JsonProperty("location")
    protected CoordinateDef m_location;

    /**
     * Width of an object in pixels.
     */
    @JsonProperty("width")
    protected Integer m_width;

    /**
     * Height of an object in pixels.
     */
    @JsonProperty("height")
    protected Integer m_height;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultBoundsDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link BoundsDefBuilder}.
     * @param builder source
     */
    DefaultBoundsDef(BoundsDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_location = builder.m_location;
        m_width = builder.m_width;
        m_height = builder.m_height;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link BoundsDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultBoundsDef(BoundsDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new BoundsDefBuilder().build());
        
        m_location = toCopy.getLocation();
        m_width = toCopy.getWidth();
        m_height = toCopy.getHeight();
        if(toCopy instanceof DefaultBoundsDef){
            var childTree = ((DefaultBoundsDef)toCopy).getLoadExceptionTree();                
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
    public DefaultBoundsDef(BoundsDef toCopy) {
        m_location = toCopy.getLocation();
        m_width = toCopy.getWidth();
        m_height = toCopy.getHeight();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing Bounds
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultBoundsDef withException(BoundsDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(BoundsDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to BoundsDef.Attribute
            return ((LoadExceptionTree<BoundsDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public CoordinateDef getLocation() {
        return m_location;
    }
    @Override
    public Integer getWidth() {
        return m_width;
    }
    @Override
    public Integer getHeight() {
        return m_height;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to location.
     */
    @JsonIgnore
    public Optional<LoadException> getLocationSupplyException(){
    	return getLoadExceptionTree(BoundsDef.Attribute.LOCATION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link CoordinateDef} returned by {@link #getLocation()}, this
     * returns the location as DefaultCoordinateDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultCoordinateDef> getFaultyLocation(){
    	final var location = getLocation(); 
        if(location instanceof DefaultCoordinateDef && ((DefaultCoordinateDef)location).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultCoordinateDef)location);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to width.
     */
    @JsonIgnore
    public Optional<LoadException> getWidthSupplyException(){
    	return getLoadExceptionTree(BoundsDef.Attribute.WIDTH).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to height.
     */
    @JsonIgnore
    public Optional<LoadException> getHeightSupplyException(){
    	return getLoadExceptionTree(BoundsDef.Attribute.HEIGHT).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultBoundsDef other = (DefaultBoundsDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_location, other.m_location);
        equalsBuilder.append(m_width, other.m_width);
        equalsBuilder.append(m_height, other.m_height);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_location)
                .append(m_width)
                .append(m_height)
                .toHashCode();
    }

} 
