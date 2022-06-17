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

import java.time.OffsetDateTime;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.AuthorInformationDef;
// for types that define enums
import org.knime.shared.workflow.def.AuthorInformationDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * AuthorInformationDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class AuthorInformationDefBuilder {

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
    public AuthorInformationDefBuilder strict(){
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
    Map<AuthorInformationDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(AuthorInformationDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_authoredBy;
    

    OffsetDateTime m_authoredWhen;
    

    Optional<String> m_lastEditedBy = Optional.empty();
    

    Optional<OffsetDateTime> m_lastEditedWhen = Optional.empty();
    

    /**
     * Create a new builder.
     */
    public AuthorInformationDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public AuthorInformationDefBuilder(final AuthorInformationDef toCopy) {
        m_authoredBy = toCopy.getAuthoredBy();
        m_authoredWhen = toCopy.getAuthoredWhen();
        m_lastEditedBy = toCopy.getLastEditedBy();
        m_lastEditedWhen = toCopy.getLastEditedWhen();
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
            var supplied = authoredBy.get();
            m_authoredBy = supplied;

            if(m_authoredBy == null) {
                throw new IllegalArgumentException("authoredBy is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_authoredBy = defaultValue;
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.AUTHORED_BY, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
            var supplied = authoredWhen.get();
            m_authoredWhen = supplied;

            if(m_authoredWhen == null) {
                throw new IllegalArgumentException("authoredWhen is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_authoredWhen = defaultValue;
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.AUTHORED_WHEN, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for lastEditedBy
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param lastEditedBy  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AuthorInformationDefBuilder setLastEditedBy(final String lastEditedBy) {
        setLastEditedBy(() -> lastEditedBy, lastEditedBy);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AuthorInformationDef.Attribute.LAST_EDITED_BY)} will return true and and
     * {@code getExceptionalChildren().get(AuthorInformationDef.Attribute.LAST_EDITED_BY)} will return the exception.
     * 
     * @param lastEditedBy see {@link AuthorInformationDef#getLastEditedBy}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLastEditedBy(String)
     */
    public AuthorInformationDefBuilder setLastEditedBy(final FallibleSupplier<String> lastEditedBy) {
        setLastEditedBy(lastEditedBy, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = lastEditedBy.get();
            m_lastEditedBy = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_lastEditedBy = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.LAST_EDITED_BY, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for lastEditedWhen
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param lastEditedWhen  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public AuthorInformationDefBuilder setLastEditedWhen(final OffsetDateTime lastEditedWhen) {
        setLastEditedWhen(() -> lastEditedWhen, lastEditedWhen);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(AuthorInformationDef.Attribute.LAST_EDITED_WHEN)} will return true and and
     * {@code getExceptionalChildren().get(AuthorInformationDef.Attribute.LAST_EDITED_WHEN)} will return the exception.
     * 
     * @param lastEditedWhen see {@link AuthorInformationDef#getLastEditedWhen}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLastEditedWhen(OffsetDateTime)
     */
    public AuthorInformationDefBuilder setLastEditedWhen(final FallibleSupplier<OffsetDateTime> lastEditedWhen) {
        setLastEditedWhen(lastEditedWhen, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            var supplied = lastEditedWhen.get();
            m_lastEditedWhen = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_lastEditedWhen = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(AuthorInformationDef.Attribute.LAST_EDITED_WHEN, supplyException);
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
	 * @return the {@link AuthorInformationDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultAuthorInformationDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_authoredBy == null) setAuthoredBy( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_authoredWhen == null) setAuthoredWhen( null);
        
    	
        return new DefaultAuthorInformationDef(this);
    }    

}
