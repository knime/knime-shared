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
import org.knime.shared.workflow.def.CredentialPlaceholderDef;
// for types that define enums
import org.knime.shared.workflow.def.CredentialPlaceholderDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * This is referred to as Workflow Credentials in KNIME. It allows defining placeholders for credentials, such as for accessing a database. When configuring a node that requires database access, the placeholder can be specified. When the workflow is executed, the user will be asked to provide the credentials fill in the placeholder.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class CredentialPlaceholderDefBuilder {

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
    Map<CredentialPlaceholderDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(CredentialPlaceholderDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_name;
    

    String m_login;
    

    /**
     * Create a new builder.
     */
    public CredentialPlaceholderDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public CredentialPlaceholderDefBuilder(final CredentialPlaceholderDef toCopy) {
        m_name = toCopy.getName();
        m_login = toCopy.getLogin();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for name
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param name Identifies this placeholder. Must be unique in the scope of this workflow project.
     * @return this builder for fluent API.
     */ 
    public CredentialPlaceholderDefBuilder setName(final String name) {
        setName(() -> name, name);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(CredentialPlaceholderDef.Attribute.NAME)} will return true and and
     * {@code getExceptionalChildren().get(CredentialPlaceholderDef.Attribute.NAME)} will return the exception.
     * 
     * @param name see {@link CredentialPlaceholderDef#getName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setName(String)
     */
    public CredentialPlaceholderDefBuilder setName(final FallibleSupplier<String> name, String defaultValue) {
        java.util.Objects.requireNonNull(name, () -> "No supplier for name provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(CredentialPlaceholderDef.Attribute.NAME);
        try {
            m_name = name.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_name = defaultValue;
            m_exceptionalChildren.put(CredentialPlaceholderDef.Attribute.NAME, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for login
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param login The login name that is passed to the secured system.
     * @return this builder for fluent API.
     */ 
    public CredentialPlaceholderDefBuilder setLogin(final String login) {
        setLogin(() -> login, login);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(CredentialPlaceholderDef.Attribute.LOGIN)} will return true and and
     * {@code getExceptionalChildren().get(CredentialPlaceholderDef.Attribute.LOGIN)} will return the exception.
     * 
     * @param login see {@link CredentialPlaceholderDef#getLogin}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLogin(String)
     */
    public CredentialPlaceholderDefBuilder setLogin(final FallibleSupplier<String> login, String defaultValue) {
        java.util.Objects.requireNonNull(login, () -> "No supplier for login provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(CredentialPlaceholderDef.Attribute.LOGIN);
        try {
            m_login = login.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_login = defaultValue;
            m_exceptionalChildren.put(CredentialPlaceholderDef.Attribute.LOGIN, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link CredentialPlaceholderDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultCredentialPlaceholderDef build() {
        
    	
        return new DefaultCredentialPlaceholderDef(this);
    }    

}
