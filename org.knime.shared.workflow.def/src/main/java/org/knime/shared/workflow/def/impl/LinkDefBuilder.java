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
import org.knime.shared.workflow.def.LinkDef;
// for types that define enums
import org.knime.shared.workflow.def.LinkDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * LinkDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 * @since 5.1
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class LinkDefBuilder {

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
    Map<LinkDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(LinkDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_url;
    

    String m_text;
    

    /**
     * Create a new builder.
     */
    public LinkDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public LinkDefBuilder(final LinkDef toCopy) {
        m_url = toCopy.getUrl();
        m_text = toCopy.getText();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for url
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param url 
     * @return this builder for fluent API.
     */ 
    public LinkDefBuilder setUrl(final String url) {
        setUrl(() -> url, url);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(LinkDef.Attribute.URL)} will return true and and
     * {@code getExceptionalChildren().get(LinkDef.Attribute.URL)} will return the exception.
     * 
     * @param url see {@link LinkDef#getUrl}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setUrl(String)
     */
    public LinkDefBuilder setUrl(final FallibleSupplier<String> url, String defaultValue) {
        java.util.Objects.requireNonNull(url, () -> "No supplier for url provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(LinkDef.Attribute.URL);
        try {
            m_url = url.get();

            if(m_url == null) {
                throw new IllegalArgumentException("url is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_url = defaultValue;
            m_exceptionalChildren.put(LinkDef.Attribute.URL, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for text
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param text 
     * @return this builder for fluent API.
     */ 
    public LinkDefBuilder setText(final String text) {
        setText(() -> text, text);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(LinkDef.Attribute.TEXT)} will return true and and
     * {@code getExceptionalChildren().get(LinkDef.Attribute.TEXT)} will return the exception.
     * 
     * @param text see {@link LinkDef#getText}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setText(String)
     */
    public LinkDefBuilder setText(final FallibleSupplier<String> text, String defaultValue) {
        java.util.Objects.requireNonNull(text, () -> "No supplier for text provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(LinkDef.Attribute.TEXT);
        try {
            m_text = text.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_text = defaultValue;
            m_exceptionalChildren.put(LinkDef.Attribute.TEXT, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link LinkDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultLinkDef build() {
        
        // in case the setter has never been called, the field is still null, but no load exception was recorded. Do that now.
        if(m_url == null) setUrl(null);
        
    	
        return new DefaultLinkDef(this);
    }    

}
