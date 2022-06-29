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


import org.knime.shared.workflow.def.StyleRangeDef;



// for types that define enums
import org.knime.shared.workflow.def.StyleRangeDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.shared.workflow.def.LoadException;
import org.knime.shared.workflow.def.LoadExceptionTree;
import org.knime.shared.workflow.def.LoadExceptionTreeProvider;
import org.knime.shared.workflow.def.SimpleLoadExceptionTree;


/**
 * Similar to a HTML span.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultStyleRangeDef implements StyleRangeDef, LoadExceptionTreeProvider {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<StyleRangeDef.Attribute> instance. */
    private final LoadExceptionTree<?> m_exceptionTree;

    @JsonProperty("fontSize")
    protected Optional<Integer> m_fontSize;

    @JsonProperty("color")
    protected Optional<Integer> m_color;

    @JsonProperty("start")
    protected Integer m_start;

    @JsonProperty("fontName")
    protected Optional<String> m_fontName;

    @JsonProperty("fontStyle")
    protected Optional<Integer> m_fontStyle;

    @JsonProperty("length")
    protected Integer m_length;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultStyleRangeDef() {
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    /**
     * Builder constructor. Only for use in {@link StyleRangeDefBuilder}.
     * @param builder source
     */
    DefaultStyleRangeDef(StyleRangeDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_fontSize = builder.m_fontSize;
        m_color = builder.m_color;
        m_start = builder.m_start;
        m_fontName = builder.m_fontName;
        m_fontStyle = builder.m_fontStyle;
        m_length = builder.m_length;

        m_exceptionTree = SimpleLoadExceptionTree.map(builder.m_exceptionalChildren);
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link StyleRangeDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultStyleRangeDef(StyleRangeDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new StyleRangeDefBuilder().build());
        
        m_fontSize = toCopy.getFontSize();
        m_color = toCopy.getColor();
        m_start = toCopy.getStart();
        m_fontName = toCopy.getFontName();
        m_fontStyle = toCopy.getFontStyle();
        m_length = toCopy.getLength();
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
    public DefaultStyleRangeDef(StyleRangeDef toCopy) {
        m_fontSize = toCopy.getFontSize();
        m_color = toCopy.getColor();
        m_start = toCopy.getStart();
        m_fontName = toCopy.getFontName();
        m_fontStyle = toCopy.getFontStyle();
        m_length = toCopy.getLength();
        
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing StyleRange
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultStyleRangeDef withException(StyleRangeDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(StyleRangeDef.Attribute attribute){
        if (m_exceptionTree instanceof LoadException) {
            return Optional.empty();
        }
        // if the tree is not a leaf, it is typed to StyleRangeDef.Attribute
        return ((LoadExceptionTree<StyleRangeDef.Attribute>)m_exceptionTree).getExceptionTree(attribute);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<Integer> getFontSize() {
        return m_fontSize;
    }
    @Override
    public Optional<Integer> getColor() {
        return m_color;
    }
    @Override
    public Integer getStart() {
        return m_start;
    }
    @Override
    public Optional<String> getFontName() {
        return m_fontName;
    }
    @Override
    public Optional<Integer> getFontStyle() {
        return m_fontStyle;
    }
    @Override
    public Integer getLength() {
        return m_length;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to fontSize.
     */
    @JsonIgnore
    public Optional<LoadException> getFontSizeSupplyException(){
    	return getLoadExceptionTree(StyleRangeDef.Attribute.FONT_SIZE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to color.
     */
    @JsonIgnore
    public Optional<LoadException> getColorSupplyException(){
    	return getLoadExceptionTree(StyleRangeDef.Attribute.COLOR).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to start.
     */
    @JsonIgnore
    public Optional<LoadException> getStartSupplyException(){
    	return getLoadExceptionTree(StyleRangeDef.Attribute.START).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to fontName.
     */
    @JsonIgnore
    public Optional<LoadException> getFontNameSupplyException(){
    	return getLoadExceptionTree(StyleRangeDef.Attribute.FONT_NAME).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to fontStyle.
     */
    @JsonIgnore
    public Optional<LoadException> getFontStyleSupplyException(){
    	return getLoadExceptionTree(StyleRangeDef.Attribute.FONT_STYLE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to length.
     */
    @JsonIgnore
    public Optional<LoadException> getLengthSupplyException(){
    	return getLoadExceptionTree(StyleRangeDef.Attribute.LENGTH).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultStyleRangeDef other = (DefaultStyleRangeDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_fontSize, other.m_fontSize);
        equalsBuilder.append(m_color, other.m_color);
        equalsBuilder.append(m_start, other.m_start);
        equalsBuilder.append(m_fontName, other.m_fontName);
        equalsBuilder.append(m_fontStyle, other.m_fontStyle);
        equalsBuilder.append(m_length, other.m_length);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_fontSize)
                .append(m_color)
                .append(m_start)
                .append(m_fontName)
                .append(m_fontStyle)
                .append(m_length)
                .toHashCode();
    }

} 
