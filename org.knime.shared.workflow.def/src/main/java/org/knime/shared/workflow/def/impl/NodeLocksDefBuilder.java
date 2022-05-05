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
import org.knime.shared.workflow.def.NodeLocksDef;
// for types that define enums
import org.knime.shared.workflow.def.NodeLocksDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * Any kind of node (native, meta, component) can be locked to restrict the user&#39;s interactions with the node in the workflow editor.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class NodeLocksDefBuilder {

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
    Map<NodeLocksDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(NodeLocksDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Boolean m_hasDeleteLock = false;
    

    Boolean m_hasResetLock = false;
    

    Boolean m_hasConfigureLock = false;
    

    /**
     * Create a new builder.
     */
    public NodeLocksDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public NodeLocksDefBuilder(final NodeLocksDef toCopy) {
        m_hasDeleteLock = toCopy.hasDeleteLock();
        m_hasResetLock = toCopy.hasResetLock();
        m_hasConfigureLock = toCopy.hasConfigureLock();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for hasDeleteLock
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param hasDeleteLock Whether a user is allowed to delete the node in the workflow editor.
     * @return this builder for fluent API.
     */ 
    public NodeLocksDefBuilder setHasDeleteLock(final Boolean hasDeleteLock) {
        setHasDeleteLock(() -> hasDeleteLock, hasDeleteLock);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NodeLocksDef.Attribute.HAS_DELETE_LOCK)} will return true and and
     * {@code getExceptionalChildren().get(NodeLocksDef.Attribute.HAS_DELETE_LOCK)} will return the exception.
     * 
     * @param hasDeleteLock see {@link NodeLocksDef#hasDeleteLock}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHasDeleteLock(Boolean)
     */
    public NodeLocksDefBuilder setHasDeleteLock(final FallibleSupplier<Boolean> hasDeleteLock, Boolean defaultValue) {
        java.util.Objects.requireNonNull(hasDeleteLock, () -> "No supplier for hasDeleteLock provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NodeLocksDef.Attribute.HAS_DELETE_LOCK);
        try {
            m_hasDeleteLock = hasDeleteLock.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_hasDeleteLock = defaultValue;
            m_exceptionalChildren.put(NodeLocksDef.Attribute.HAS_DELETE_LOCK, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for hasResetLock
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param hasResetLock Whether a user is allowed to reset the node in the workflow editor (the node might contain data).
     * @return this builder for fluent API.
     */ 
    public NodeLocksDefBuilder setHasResetLock(final Boolean hasResetLock) {
        setHasResetLock(() -> hasResetLock, hasResetLock);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NodeLocksDef.Attribute.HAS_RESET_LOCK)} will return true and and
     * {@code getExceptionalChildren().get(NodeLocksDef.Attribute.HAS_RESET_LOCK)} will return the exception.
     * 
     * @param hasResetLock see {@link NodeLocksDef#hasResetLock}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHasResetLock(Boolean)
     */
    public NodeLocksDefBuilder setHasResetLock(final FallibleSupplier<Boolean> hasResetLock, Boolean defaultValue) {
        java.util.Objects.requireNonNull(hasResetLock, () -> "No supplier for hasResetLock provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NodeLocksDef.Attribute.HAS_RESET_LOCK);
        try {
            m_hasResetLock = hasResetLock.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_hasResetLock = defaultValue;
            m_exceptionalChildren.put(NodeLocksDef.Attribute.HAS_RESET_LOCK, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for hasConfigureLock
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param hasConfigureLock Whether a user is allowed to configure the node in the workflow editor.
     * @return this builder for fluent API.
     */ 
    public NodeLocksDefBuilder setHasConfigureLock(final Boolean hasConfigureLock) {
        setHasConfigureLock(() -> hasConfigureLock, hasConfigureLock);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NodeLocksDef.Attribute.HAS_CONFIGURE_LOCK)} will return true and and
     * {@code getExceptionalChildren().get(NodeLocksDef.Attribute.HAS_CONFIGURE_LOCK)} will return the exception.
     * 
     * @param hasConfigureLock see {@link NodeLocksDef#hasConfigureLock}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHasConfigureLock(Boolean)
     */
    public NodeLocksDefBuilder setHasConfigureLock(final FallibleSupplier<Boolean> hasConfigureLock, Boolean defaultValue) {
        java.util.Objects.requireNonNull(hasConfigureLock, () -> "No supplier for hasConfigureLock provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NodeLocksDef.Attribute.HAS_CONFIGURE_LOCK);
        try {
            m_hasConfigureLock = hasConfigureLock.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_hasConfigureLock = defaultValue;
            m_exceptionalChildren.put(NodeLocksDef.Attribute.HAS_CONFIGURE_LOCK, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link NodeLocksDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultNodeLocksDef build() {
        
    	
        return new DefaultNodeLocksDef(this);
    }    

}
