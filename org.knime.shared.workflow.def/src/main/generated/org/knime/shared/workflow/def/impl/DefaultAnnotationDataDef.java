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
import org.knime.shared.workflow.def.StyleRangeDef;

import org.knime.shared.workflow.def.AnnotationDataDef;



// for types that define enums
import org.knime.shared.workflow.def.AnnotationDataDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.shared.workflow.def.LoadException;
import org.knime.shared.workflow.def.LoadExceptionTree;
import org.knime.shared.workflow.def.LoadExceptionTreeProvider;
import org.knime.shared.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultAnnotationDataDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultAnnotationDataDef implements AnnotationDataDef, LoadExceptionTreeProvider {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<AnnotationDataDef.Attribute> instance. */
    private final LoadExceptionTree<?> m_exceptionTree;

    @JsonProperty("text")
    protected Optional<String> m_text;

    @JsonProperty("location")
    protected CoordinateDef m_location;

    @JsonProperty("textAlignment")
    protected Optional<String> m_textAlignment;

    @JsonProperty("borderSize")
    protected Optional<Integer> m_borderSize;

    @JsonProperty("borderColor")
    protected Optional<Integer> m_borderColor;

    /** 
     * Background color 
     */
    @JsonProperty("bgcolor")
    protected Optional<Integer> m_bgcolor;

    @JsonProperty("width")
    protected Integer m_width;

    @JsonProperty("annotationVersion")
    protected Optional<Integer> m_annotationVersion;

    @JsonProperty("defaultFontSize")
    protected Optional<Integer> m_defaultFontSize;

    @JsonProperty("styles")
    protected Optional<java.util.List<StyleRangeDef>> m_styles;

    @JsonProperty("height")
    protected Integer m_height;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultAnnotationDataDef() {
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    /**
     * Builder constructor. Only for use in {@link AnnotationDataDefBuilder}.
     * @param builder source
     */
    DefaultAnnotationDataDef(AnnotationDataDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_text = builder.m_text;
        m_location = builder.m_location;
        m_textAlignment = builder.m_textAlignment;
        m_borderSize = builder.m_borderSize;
        m_borderColor = builder.m_borderColor;
        m_bgcolor = builder.m_bgcolor;
        m_width = builder.m_width;
        m_annotationVersion = builder.m_annotationVersion;
        m_defaultFontSize = builder.m_defaultFontSize;
        m_styles = builder.m_styles;
        m_height = builder.m_height;

        m_exceptionTree = SimpleLoadExceptionTree.map(builder.m_exceptionalChildren);
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link AnnotationDataDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultAnnotationDataDef(AnnotationDataDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new AnnotationDataDefBuilder().build());
        
        m_text = toCopy.getText();
        m_location = toCopy.getLocation();
        m_textAlignment = toCopy.getTextAlignment();
        m_borderSize = toCopy.getBorderSize();
        m_borderColor = toCopy.getBorderColor();
        m_bgcolor = toCopy.getBgcolor();
        m_width = toCopy.getWidth();
        m_annotationVersion = toCopy.getAnnotationVersion();
        m_defaultFontSize = toCopy.getDefaultFontSize();
        m_styles = toCopy.getStyles();
        m_height = toCopy.getHeight();
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
    public DefaultAnnotationDataDef(AnnotationDataDef toCopy) {
        m_text = toCopy.getText();
        m_location = toCopy.getLocation();
        m_textAlignment = toCopy.getTextAlignment();
        m_borderSize = toCopy.getBorderSize();
        m_borderColor = toCopy.getBorderColor();
        m_bgcolor = toCopy.getBgcolor();
        m_width = toCopy.getWidth();
        m_annotationVersion = toCopy.getAnnotationVersion();
        m_defaultFontSize = toCopy.getDefaultFontSize();
        m_styles = toCopy.getStyles();
        m_height = toCopy.getHeight();
        
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing AnnotationData
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultAnnotationDataDef withException(AnnotationDataDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(AnnotationDataDef.Attribute attribute){
        if (m_exceptionTree instanceof LoadException) {
            return Optional.empty();
        }
        // if the tree is not a leaf, it is typed to AnnotationDataDef.Attribute
        return ((LoadExceptionTree<AnnotationDataDef.Attribute>)m_exceptionTree).getExceptionTree(attribute);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<String> getText() {
        return m_text;
    }
    @Override
    public CoordinateDef getLocation() {
        return m_location;
    }
    @Override
    public Optional<String> getTextAlignment() {
        return m_textAlignment;
    }
    @Override
    public Optional<Integer> getBorderSize() {
        return m_borderSize;
    }
    @Override
    public Optional<Integer> getBorderColor() {
        return m_borderColor;
    }
    @Override
    public Optional<Integer> getBgcolor() {
        return m_bgcolor;
    }
    @Override
    public Integer getWidth() {
        return m_width;
    }
    @Override
    public Optional<Integer> getAnnotationVersion() {
        return m_annotationVersion;
    }
    @Override
    public Optional<Integer> getDefaultFontSize() {
        return m_defaultFontSize;
    }
    @Override
    public Optional<java.util.List<StyleRangeDef>> getStyles() {
        return m_styles;
    }
    @Override
    public Integer getHeight() {
        return m_height;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to text.
     */
    @JsonIgnore
    public Optional<LoadException> getTextSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.TEXT).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to location.
     */
    @JsonIgnore
    public Optional<LoadException> getLocationSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.LOCATION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link CoordinateDef} returned by {@link #getLocation()}, this
     * returns the location as DefaultCoordinateDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultCoordinateDef> getFaultyLocation(){
    	final var location = getLocation(); 
        if(LoadExceptionTreeProvider.hasExceptions(location)) {
            return Optional.of((DefaultCoordinateDef)location);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to textAlignment.
     */
    @JsonIgnore
    public Optional<LoadException> getTextAlignmentSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.TEXT_ALIGNMENT).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to borderSize.
     */
    @JsonIgnore
    public Optional<LoadException> getBorderSizeSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.BORDER_SIZE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to borderColor.
     */
    @JsonIgnore
    public Optional<LoadException> getBorderColorSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.BORDER_COLOR).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to bgcolor.
     */
    @JsonIgnore
    public Optional<LoadException> getBgcolorSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.BGCOLOR).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to width.
     */
    @JsonIgnore
    public Optional<LoadException> getWidthSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.WIDTH).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to annotationVersion.
     */
    @JsonIgnore
    public Optional<LoadException> getAnnotationVersionSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.ANNOTATION_VERSION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to defaultFontSize.
     */
    @JsonIgnore
    public Optional<LoadException> getDefaultFontSizeSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to styles.
     */
    @JsonIgnore
    public Optional<LoadException> getStylesSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.STYLES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getStylesExceptionTree(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.STYLES).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to height.
     */
    @JsonIgnore
    public Optional<LoadException> getHeightSupplyException(){
    	return getLoadExceptionTree(AnnotationDataDef.Attribute.HEIGHT).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultAnnotationDataDef other = (DefaultAnnotationDataDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_text, other.m_text);
        equalsBuilder.append(m_location, other.m_location);
        equalsBuilder.append(m_textAlignment, other.m_textAlignment);
        equalsBuilder.append(m_borderSize, other.m_borderSize);
        equalsBuilder.append(m_borderColor, other.m_borderColor);
        equalsBuilder.append(m_bgcolor, other.m_bgcolor);
        equalsBuilder.append(m_width, other.m_width);
        equalsBuilder.append(m_annotationVersion, other.m_annotationVersion);
        equalsBuilder.append(m_defaultFontSize, other.m_defaultFontSize);
        equalsBuilder.append(m_styles, other.m_styles);
        equalsBuilder.append(m_height, other.m_height);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_text)
                .append(m_location)
                .append(m_textAlignment)
                .append(m_borderSize)
                .append(m_borderColor)
                .append(m_bgcolor)
                .append(m_width)
                .append(m_annotationVersion)
                .append(m_defaultFontSize)
                .append(m_styles)
                .append(m_height)
                .toHashCode();
    }

} 
