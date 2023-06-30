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




// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.PageMarginsDef;
// for types that define enums
import org.knime.shared.workflow.def.PageMarginsDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * Page margins for report configuration
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class PageMarginsDefBuilder {

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
    Map<PageMarginsDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(PageMarginsDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Double m_top;
    

    Double m_right;
    

    Double m_bottom;
    

    Double m_left;
    

    /**
     * Create a new builder.
     */
    public PageMarginsDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public PageMarginsDefBuilder(final PageMarginsDef toCopy) {
        m_top = toCopy.getTop();
        m_right = toCopy.getRight();
        m_bottom = toCopy.getBottom();
        m_left = toCopy.getLeft();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for top
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param top 
     * @return this builder for fluent API.
     */ 
    public PageMarginsDefBuilder setTop(final Double top) {
        setTop(() -> top, top);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PageMarginsDef.Attribute.TOP)} will return true and and
     * {@code getExceptionalChildren().get(PageMarginsDef.Attribute.TOP)} will return the exception.
     * 
     * @param top see {@link PageMarginsDef#getTop}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTop(Double)
     */
    public PageMarginsDefBuilder setTop(final FallibleSupplier<Double> top, Double defaultValue) {
        java.util.Objects.requireNonNull(top, () -> "No supplier for top provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PageMarginsDef.Attribute.TOP);
        try {
            m_top = top.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_top = defaultValue;
            m_exceptionalChildren.put(PageMarginsDef.Attribute.TOP, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for right
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param right 
     * @return this builder for fluent API.
     */ 
    public PageMarginsDefBuilder setRight(final Double right) {
        setRight(() -> right, right);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PageMarginsDef.Attribute.RIGHT)} will return true and and
     * {@code getExceptionalChildren().get(PageMarginsDef.Attribute.RIGHT)} will return the exception.
     * 
     * @param right see {@link PageMarginsDef#getRight}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setRight(Double)
     */
    public PageMarginsDefBuilder setRight(final FallibleSupplier<Double> right, Double defaultValue) {
        java.util.Objects.requireNonNull(right, () -> "No supplier for right provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PageMarginsDef.Attribute.RIGHT);
        try {
            m_right = right.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_right = defaultValue;
            m_exceptionalChildren.put(PageMarginsDef.Attribute.RIGHT, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for bottom
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param bottom 
     * @return this builder for fluent API.
     */ 
    public PageMarginsDefBuilder setBottom(final Double bottom) {
        setBottom(() -> bottom, bottom);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PageMarginsDef.Attribute.BOTTOM)} will return true and and
     * {@code getExceptionalChildren().get(PageMarginsDef.Attribute.BOTTOM)} will return the exception.
     * 
     * @param bottom see {@link PageMarginsDef#getBottom}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBottom(Double)
     */
    public PageMarginsDefBuilder setBottom(final FallibleSupplier<Double> bottom, Double defaultValue) {
        java.util.Objects.requireNonNull(bottom, () -> "No supplier for bottom provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PageMarginsDef.Attribute.BOTTOM);
        try {
            m_bottom = bottom.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_bottom = defaultValue;
            m_exceptionalChildren.put(PageMarginsDef.Attribute.BOTTOM, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for left
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param left 
     * @return this builder for fluent API.
     */ 
    public PageMarginsDefBuilder setLeft(final Double left) {
        setLeft(() -> left, left);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PageMarginsDef.Attribute.LEFT)} will return true and and
     * {@code getExceptionalChildren().get(PageMarginsDef.Attribute.LEFT)} will return the exception.
     * 
     * @param left see {@link PageMarginsDef#getLeft}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLeft(Double)
     */
    public PageMarginsDefBuilder setLeft(final FallibleSupplier<Double> left, Double defaultValue) {
        java.util.Objects.requireNonNull(left, () -> "No supplier for left provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PageMarginsDef.Attribute.LEFT);
        try {
            m_left = left.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_left = defaultValue;
            m_exceptionalChildren.put(PageMarginsDef.Attribute.LEFT, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link PageMarginsDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultPageMarginsDef build() {
        
    	
        return new DefaultPageMarginsDef(this);
    }    

}
