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

import org.knime.shared.workflow.def.CoordinateDef;
import org.knime.shared.workflow.def.StyleRangeDef;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.AnnotationDataDef;
// for types that define enums
import org.knime.shared.workflow.def.AnnotationDataDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * AnnotationDataDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class AnnotationDataDefBuilder {

    /**
     * @see #strict()
     */
    boolean m__failFast = false;

    /**
     * Enable fail-fast mode.
     * In fail-fast mode, all load exceptions will be immediately thrown.
     * This can be when invoking a setter with an illegal argument (e.g., null or out of range) or 
     * when invoking {@link #build()} without previously having called the setter for a required field.
     * By default, fail-fast mode is off and all exceptions will be caught instead of thrown and collected for later reference into a LoadExceptionTree.
     * @return this builder for fluent API.
     */
    public AnnotationDataDefBuilder strict(){
        m__failFast = true;
        return this;
    }

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
    Optional<String> m_text = Optional.empty();
    

    CoordinateDef m_location;
    
    Optional<String> m_textAlignment = Optional.of("LEFT");
    

    Optional<Integer> m_borderSize = Optional.of(1);
    

    Optional<Integer> m_borderColor = Optional.empty();
    

    Optional<Integer> m_bgcolor = Optional.empty();
    

    Integer m_width;
    

    Optional<Integer> m_annotationVersion = Optional.empty();
    

    Optional<Integer> m_defaultFontSize = Optional.empty();
    

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.List<StyleRangeDef>> m_styles = Optional.of(java.util.List.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     * Setting elements individually is optional.
     */
    Optional<java.util.List<StyleRangeDef>> m_stylesIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setStyles these are added to m_styles in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.List<StyleRangeDef>> m_stylesBulkElements = Optional.empty();
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
     * @param text  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setText(final String text) {
        setText(() -> text, text);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.TEXT)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.TEXT)} will return the exception.
     * 
     * @param text see {@link AnnotationDataDef#getText}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setText(String)
     */
    public AnnotationDataDefBuilder setText(final FallibleSupplier<String> text) {
        setText(text, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = text.get();
            m_text = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_text = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.TEXT, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
            var supplied = location.get();
            m_location = supplied;

            if(m_location == null) {
                throw new IllegalArgumentException("location is required and must not be null.");
            }
            if (m_location instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_location).hasExceptions()) {
                m_exceptionalChildren.put(AnnotationDataDef.Attribute.LOCATION, (LoadExceptionTree<?>)m_location);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof LoadExceptionTreeProvider){
                var childTree = LoadExceptionTreeProvider.getTree(defaultValue);
                // if present, merge child tree with supply exception
                exceptionTree = childTree.hasExceptions() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree, supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_location = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.LOCATION, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for textAlignment
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param textAlignment  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setTextAlignment(final String textAlignment) {
        setTextAlignment(() -> textAlignment, textAlignment);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.TEXT_ALIGNMENT)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.TEXT_ALIGNMENT)} will return the exception.
     * 
     * @param textAlignment see {@link AnnotationDataDef#getTextAlignment}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTextAlignment(String)
     */
    public AnnotationDataDefBuilder setTextAlignment(final FallibleSupplier<String> textAlignment) {
        setTextAlignment(textAlignment, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = textAlignment.get();
            m_textAlignment = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_textAlignment = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.TEXT_ALIGNMENT, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for borderSize
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param borderSize  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setBorderSize(final Integer borderSize) {
        setBorderSize(() -> borderSize, borderSize);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.BORDER_SIZE)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.BORDER_SIZE)} will return the exception.
     * 
     * @param borderSize see {@link AnnotationDataDef#getBorderSize}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBorderSize(Integer)
     */
    public AnnotationDataDefBuilder setBorderSize(final FallibleSupplier<Integer> borderSize) {
        setBorderSize(borderSize, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = borderSize.get();
            m_borderSize = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_borderSize = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.BORDER_SIZE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for borderColor
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param borderColor  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setBorderColor(final Integer borderColor) {
        setBorderColor(() -> borderColor, borderColor);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.BORDER_COLOR)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.BORDER_COLOR)} will return the exception.
     * 
     * @param borderColor see {@link AnnotationDataDef#getBorderColor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBorderColor(Integer)
     */
    public AnnotationDataDefBuilder setBorderColor(final FallibleSupplier<Integer> borderColor) {
        setBorderColor(borderColor, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = borderColor.get();
            m_borderColor = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_borderColor = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.BORDER_COLOR, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for bgcolor
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param bgcolor Background color This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setBgcolor(final Integer bgcolor) {
        setBgcolor(() -> bgcolor, bgcolor);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.BGCOLOR)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.BGCOLOR)} will return the exception.
     * 
     * @param bgcolor see {@link AnnotationDataDef#getBgcolor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBgcolor(Integer)
     */
    public AnnotationDataDefBuilder setBgcolor(final FallibleSupplier<Integer> bgcolor) {
        setBgcolor(bgcolor, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = bgcolor.get();
            m_bgcolor = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_bgcolor = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.BGCOLOR, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
            var supplied = width.get();
            m_width = supplied;

            if(m_width == null) {
                throw new IllegalArgumentException("width is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_width = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.WIDTH, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for annotationVersion
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param annotationVersion  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setAnnotationVersion(final Integer annotationVersion) {
        setAnnotationVersion(() -> annotationVersion, annotationVersion);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.ANNOTATION_VERSION)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.ANNOTATION_VERSION)} will return the exception.
     * 
     * @param annotationVersion see {@link AnnotationDataDef#getAnnotationVersion}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotationVersion(Integer)
     */
    public AnnotationDataDefBuilder setAnnotationVersion(final FallibleSupplier<Integer> annotationVersion) {
        setAnnotationVersion(annotationVersion, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = annotationVersion.get();
            m_annotationVersion = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_annotationVersion = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.ANNOTATION_VERSION, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for defaultFontSize
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param defaultFontSize  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AnnotationDataDefBuilder setDefaultFontSize(final Integer defaultFontSize) {
        setDefaultFontSize(() -> defaultFontSize, defaultFontSize);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE)} will return true and and
     * {@code getExceptionalChildren().get(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE)} will return the exception.
     * 
     * @param defaultFontSize see {@link AnnotationDataDef#getDefaultFontSize}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDefaultFontSize(Integer)
     */
    public AnnotationDataDefBuilder setDefaultFontSize(final FallibleSupplier<Integer> defaultFontSize) {
        setDefaultFontSize(defaultFontSize, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = defaultFontSize.get();
            m_defaultFontSize = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_defaultFontSize = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.DEFAULT_FONT_SIZE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = styles.get();
            m_styles = Optional.ofNullable(supplied);
            // we set m_styles in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_stylesBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_stylesContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
        // we're always adding an element (to have something to link the exception to), so make sure the list is present
        if(m_stylesIndividualElements.isEmpty()) m_stylesIndividualElements = Optional.of(new java.util.ArrayList<>());
        StyleRangeDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultStyleRangeDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_stylesIndividualElements.get().add(toAdd);
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
            var supplied = height.get();
            m_height = supplied;

            if(m_height == null) {
                throw new IllegalArgumentException("height is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_height = defaultValue;
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.HEIGHT, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
    public DefaultAnnotationDataDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_location == null) setLocation( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_width == null) setWidth( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_height == null) setHeight( null);
        
    	
        // if bulk elements are present, add them to individual elements
        if(m_stylesBulkElements.isPresent()){
            if(m_stylesIndividualElements.isEmpty()) {
                m_stylesIndividualElements = Optional.of(new java.util.ArrayList<>());
            }
            m_stylesIndividualElements.get().addAll(m_stylesBulkElements.get());    
        }
        m_styles = m_stylesIndividualElements;        
        
                
        var stylesLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_styles.orElse(new java.util.ArrayList<>()), m_stylesContainerSupplyException);
        if(stylesLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(AnnotationDataDef.Attribute.STYLES, stylesLoadExceptionTree);
        }
        
        return new DefaultAnnotationDataDef(this);
    }    

}
