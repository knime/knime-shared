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

import java.time.OffsetDateTime;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.TemplateInfoDef;
// for types that define enums
import org.knime.shared.workflow.def.TemplateInfoDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * For metanodes and components that have been inserted into a workflow by inserting a template metanode/component. The link allows to fetch the inserted content again for updating. 
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class TemplateInfoDefBuilder {

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
    Map<TemplateInfoDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(TemplateInfoDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_uri;
    

    OffsetDateTime m_updatedAt;
    

    /**
     * Create a new builder.
     */
    public TemplateInfoDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public TemplateInfoDefBuilder(final TemplateInfoDef toCopy) {
        m_uri = toCopy.getUri();
        m_updatedAt = toCopy.getUpdatedAt();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for uri
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param uri How to resolve the linked Component/Metanode
     * @return this builder for fluent API.
     */ 
    public TemplateInfoDefBuilder setUri(final String uri) {
        setUri(() -> uri, uri);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(TemplateInfoDef.Attribute.URI)} will return true and and
     * {@code getExceptionalChildren().get(TemplateInfoDef.Attribute.URI)} will return the exception.
     * 
     * @param uri see {@link TemplateInfoDef#getUri}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setUri(String)
     */
    public TemplateInfoDefBuilder setUri(final FallibleSupplier<String> uri, String defaultValue) {
        java.util.Objects.requireNonNull(uri, () -> "No supplier for uri provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(TemplateInfoDef.Attribute.URI);
        try {
            m_uri = uri.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_uri = defaultValue;
            m_exceptionalChildren.put(TemplateInfoDef.Attribute.URI, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for updatedAt
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param updatedAt When the template was last updated. If this date is older than the last changed date of the component or metanode, an update is available.
     * @return this builder for fluent API.
     */ 
    public TemplateInfoDefBuilder setUpdatedAt(final OffsetDateTime updatedAt) {
        setUpdatedAt(() -> updatedAt, updatedAt);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(TemplateInfoDef.Attribute.UPDATED_AT)} will return true and and
     * {@code getExceptionalChildren().get(TemplateInfoDef.Attribute.UPDATED_AT)} will return the exception.
     * 
     * @param updatedAt see {@link TemplateInfoDef#getUpdatedAt}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setUpdatedAt(OffsetDateTime)
     */
    public TemplateInfoDefBuilder setUpdatedAt(final FallibleSupplier<OffsetDateTime> updatedAt, OffsetDateTime defaultValue) {
        java.util.Objects.requireNonNull(updatedAt, () -> "No supplier for updatedAt provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(TemplateInfoDef.Attribute.UPDATED_AT);
        try {
            m_updatedAt = updatedAt.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_updatedAt = defaultValue;
            m_exceptionalChildren.put(TemplateInfoDef.Attribute.UPDATED_AT, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link TemplateInfoDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultTemplateInfoDef build() {
        
    	
        return new DefaultTemplateInfoDef(this);
    }    

}
