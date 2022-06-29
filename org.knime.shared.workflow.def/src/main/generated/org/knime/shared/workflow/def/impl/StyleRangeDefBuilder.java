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


// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.StyleRangeDef;
// for types that define enums
import org.knime.shared.workflow.def.StyleRangeDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.shared.workflow.def.FallibleSupplier;
import org.knime.shared.workflow.def.LoadException;
import org.knime.shared.workflow.def.LoadExceptionTree;
import org.knime.shared.workflow.def.LoadExceptionTreeProvider;

/**
 * Similar to a HTML span.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class StyleRangeDefBuilder {

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
    public StyleRangeDefBuilder strict(){
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
    Map<StyleRangeDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(StyleRangeDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Optional<Integer> m_fontSize = Optional.empty();
    

    Optional<Integer> m_color = Optional.empty();
    

    Integer m_start;
    

    Optional<String> m_fontName = Optional.empty();
    

    Optional<Integer> m_fontStyle = Optional.empty();
    

    Integer m_length;
    

    /**
     * Create a new builder.
     */
    public StyleRangeDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public StyleRangeDefBuilder(final StyleRangeDef toCopy) {
        setFontSize(toCopy.getFontSize().orElse(null));
        setColor(toCopy.getColor().orElse(null));
        setStart(toCopy.getStart());
        setFontName(toCopy.getFontName().orElse(null));
        setFontStyle(toCopy.getFontStyle().orElse(null));
        setLength(toCopy.getLength());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for fontSize
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param fontSize  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public StyleRangeDefBuilder setFontSize(final Integer fontSize) {
        setFontSize(() -> fontSize, fontSize);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.FONT_SIZE)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.FONT_SIZE)} will return the exception.
     * 
     * @param fontSize see {@link StyleRangeDef#getFontSize}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFontSize(Integer)
     */
    public StyleRangeDefBuilder setFontSize(final FallibleSupplier<Integer> fontSize) {
        setFontSize(fontSize, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.FONT_SIZE)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.FONT_SIZE)} will return the exception.
     * 
     * @param fontSize see {@link StyleRangeDef#getFontSize}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFontSize(Integer)
     */
    public StyleRangeDefBuilder setFontSize(final FallibleSupplier<Integer> fontSize, Integer defaultValue) {
        java.util.Objects.requireNonNull(fontSize, () -> "No supplier for fontSize provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StyleRangeDef.Attribute.FONT_SIZE);
        try {
            var supplied = fontSize.get();
            m_fontSize = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_fontSize = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(StyleRangeDef.Attribute.FONT_SIZE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for color
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param color  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public StyleRangeDefBuilder setColor(final Integer color) {
        setColor(() -> color, color);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.COLOR)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.COLOR)} will return the exception.
     * 
     * @param color see {@link StyleRangeDef#getColor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setColor(Integer)
     */
    public StyleRangeDefBuilder setColor(final FallibleSupplier<Integer> color) {
        setColor(color, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.COLOR)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.COLOR)} will return the exception.
     * 
     * @param color see {@link StyleRangeDef#getColor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setColor(Integer)
     */
    public StyleRangeDefBuilder setColor(final FallibleSupplier<Integer> color, Integer defaultValue) {
        java.util.Objects.requireNonNull(color, () -> "No supplier for color provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StyleRangeDef.Attribute.COLOR);
        try {
            var supplied = color.get();
            m_color = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_color = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(StyleRangeDef.Attribute.COLOR, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for start
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param start  
     * @return this builder for fluent API.
     */ 
    public StyleRangeDefBuilder setStart(final Integer start) {
        setStart(() -> start, start);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.START)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.START)} will return the exception.
     * 
     * @param start see {@link StyleRangeDef#getStart}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setStart(Integer)
     */
    public StyleRangeDefBuilder setStart(final FallibleSupplier<Integer> start, Integer defaultValue) {
        java.util.Objects.requireNonNull(start, () -> "No supplier for start provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StyleRangeDef.Attribute.START);
        try {
            var supplied = start.get();
            m_start = supplied;

            if(m_start == null) {
                throw new IllegalArgumentException("start is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_start = defaultValue;
            m_exceptionalChildren.put(StyleRangeDef.Attribute.START, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for fontName
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param fontName  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public StyleRangeDefBuilder setFontName(final String fontName) {
        setFontName(() -> fontName, fontName);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.FONT_NAME)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.FONT_NAME)} will return the exception.
     * 
     * @param fontName see {@link StyleRangeDef#getFontName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFontName(String)
     */
    public StyleRangeDefBuilder setFontName(final FallibleSupplier<String> fontName) {
        setFontName(fontName, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.FONT_NAME)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.FONT_NAME)} will return the exception.
     * 
     * @param fontName see {@link StyleRangeDef#getFontName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFontName(String)
     */
    public StyleRangeDefBuilder setFontName(final FallibleSupplier<String> fontName, String defaultValue) {
        java.util.Objects.requireNonNull(fontName, () -> "No supplier for fontName provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StyleRangeDef.Attribute.FONT_NAME);
        try {
            var supplied = fontName.get();
            m_fontName = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_fontName = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(StyleRangeDef.Attribute.FONT_NAME, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for fontStyle
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param fontStyle  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public StyleRangeDefBuilder setFontStyle(final Integer fontStyle) {
        setFontStyle(() -> fontStyle, fontStyle);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.FONT_STYLE)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.FONT_STYLE)} will return the exception.
     * 
     * @param fontStyle see {@link StyleRangeDef#getFontStyle}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFontStyle(Integer)
     */
    public StyleRangeDefBuilder setFontStyle(final FallibleSupplier<Integer> fontStyle) {
        setFontStyle(fontStyle, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.FONT_STYLE)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.FONT_STYLE)} will return the exception.
     * 
     * @param fontStyle see {@link StyleRangeDef#getFontStyle}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFontStyle(Integer)
     */
    public StyleRangeDefBuilder setFontStyle(final FallibleSupplier<Integer> fontStyle, Integer defaultValue) {
        java.util.Objects.requireNonNull(fontStyle, () -> "No supplier for fontStyle provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StyleRangeDef.Attribute.FONT_STYLE);
        try {
            var supplied = fontStyle.get();
            m_fontStyle = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_fontStyle = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(StyleRangeDef.Attribute.FONT_STYLE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for length
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param length  
     * @return this builder for fluent API.
     */ 
    public StyleRangeDefBuilder setLength(final Integer length) {
        setLength(() -> length, length);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StyleRangeDef.Attribute.LENGTH)} will return true and and
     * {@code getExceptionalChildren().get(StyleRangeDef.Attribute.LENGTH)} will return the exception.
     * 
     * @param length see {@link StyleRangeDef#getLength}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLength(Integer)
     */
    public StyleRangeDefBuilder setLength(final FallibleSupplier<Integer> length, Integer defaultValue) {
        java.util.Objects.requireNonNull(length, () -> "No supplier for length provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StyleRangeDef.Attribute.LENGTH);
        try {
            var supplied = length.get();
            m_length = supplied;

            if(m_length == null) {
                throw new IllegalArgumentException("length is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_length = defaultValue;
            m_exceptionalChildren.put(StyleRangeDef.Attribute.LENGTH, supplyException);
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
	 * @return the {@link StyleRangeDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultStyleRangeDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_start == null) setStart( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_length == null) setLength( null);
        
    	
        return new DefaultStyleRangeDef(this);
    }    

}
