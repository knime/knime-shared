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

import org.knime.shared.workflow.def.ConnectionUISettingsDef;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.ConnectionDef;
// for types that define enums
import org.knime.shared.workflow.def.ConnectionDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * ConnectionDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class ConnectionDefBuilder {

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
    public ConnectionDefBuilder strict(){
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
    Map<ConnectionDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ConnectionDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Integer m_sourceID;
    

    Integer m_destID;
    

    Integer m_sourcePort;
    

    Integer m_destPort;
    

    Boolean m_deletable = true;
    

    Optional<ConnectionUISettingsDef> m_uiSettings = Optional.empty();
    
    /**
     * Create a new builder.
     */
    public ConnectionDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ConnectionDefBuilder(final ConnectionDef toCopy) {
        setSourceID(toCopy.getSourceID());
        setDestID(toCopy.getDestID());
        setSourcePort(toCopy.getSourcePort());
        setDestPort(toCopy.getDestPort());
        setDeletable(toCopy.isDeletable());
        setUiSettings(toCopy.getUiSettings().orElse(null));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for sourceID
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param sourceID  
     * @return this builder for fluent API.
     */ 
    public ConnectionDefBuilder setSourceID(final Integer sourceID) {
        setSourceID(() -> sourceID, sourceID);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConnectionDef.Attribute.SOURCE_ID)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionDef.Attribute.SOURCE_ID)} will return the exception.
     * 
     * @param sourceID see {@link ConnectionDef#getSourceID}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setSourceID(Integer)
     */
    public ConnectionDefBuilder setSourceID(final FallibleSupplier<Integer> sourceID, Integer defaultValue) {
        java.util.Objects.requireNonNull(sourceID, () -> "No supplier for sourceID provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConnectionDef.Attribute.SOURCE_ID);
        try {
            var supplied = sourceID.get();
            m_sourceID = supplied;

            if(m_sourceID == null) {
                throw new IllegalArgumentException("sourceID is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_sourceID = defaultValue;
            m_exceptionalChildren.put(ConnectionDef.Attribute.SOURCE_ID, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for destID
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param destID  
     * @return this builder for fluent API.
     */ 
    public ConnectionDefBuilder setDestID(final Integer destID) {
        setDestID(() -> destID, destID);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConnectionDef.Attribute.DEST_ID)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionDef.Attribute.DEST_ID)} will return the exception.
     * 
     * @param destID see {@link ConnectionDef#getDestID}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDestID(Integer)
     */
    public ConnectionDefBuilder setDestID(final FallibleSupplier<Integer> destID, Integer defaultValue) {
        java.util.Objects.requireNonNull(destID, () -> "No supplier for destID provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConnectionDef.Attribute.DEST_ID);
        try {
            var supplied = destID.get();
            m_destID = supplied;

            if(m_destID == null) {
                throw new IllegalArgumentException("destID is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_destID = defaultValue;
            m_exceptionalChildren.put(ConnectionDef.Attribute.DEST_ID, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for sourcePort
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param sourcePort  
     * @return this builder for fluent API.
     */ 
    public ConnectionDefBuilder setSourcePort(final Integer sourcePort) {
        setSourcePort(() -> sourcePort, sourcePort);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConnectionDef.Attribute.SOURCE_PORT)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionDef.Attribute.SOURCE_PORT)} will return the exception.
     * 
     * @param sourcePort see {@link ConnectionDef#getSourcePort}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setSourcePort(Integer)
     */
    public ConnectionDefBuilder setSourcePort(final FallibleSupplier<Integer> sourcePort, Integer defaultValue) {
        java.util.Objects.requireNonNull(sourcePort, () -> "No supplier for sourcePort provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConnectionDef.Attribute.SOURCE_PORT);
        try {
            var supplied = sourcePort.get();
            m_sourcePort = supplied;

            if(m_sourcePort == null) {
                throw new IllegalArgumentException("sourcePort is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_sourcePort = defaultValue;
            m_exceptionalChildren.put(ConnectionDef.Attribute.SOURCE_PORT, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for destPort
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param destPort  
     * @return this builder for fluent API.
     */ 
    public ConnectionDefBuilder setDestPort(final Integer destPort) {
        setDestPort(() -> destPort, destPort);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConnectionDef.Attribute.DEST_PORT)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionDef.Attribute.DEST_PORT)} will return the exception.
     * 
     * @param destPort see {@link ConnectionDef#getDestPort}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDestPort(Integer)
     */
    public ConnectionDefBuilder setDestPort(final FallibleSupplier<Integer> destPort, Integer defaultValue) {
        java.util.Objects.requireNonNull(destPort, () -> "No supplier for destPort provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConnectionDef.Attribute.DEST_PORT);
        try {
            var supplied = destPort.get();
            m_destPort = supplied;

            if(m_destPort == null) {
                throw new IllegalArgumentException("destPort is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_destPort = defaultValue;
            m_exceptionalChildren.put(ConnectionDef.Attribute.DEST_PORT, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for deletable
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param deletable  
     * @return this builder for fluent API.
     */ 
    public ConnectionDefBuilder setDeletable(final Boolean deletable) {
        setDeletable(() -> deletable, deletable);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConnectionDef.Attribute.DELETABLE)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionDef.Attribute.DELETABLE)} will return the exception.
     * 
     * @param deletable see {@link ConnectionDef#isDeletable}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDeletable(Boolean)
     */
    public ConnectionDefBuilder setDeletable(final FallibleSupplier<Boolean> deletable, Boolean defaultValue) {
        java.util.Objects.requireNonNull(deletable, () -> "No supplier for deletable provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConnectionDef.Attribute.DELETABLE);
        try {
            var supplied = deletable.get();
            m_deletable = supplied;

            if(m_deletable == null) {
                throw new IllegalArgumentException("deletable is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_deletable = defaultValue;
            m_exceptionalChildren.put(ConnectionDef.Attribute.DELETABLE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for uiSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param uiSettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ConnectionDefBuilder setUiSettings(final ConnectionUISettingsDef uiSettings) {
        setUiSettings(() -> uiSettings, uiSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConnectionDef.Attribute.UI_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionDef.Attribute.UI_SETTINGS)} will return the exception.
     * 
     * @param uiSettings see {@link ConnectionDef#getUiSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setUiSettings(ConnectionUISettingsDef)
     */
    public ConnectionDefBuilder setUiSettings(final FallibleSupplier<ConnectionUISettingsDef> uiSettings) {
        setUiSettings(uiSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ConnectionDef.Attribute.UI_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionDef.Attribute.UI_SETTINGS)} will return the exception.
     * 
     * @param uiSettings see {@link ConnectionDef#getUiSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setUiSettings(ConnectionUISettingsDef)
     */
    public ConnectionDefBuilder setUiSettings(final FallibleSupplier<ConnectionUISettingsDef> uiSettings, ConnectionUISettingsDef defaultValue) {
        java.util.Objects.requireNonNull(uiSettings, () -> "No supplier for uiSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConnectionDef.Attribute.UI_SETTINGS);
        try {
            var supplied = uiSettings.get();
            m_uiSettings = Optional.ofNullable(supplied);
            if (m_uiSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_uiSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(ConnectionDef.Attribute.UI_SETTINGS, (LoadExceptionTree<?>)m_uiSettings.get());
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
            m_uiSettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ConnectionDef.Attribute.UI_SETTINGS, exceptionTree);
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
	 * @return the {@link ConnectionDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultConnectionDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_sourceID == null) setSourceID( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_destID == null) setDestID( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_sourcePort == null) setSourcePort( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_destPort == null) setDestPort( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_deletable == null) setDeletable( null);
        
    	
        return new DefaultConnectionDef(this);
    }    

}
