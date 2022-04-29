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

import org.knime.core.workflow.def.CoordinateDef;
import org.knime.core.workflow.def.StyleRangeDef;

// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.AnnotationDataDef;
// for types that define enums
import org.knime.core.workflow.def.AnnotationDataDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * AnnotationDataDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class AnnotationDataDefBuilder {

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
    Map<AnnotationDataDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(AnnotationDataDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_text;
    

    CoordinateDef m_location;
    
    String m_textAlignment = "LEFT";
    

    Integer m_borderSize;
    

    Integer m_borderColor;
    

    Integer m_bgcolor;
    

    Integer m_width;
    

    Integer m_annotationVersion;
    

    Integer m_defaultFontSize;
    

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<StyleRangeDef> m_styles = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setStyles these are added to m_styles in build */
    private java.util.List<StyleRangeDef> m_stylesBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_stylesContainerSupplyException; 
    
    Integer m_height;
    

    /**
     * Create a new builder.
     */
    public AnnotationDataDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public AnnotationDataDefBuilder(final AnnotationDataDef toCopy) {
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
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for text
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param text 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setText(final String text) {
        setText(() -> text, text);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.TEXT)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.TEXT)} will return the exception.
     * 
     * @param text see {@link AnnotationDataDef#getText}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setText(String)
     */
    public AnnotationDataDefBuilder setText(final FallibleSupplier<String> text, String defaultValue) {
        java.util.Objects.requireNonNull(text, () -> "No supplier for text provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.TEXT);
        try {
            m_text = text.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_text = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.TEXT, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for location
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param location 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setLocation(final CoordinateDef location) {
        setLocation(() -> location, location);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.LOCATION)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.LOCATION)} will return the exception.
     * 
     * @param location see {@link AnnotationDataDef#getLocation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocation(CoordinateDef)
     */
    public AnnotationDataDefBuilder setLocation(final FallibleSupplier<CoordinateDef> location, CoordinateDef defaultValue) {
        java.util.Objects.requireNonNull(location, () -> "No supplier for location provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.LOCATION);
        try {
            m_location = location.get();
            if (m_location instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_location).hasExceptions()) {
                m_exceptionalChildren.put(AnnotationDataDef.Attribute.LOCATION, (LoadExceptionTree<?>)m_location);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleCoordinateDef){
                var childTree = ((FallibleCoordinateDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_location = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.LOCATION, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for textAlignment
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param textAlignment 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setTextAlignment(final String textAlignment) {
        setTextAlignment(() -> textAlignment, textAlignment);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.TEXT_ALIGNMENT)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.TEXT_ALIGNMENT)} will return the exception.
     * 
     * @param textAlignment see {@link AnnotationDataDef#getTextAlignment}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTextAlignment(String)
     */
    public AnnotationDataDefBuilder setTextAlignment(final FallibleSupplier<String> textAlignment, String defaultValue) {
        java.util.Objects.requireNonNull(textAlignment, () -> "No supplier for textAlignment provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.TEXT_ALIGNMENT);
        try {
            m_textAlignment = textAlignment.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_textAlignment = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.TEXT_ALIGNMENT, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for borderSize
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param borderSize 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setBorderSize(final Integer borderSize) {
        setBorderSize(() -> borderSize, borderSize);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.BORDER_SIZE)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.BORDER_SIZE)} will return the exception.
     * 
     * @param borderSize see {@link AnnotationDataDef#getBorderSize}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBorderSize(Integer)
     */
    public AnnotationDataDefBuilder setBorderSize(final FallibleSupplier<Integer> borderSize, Integer defaultValue) {
        java.util.Objects.requireNonNull(borderSize, () -> "No supplier for borderSize provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.BORDER_SIZE);
        try {
            m_borderSize = borderSize.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_borderSize = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.BORDER_SIZE, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for borderColor
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param borderColor 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setBorderColor(final Integer borderColor) {
        setBorderColor(() -> borderColor, borderColor);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.BORDER_COLOR)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.BORDER_COLOR)} will return the exception.
     * 
     * @param borderColor see {@link AnnotationDataDef#getBorderColor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBorderColor(Integer)
     */
    public AnnotationDataDefBuilder setBorderColor(final FallibleSupplier<Integer> borderColor, Integer defaultValue) {
        java.util.Objects.requireNonNull(borderColor, () -> "No supplier for borderColor provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.BORDER_COLOR);
        try {
            m_borderColor = borderColor.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_borderColor = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.BORDER_COLOR, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for bgcolor
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param bgcolor Background color
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setBgcolor(final Integer bgcolor) {
        setBgcolor(() -> bgcolor, bgcolor);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.BGCOLOR)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.BGCOLOR)} will return the exception.
     * 
     * @param bgcolor see {@link AnnotationDataDef#getBgcolor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBgcolor(Integer)
     */
    public AnnotationDataDefBuilder setBgcolor(final FallibleSupplier<Integer> bgcolor, Integer defaultValue) {
        java.util.Objects.requireNonNull(bgcolor, () -> "No supplier for bgcolor provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.BGCOLOR);
        try {
            m_bgcolor = bgcolor.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_bgcolor = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.BGCOLOR, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for width
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param width 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setWidth(final Integer width) {
        setWidth(() -> width, width);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.WIDTH)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.WIDTH)} will return the exception.
     * 
     * @param width see {@link AnnotationDataDef#getWidth}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWidth(Integer)
     */
    public AnnotationDataDefBuilder setWidth(final FallibleSupplier<Integer> width, Integer defaultValue) {
        java.util.Objects.requireNonNull(width, () -> "No supplier for width provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.WIDTH);
        try {
            m_width = width.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_width = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.WIDTH, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for annotationVersion
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param annotationVersion 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setAnnotationVersion(final Integer annotationVersion) {
        setAnnotationVersion(() -> annotationVersion, annotationVersion);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.ANNOTATION_VERSION)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.ANNOTATION_VERSION)} will return the exception.
     * 
     * @param annotationVersion see {@link AnnotationDataDef#getAnnotationVersion}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotationVersion(Integer)
     */
    public AnnotationDataDefBuilder setAnnotationVersion(final FallibleSupplier<Integer> annotationVersion, Integer defaultValue) {
        java.util.Objects.requireNonNull(annotationVersion, () -> "No supplier for annotationVersion provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.ANNOTATION_VERSION);
        try {
            m_annotationVersion = annotationVersion.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_annotationVersion = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.ANNOTATION_VERSION, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for defaultFontSize
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param defaultFontSize 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setDefaultFontSize(final Integer defaultFontSize) {
        setDefaultFontSize(() -> defaultFontSize, defaultFontSize);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE)} will return the exception.
     * 
     * @param defaultFontSize see {@link AnnotationDataDef#getDefaultFontSize}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDefaultFontSize(Integer)
     */
    public AnnotationDataDefBuilder setDefaultFontSize(final FallibleSupplier<Integer> defaultFontSize, Integer defaultValue) {
        java.util.Objects.requireNonNull(defaultFontSize, () -> "No supplier for defaultFontSize provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE);
        try {
            m_defaultFontSize = defaultFontSize.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_defaultFontSize = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for styles
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the styles list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToStyles} will be inserted at the end of the list.
     * @param styles 
     * @return this for fluent API
     */
    public AnnotationDataDefBuilder setStyles(final java.util.List<StyleRangeDef> styles) {
        setStyles(() -> styles);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the styles list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the styles list). 
     * {@code hasExceptions(AnnotationDataDef.Attribute.STYLES)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.STYLES)} will return the exception.
     * 
     * @param styles see {@link AnnotationDataDef#getStyles}
     * 
     * @return this builder for fluent API.
     * @see #setStyles(java.util.List<StyleRangeDef>)
     */
    public AnnotationDataDefBuilder setStyles(final FallibleSupplier<java.util.List<StyleRangeDef>> styles) {
        java.util.Objects.requireNonNull(styles, () -> "No supplier for styles provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.STYLES);
        try {
            m_stylesBulkElements = styles.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_stylesBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_stylesContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the styles list
     * @return this builder for fluent API
     */
    public AnnotationDataDefBuilder addToStyles(StyleRangeDef value){
    	addToStyles(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link AnnotationDataDef#getStyles}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the styles list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public AnnotationDataDefBuilder addToStyles(FallibleSupplier<StyleRangeDef> value, StyleRangeDef defaultValue) {
        StyleRangeDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new FallibleStyleRangeDef(defaultValue, supplyException);
        }
        m_styles.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for height
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param height 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setHeight(final Integer height) {
        setHeight(() -> height, height);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.HEIGHT)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.HEIGHT)} will return the exception.
     * 
     * @param height see {@link AnnotationDataDef#getHeight}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHeight(Integer)
     */
    public AnnotationDataDefBuilder setHeight(final FallibleSupplier<Integer> height, Integer defaultValue) {
        java.util.Objects.requireNonNull(height, () -> "No supplier for height provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AnnotationDataDef.Attribute.HEIGHT);
        try {
            m_height = height.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_height = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.HEIGHT, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link AnnotationDataDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public FallibleAnnotationDataDef build() {
        
    	
        // contains the elements set with #setStyles (those added with #addToStyles have already been inserted into m_styles)
        m_stylesBulkElements = java.util.Objects.requireNonNullElse(m_stylesBulkElements, java.util.List.of());
        m_styles.addAll(0, m_stylesBulkElements);
                
        var stylesLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_styles, m_stylesContainerSupplyException);
        if(stylesLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.STYLES, stylesLoadExceptionTree);
        }
        
        return new FallibleAnnotationDataDef(this);
    }    

}
