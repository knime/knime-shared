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

import java.time.OffsetDateTime;

// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.AuthorInformationDef;
// for types that define enums
import org.knime.core.workflow.def.AuthorInformationDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * AuthorInformationDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class AuthorInformationDefBuilder {

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
    Map<AuthorInformationDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(AuthorInformationDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_authoredBy;
    

    OffsetDateTime m_authoredWhen;
    

    String m_lastEditedBy;
    

    OffsetDateTime m_lastEditedWhen;
    

    /**
     * Create a new builder.
     */
    public AuthorInformationDefBuilder() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for authoredBy
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param authoredBy 
     * @return this builder for fluent API.
     */ 
    public AuthorInformationDefBuilder setAuthoredBy(final String authoredBy) {
        setAuthoredBy(() -> authoredBy, authoredBy);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AuthorInformationDef.Attribute.AUTHORED_BY)} will return true and and
     * {@code getExceptionalChildren().get(AuthorInformationDef.Attribute.AUTHORED_BY)} will return the exception.
     * 
     * @param authoredBy see {@link AuthorInformationDef#getAuthoredBy}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAuthoredBy(String)
     */
    public AuthorInformationDefBuilder setAuthoredBy(final FallibleSupplier<String> authoredBy, String defaultValue) {
        java.util.Objects.requireNonNull(authoredBy, () -> "No supplier for authoredBy provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AuthorInformationDef.Attribute.AUTHORED_BY);
        try {
            m_authoredBy = authoredBy.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_authoredBy = defaultValue;
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.AUTHORED_BY, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for authoredWhen
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param authoredWhen 
     * @return this builder for fluent API.
     */ 
    public AuthorInformationDefBuilder setAuthoredWhen(final OffsetDateTime authoredWhen) {
        setAuthoredWhen(() -> authoredWhen, authoredWhen);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AuthorInformationDef.Attribute.AUTHORED_WHEN)} will return true and and
     * {@code getExceptionalChildren().get(AuthorInformationDef.Attribute.AUTHORED_WHEN)} will return the exception.
     * 
     * @param authoredWhen see {@link AuthorInformationDef#getAuthoredWhen}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAuthoredWhen(OffsetDateTime)
     */
    public AuthorInformationDefBuilder setAuthoredWhen(final FallibleSupplier<OffsetDateTime> authoredWhen, OffsetDateTime defaultValue) {
        java.util.Objects.requireNonNull(authoredWhen, () -> "No supplier for authoredWhen provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AuthorInformationDef.Attribute.AUTHORED_WHEN);
        try {
            m_authoredWhen = authoredWhen.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_authoredWhen = defaultValue;
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.AUTHORED_WHEN, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for lastEditedBy
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param lastEditedBy 
     * @return this builder for fluent API.
     */ 
    public AuthorInformationDefBuilder setLastEditedBy(final String lastEditedBy) {
        setLastEditedBy(() -> lastEditedBy, lastEditedBy);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AuthorInformationDef.Attribute.LAST_EDITED_BY)} will return true and and
     * {@code getExceptionalChildren().get(AuthorInformationDef.Attribute.LAST_EDITED_BY)} will return the exception.
     * 
     * @param lastEditedBy see {@link AuthorInformationDef#getLastEditedBy}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLastEditedBy(String)
     */
    public AuthorInformationDefBuilder setLastEditedBy(final FallibleSupplier<String> lastEditedBy, String defaultValue) {
        java.util.Objects.requireNonNull(lastEditedBy, () -> "No supplier for lastEditedBy provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AuthorInformationDef.Attribute.LAST_EDITED_BY);
        try {
            m_lastEditedBy = lastEditedBy.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_lastEditedBy = defaultValue;
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.LAST_EDITED_BY, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for lastEditedWhen
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param lastEditedWhen 
     * @return this builder for fluent API.
     */ 
    public AuthorInformationDefBuilder setLastEditedWhen(final OffsetDateTime lastEditedWhen) {
        setLastEditedWhen(() -> lastEditedWhen, lastEditedWhen);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AuthorInformationDef.Attribute.LAST_EDITED_WHEN)} will return true and and
     * {@code getExceptionalChildren().get(AuthorInformationDef.Attribute.LAST_EDITED_WHEN)} will return the exception.
     * 
     * @param lastEditedWhen see {@link AuthorInformationDef#getLastEditedWhen}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLastEditedWhen(OffsetDateTime)
     */
    public AuthorInformationDefBuilder setLastEditedWhen(final FallibleSupplier<OffsetDateTime> lastEditedWhen, OffsetDateTime defaultValue) {
        java.util.Objects.requireNonNull(lastEditedWhen, () -> "No supplier for lastEditedWhen provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(AuthorInformationDef.Attribute.LAST_EDITED_WHEN);
        try {
            m_lastEditedWhen = lastEditedWhen.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_lastEditedWhen = defaultValue;
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.LAST_EDITED_WHEN, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link AuthorInformationDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public FallibleAuthorInformationDef build() {
        
    	
        return new FallibleAuthorInformationDef(this);
    }    

}
