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

import org.knime.core.workflow.def.ConfigMapDef;

// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.JobManagerDef;
// for types that define enums
import org.knime.core.workflow.def.JobManagerDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * Optional information on the node&#39;s execution job manager.  If missing, the node is executed in a default mode.  If present, combines the factory id and factory configuration of the job manager, e.g.,  a streaming node executor and its chunk size setting.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class JobManagerDefBuilder {

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
    Map<JobManagerDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(JobManagerDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_factory;
    

    ConfigMapDef m_settings;
    
    /**
     * Create a new builder.
     */
    public JobManagerDefBuilder() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for factory
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param factory Qualified name of a class that implements NodeExecutionJobManagerFactory This is passed to NodeExecutionJobManagerPool#getFactory when restoring a node&#39;s job manager.
     * @return this builder for fluent API.
     */ 
    public JobManagerDefBuilder setFactory(final String factory) {
        setFactory(() -> factory, factory);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(JobManagerDef.Attribute.FACTORY)} will return true and and
     * {@code getExceptionalChildren().get(JobManagerDef.Attribute.FACTORY)} will return the exception.
     * 
     * @param factory see {@link JobManagerDef#getFactory}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFactory(String)
     */
    public JobManagerDefBuilder setFactory(final FallibleSupplier<String> factory, String defaultValue) {
        java.util.Objects.requireNonNull(factory, () -> "No supplier for factory provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(JobManagerDef.Attribute.FACTORY);
        try {
            m_factory = factory.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_factory = defaultValue;
            m_exceptionalChildren.put(JobManagerDef.Attribute.FACTORY, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for settings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param settings 
     * @return this builder for fluent API.
     */ 
    public JobManagerDefBuilder setSettings(final ConfigMapDef settings) {
        setSettings(() -> settings, settings);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(JobManagerDef.Attribute.SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(JobManagerDef.Attribute.SETTINGS)} will return the exception.
     * 
     * @param settings see {@link JobManagerDef#getSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setSettings(ConfigMapDef)
     */
    public JobManagerDefBuilder setSettings(final FallibleSupplier<ConfigMapDef> settings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(settings, () -> "No supplier for settings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(JobManagerDef.Attribute.SETTINGS);
        try {
            m_settings = settings.get();
            if (m_settings instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_settings).hasExceptions()) {
                m_exceptionalChildren.put(JobManagerDef.Attribute.SETTINGS, (LoadExceptionTree<?>)m_settings);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleConfigMapDef){
                var childTree = ((FallibleConfigMapDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_settings = defaultValue;
            m_exceptionalChildren.put(JobManagerDef.Attribute.SETTINGS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link JobManagerDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public FallibleJobManagerDef build() {
        
    	
        return new FallibleJobManagerDef(this);
    }    

}
