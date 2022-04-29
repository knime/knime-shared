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

import org.knime.core.workflow.def.CipherDef;
import org.knime.core.workflow.def.ComponentDialogSettingsDef;
import org.knime.core.workflow.def.ComponentMetadataDef;
import org.knime.core.workflow.def.ConfigMapDef;
import org.knime.core.workflow.def.JobManagerDef;
import org.knime.core.workflow.def.NodeAnnotationDef;
import org.knime.core.workflow.def.NodeLocksDef;
import org.knime.core.workflow.def.NodeUIInfoDef;
import org.knime.core.workflow.def.PortDef;
import org.knime.core.workflow.def.TemplateInfoDef;
import org.knime.core.workflow.def.WorkflowDef;
import org.knime.core.workflow.def.impl.ConfigurableNodeDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.ComponentDef;
// for types that define enums
import org.knime.core.workflow.def.ComponentDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * A node that contains a workflow. Similar to a metanode, except it has more flexibility, e.g., filtering the incoming and outgoing flow variables.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class ComponentDefBuilder {

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
    Map<ComponentDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ComponentDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Integer m_id;
    

    NodeTypeEnum m_nodeType;
    

    String m_customDescription;
    

    NodeAnnotationDef m_annotation;
    
    NodeUIInfoDef m_uiInfo;
    
    NodeLocksDef m_locks;
    
    JobManagerDef m_jobManager;
    
    ConfigMapDef m_modelSettings;
    
    ConfigMapDef m_internalNodeSubSettings;
    
    ConfigMapDef m_variableSettings;
    
    WorkflowDef m_workflow;
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<PortDef> m_inPorts = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setInPorts these are added to m_inPorts in build */
    private java.util.List<PortDef> m_inPortsBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_inPortsContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<PortDef> m_outPorts = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setOutPorts these are added to m_outPorts in build */
    private java.util.List<PortDef> m_outPortsBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_outPortsContainerSupplyException; 
    
    CipherDef m_cipher;
    
    Integer m_virtualInNodeId;
    

    Integer m_virtualOutNodeId;
    

    ComponentMetadataDef m_metadata;
    
    TemplateInfoDef m_templateInfo;
    
    ComponentDialogSettingsDef m_dialogSettings;
    
    /**
     * Create a new builder.
     */
    public ComponentDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ComponentDefBuilder(final ComponentDef toCopy) {
        m_id = toCopy.getId();
        m_nodeType = toCopy.getNodeType();
        m_customDescription = toCopy.getCustomDescription();
        m_annotation = toCopy.getAnnotation();
        m_uiInfo = toCopy.getUiInfo();
        m_locks = toCopy.getLocks();
        m_jobManager = toCopy.getJobManager();
        m_modelSettings = toCopy.getModelSettings();
        m_internalNodeSubSettings = toCopy.getInternalNodeSubSettings();
        m_variableSettings = toCopy.getVariableSettings();
        m_workflow = toCopy.getWorkflow();
        m_inPorts = toCopy.getInPorts();
        m_outPorts = toCopy.getOutPorts();
        m_cipher = toCopy.getCipher();
        m_virtualInNodeId = toCopy.getVirtualInNodeId();
        m_virtualOutNodeId = toCopy.getVirtualOutNodeId();
        m_metadata = toCopy.getMetadata();
        m_templateInfo = toCopy.getTemplateInfo();
        m_dialogSettings = toCopy.getDialogSettings();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for id
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param id Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setId(final Integer id) {
        setId(() -> id, id);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.ID)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.ID)} will return the exception.
     * 
     * @param id see {@link ComponentDef#getId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setId(Integer)
     */
    public ComponentDefBuilder setId(final FallibleSupplier<Integer> id, Integer defaultValue) {
        java.util.Objects.requireNonNull(id, () -> "No supplier for id provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.ID);
        try {
            m_id = id.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_id = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.ID, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for nodeType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param nodeType states the most specific subtype, i.e., Metanode, Component, or Native Node
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setNodeType(final NodeTypeEnum nodeType) {
        setNodeType(() -> nodeType, nodeType);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.NODE_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.NODE_TYPE)} will return the exception.
     * 
     * @param nodeType see {@link ComponentDef#getNodeType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setNodeType(NodeTypeEnum)
     */
    public ComponentDefBuilder setNodeType(final FallibleSupplier<NodeTypeEnum> nodeType, NodeTypeEnum defaultValue) {
        java.util.Objects.requireNonNull(nodeType, () -> "No supplier for nodeType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.NODE_TYPE);
        try {
            m_nodeType = nodeType.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_nodeType = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.NODE_TYPE, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for customDescription
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param customDescription A longer description, provided by the user
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setCustomDescription(final String customDescription) {
        setCustomDescription(() -> customDescription, customDescription);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.CUSTOM_DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.CUSTOM_DESCRIPTION)} will return the exception.
     * 
     * @param customDescription see {@link ComponentDef#getCustomDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCustomDescription(String)
     */
    public ComponentDefBuilder setCustomDescription(final FallibleSupplier<String> customDescription, String defaultValue) {
        java.util.Objects.requireNonNull(customDescription, () -> "No supplier for customDescription provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.CUSTOM_DESCRIPTION);
        try {
            m_customDescription = customDescription.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_customDescription = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.CUSTOM_DESCRIPTION, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for annotation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param annotation 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setAnnotation(final NodeAnnotationDef annotation) {
        setAnnotation(() -> annotation, annotation);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.ANNOTATION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.ANNOTATION)} will return the exception.
     * 
     * @param annotation see {@link ComponentDef#getAnnotation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotation(NodeAnnotationDef)
     */
    public ComponentDefBuilder setAnnotation(final FallibleSupplier<NodeAnnotationDef> annotation, NodeAnnotationDef defaultValue) {
        java.util.Objects.requireNonNull(annotation, () -> "No supplier for annotation provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.ANNOTATION);
        try {
            m_annotation = annotation.get();
            if (m_annotation instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_annotation).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.ANNOTATION, (LoadExceptionTree<?>)m_annotation);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleNodeAnnotationDef){
                var childTree = ((FallibleNodeAnnotationDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_annotation = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.ANNOTATION, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for uiInfo
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param uiInfo 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setUiInfo(final NodeUIInfoDef uiInfo) {
        setUiInfo(() -> uiInfo, uiInfo);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.UI_INFO)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.UI_INFO)} will return the exception.
     * 
     * @param uiInfo see {@link ComponentDef#getUiInfo}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setUiInfo(NodeUIInfoDef)
     */
    public ComponentDefBuilder setUiInfo(final FallibleSupplier<NodeUIInfoDef> uiInfo, NodeUIInfoDef defaultValue) {
        java.util.Objects.requireNonNull(uiInfo, () -> "No supplier for uiInfo provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.UI_INFO);
        try {
            m_uiInfo = uiInfo.get();
            if (m_uiInfo instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_uiInfo).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.UI_INFO, (LoadExceptionTree<?>)m_uiInfo);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleNodeUIInfoDef){
                var childTree = ((FallibleNodeUIInfoDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_uiInfo = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.UI_INFO, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for locks
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param locks 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setLocks(final NodeLocksDef locks) {
        setLocks(() -> locks, locks);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.LOCKS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.LOCKS)} will return the exception.
     * 
     * @param locks see {@link ComponentDef#getLocks}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocks(NodeLocksDef)
     */
    public ComponentDefBuilder setLocks(final FallibleSupplier<NodeLocksDef> locks, NodeLocksDef defaultValue) {
        java.util.Objects.requireNonNull(locks, () -> "No supplier for locks provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.LOCKS);
        try {
            m_locks = locks.get();
            if (m_locks instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_locks).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.LOCKS, (LoadExceptionTree<?>)m_locks);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleNodeLocksDef){
                var childTree = ((FallibleNodeLocksDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_locks = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.LOCKS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for jobManager
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param jobManager 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setJobManager(final JobManagerDef jobManager) {
        setJobManager(() -> jobManager, jobManager);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.JOB_MANAGER)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.JOB_MANAGER)} will return the exception.
     * 
     * @param jobManager see {@link ComponentDef#getJobManager}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setJobManager(JobManagerDef)
     */
    public ComponentDefBuilder setJobManager(final FallibleSupplier<JobManagerDef> jobManager, JobManagerDef defaultValue) {
        java.util.Objects.requireNonNull(jobManager, () -> "No supplier for jobManager provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.JOB_MANAGER);
        try {
            m_jobManager = jobManager.get();
            if (m_jobManager instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_jobManager).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.JOB_MANAGER, (LoadExceptionTree<?>)m_jobManager);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleJobManagerDef){
                var childTree = ((FallibleJobManagerDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_jobManager = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.JOB_MANAGER, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for modelSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param modelSettings 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setModelSettings(final ConfigMapDef modelSettings) {
        setModelSettings(() -> modelSettings, modelSettings);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.MODEL_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.MODEL_SETTINGS)} will return the exception.
     * 
     * @param modelSettings see {@link ComponentDef#getModelSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setModelSettings(ConfigMapDef)
     */
    public ComponentDefBuilder setModelSettings(final FallibleSupplier<ConfigMapDef> modelSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(modelSettings, () -> "No supplier for modelSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.MODEL_SETTINGS);
        try {
            m_modelSettings = modelSettings.get();
            if (m_modelSettings instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_modelSettings).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.MODEL_SETTINGS, (LoadExceptionTree<?>)m_modelSettings);
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
            m_modelSettings = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.MODEL_SETTINGS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for internalNodeSubSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param internalNodeSubSettings 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setInternalNodeSubSettings(final ConfigMapDef internalNodeSubSettings) {
        setInternalNodeSubSettings(() -> internalNodeSubSettings, internalNodeSubSettings);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return the exception.
     * 
     * @param internalNodeSubSettings see {@link ComponentDef#getInternalNodeSubSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setInternalNodeSubSettings(ConfigMapDef)
     */
    public ComponentDefBuilder setInternalNodeSubSettings(final FallibleSupplier<ConfigMapDef> internalNodeSubSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(internalNodeSubSettings, () -> "No supplier for internalNodeSubSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.INTERNAL_NODE_SUB_SETTINGS);
        try {
            m_internalNodeSubSettings = internalNodeSubSettings.get();
            if (m_internalNodeSubSettings instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_internalNodeSubSettings).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.INTERNAL_NODE_SUB_SETTINGS, (LoadExceptionTree<?>)m_internalNodeSubSettings);
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
            m_internalNodeSubSettings = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.INTERNAL_NODE_SUB_SETTINGS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for variableSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param variableSettings 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setVariableSettings(final ConfigMapDef variableSettings) {
        setVariableSettings(() -> variableSettings, variableSettings);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.VARIABLE_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.VARIABLE_SETTINGS)} will return the exception.
     * 
     * @param variableSettings see {@link ComponentDef#getVariableSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVariableSettings(ConfigMapDef)
     */
    public ComponentDefBuilder setVariableSettings(final FallibleSupplier<ConfigMapDef> variableSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(variableSettings, () -> "No supplier for variableSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.VARIABLE_SETTINGS);
        try {
            m_variableSettings = variableSettings.get();
            if (m_variableSettings instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_variableSettings).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.VARIABLE_SETTINGS, (LoadExceptionTree<?>)m_variableSettings);
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
            m_variableSettings = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.VARIABLE_SETTINGS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for workflow
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param workflow 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setWorkflow(final WorkflowDef workflow) {
        setWorkflow(() -> workflow, workflow);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.WORKFLOW)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.WORKFLOW)} will return the exception.
     * 
     * @param workflow see {@link ComponentDef#getWorkflow}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWorkflow(WorkflowDef)
     */
    public ComponentDefBuilder setWorkflow(final FallibleSupplier<WorkflowDef> workflow, WorkflowDef defaultValue) {
        java.util.Objects.requireNonNull(workflow, () -> "No supplier for workflow provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.WORKFLOW);
        try {
            m_workflow = workflow.get();
            if (m_workflow instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_workflow).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.WORKFLOW, (LoadExceptionTree<?>)m_workflow);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleWorkflowDef){
                var childTree = ((FallibleWorkflowDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_workflow = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.WORKFLOW, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for inPorts
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the inPorts list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToInPorts} will be inserted at the end of the list.
     * @param inPorts 
     * @return this for fluent API
     */
    public ComponentDefBuilder setInPorts(final java.util.List<PortDef> inPorts) {
        setInPorts(() -> inPorts);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the inPorts list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the inPorts list). 
     * {@code hasExceptions(ComponentDef.Attribute.IN_PORTS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.IN_PORTS)} will return the exception.
     * 
     * @param inPorts see {@link ComponentDef#getInPorts}
     * 
     * @return this builder for fluent API.
     * @see #setInPorts(java.util.List<PortDef>)
     */
    public ComponentDefBuilder setInPorts(final FallibleSupplier<java.util.List<PortDef>> inPorts) {
        java.util.Objects.requireNonNull(inPorts, () -> "No supplier for inPorts provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.IN_PORTS);
        try {
            m_inPortsBulkElements = inPorts.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_inPortsBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_inPortsContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the inPorts list
     * @return this builder for fluent API
     */
    public ComponentDefBuilder addToInPorts(PortDef value){
    	addToInPorts(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentDef#getInPorts}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the inPorts list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentDefBuilder addToInPorts(FallibleSupplier<PortDef> value, PortDef defaultValue) {
        PortDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new FalliblePortDef(defaultValue, supplyException);
        }
        m_inPorts.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for outPorts
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the outPorts list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToOutPorts} will be inserted at the end of the list.
     * @param outPorts 
     * @return this for fluent API
     */
    public ComponentDefBuilder setOutPorts(final java.util.List<PortDef> outPorts) {
        setOutPorts(() -> outPorts);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the outPorts list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the outPorts list). 
     * {@code hasExceptions(ComponentDef.Attribute.OUT_PORTS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.OUT_PORTS)} will return the exception.
     * 
     * @param outPorts see {@link ComponentDef#getOutPorts}
     * 
     * @return this builder for fluent API.
     * @see #setOutPorts(java.util.List<PortDef>)
     */
    public ComponentDefBuilder setOutPorts(final FallibleSupplier<java.util.List<PortDef>> outPorts) {
        java.util.Objects.requireNonNull(outPorts, () -> "No supplier for outPorts provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.OUT_PORTS);
        try {
            m_outPortsBulkElements = outPorts.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_outPortsBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_outPortsContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the outPorts list
     * @return this builder for fluent API
     */
    public ComponentDefBuilder addToOutPorts(PortDef value){
    	addToOutPorts(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentDef#getOutPorts}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the outPorts list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentDefBuilder addToOutPorts(FallibleSupplier<PortDef> value, PortDef defaultValue) {
        PortDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new FalliblePortDef(defaultValue, supplyException);
        }
        m_outPorts.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for cipher
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param cipher 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setCipher(final CipherDef cipher) {
        setCipher(() -> cipher, cipher);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.CIPHER)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.CIPHER)} will return the exception.
     * 
     * @param cipher see {@link ComponentDef#getCipher}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCipher(CipherDef)
     */
    public ComponentDefBuilder setCipher(final FallibleSupplier<CipherDef> cipher, CipherDef defaultValue) {
        java.util.Objects.requireNonNull(cipher, () -> "No supplier for cipher provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.CIPHER);
        try {
            m_cipher = cipher.get();
            if (m_cipher instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_cipher).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.CIPHER, (LoadExceptionTree<?>)m_cipher);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleCipherDef){
                var childTree = ((FallibleCipherDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_cipher = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.CIPHER, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for virtualInNodeId
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param virtualInNodeId The virtual in node provides the input ports of the component as its output ports (replaces the input bar of the metanode)
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setVirtualInNodeId(final Integer virtualInNodeId) {
        setVirtualInNodeId(() -> virtualInNodeId, virtualInNodeId);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.VIRTUAL_IN_NODE_ID)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.VIRTUAL_IN_NODE_ID)} will return the exception.
     * 
     * @param virtualInNodeId see {@link ComponentDef#getVirtualInNodeId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVirtualInNodeId(Integer)
     */
    public ComponentDefBuilder setVirtualInNodeId(final FallibleSupplier<Integer> virtualInNodeId, Integer defaultValue) {
        java.util.Objects.requireNonNull(virtualInNodeId, () -> "No supplier for virtualInNodeId provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.VIRTUAL_IN_NODE_ID);
        try {
            m_virtualInNodeId = virtualInNodeId.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_virtualInNodeId = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.VIRTUAL_IN_NODE_ID, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for virtualOutNodeId
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param virtualOutNodeId 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setVirtualOutNodeId(final Integer virtualOutNodeId) {
        setVirtualOutNodeId(() -> virtualOutNodeId, virtualOutNodeId);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.VIRTUAL_OUT_NODE_ID)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.VIRTUAL_OUT_NODE_ID)} will return the exception.
     * 
     * @param virtualOutNodeId see {@link ComponentDef#getVirtualOutNodeId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVirtualOutNodeId(Integer)
     */
    public ComponentDefBuilder setVirtualOutNodeId(final FallibleSupplier<Integer> virtualOutNodeId, Integer defaultValue) {
        java.util.Objects.requireNonNull(virtualOutNodeId, () -> "No supplier for virtualOutNodeId provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.VIRTUAL_OUT_NODE_ID);
        try {
            m_virtualOutNodeId = virtualOutNodeId.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_virtualOutNodeId = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.VIRTUAL_OUT_NODE_ID, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for metadata
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param metadata 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setMetadata(final ComponentMetadataDef metadata) {
        setMetadata(() -> metadata, metadata);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.METADATA)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.METADATA)} will return the exception.
     * 
     * @param metadata see {@link ComponentDef#getMetadata}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setMetadata(ComponentMetadataDef)
     */
    public ComponentDefBuilder setMetadata(final FallibleSupplier<ComponentMetadataDef> metadata, ComponentMetadataDef defaultValue) {
        java.util.Objects.requireNonNull(metadata, () -> "No supplier for metadata provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.METADATA);
        try {
            m_metadata = metadata.get();
            if (m_metadata instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_metadata).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.METADATA, (LoadExceptionTree<?>)m_metadata);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleComponentMetadataDef){
                var childTree = ((FallibleComponentMetadataDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_metadata = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.METADATA, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for templateInfo
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param templateInfo 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setTemplateInfo(final TemplateInfoDef templateInfo) {
        setTemplateInfo(() -> templateInfo, templateInfo);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.TEMPLATE_INFO)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.TEMPLATE_INFO)} will return the exception.
     * 
     * @param templateInfo see {@link ComponentDef#getTemplateInfo}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTemplateInfo(TemplateInfoDef)
     */
    public ComponentDefBuilder setTemplateInfo(final FallibleSupplier<TemplateInfoDef> templateInfo, TemplateInfoDef defaultValue) {
        java.util.Objects.requireNonNull(templateInfo, () -> "No supplier for templateInfo provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.TEMPLATE_INFO);
        try {
            m_templateInfo = templateInfo.get();
            if (m_templateInfo instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_templateInfo).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.TEMPLATE_INFO, (LoadExceptionTree<?>)m_templateInfo);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleTemplateInfoDef){
                var childTree = ((FallibleTemplateInfoDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_templateInfo = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.TEMPLATE_INFO, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for dialogSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param dialogSettings 
     * @return this builder for fluent API.
     */ 
    public ComponentDefBuilder setDialogSettings(final ComponentDialogSettingsDef dialogSettings) {
        setDialogSettings(() -> dialogSettings, dialogSettings);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDef.Attribute.DIALOG_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDef.Attribute.DIALOG_SETTINGS)} will return the exception.
     * 
     * @param dialogSettings see {@link ComponentDef#getDialogSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDialogSettings(ComponentDialogSettingsDef)
     */
    public ComponentDefBuilder setDialogSettings(final FallibleSupplier<ComponentDialogSettingsDef> dialogSettings, ComponentDialogSettingsDef defaultValue) {
        java.util.Objects.requireNonNull(dialogSettings, () -> "No supplier for dialogSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDef.Attribute.DIALOG_SETTINGS);
        try {
            m_dialogSettings = dialogSettings.get();
            if (m_dialogSettings instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_dialogSettings).hasExceptions()) {
                m_exceptionalChildren.put(ComponentDef.Attribute.DIALOG_SETTINGS, (LoadExceptionTree<?>)m_dialogSettings);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof FallibleComponentDialogSettingsDef){
                var childTree = ((FallibleComponentDialogSettingsDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_dialogSettings = defaultValue;
            m_exceptionalChildren.put(ComponentDef.Attribute.DIALOG_SETTINGS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link ComponentDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public FallibleComponentDef build() {
        
    	
        // contains the elements set with #setInPorts (those added with #addToInPorts have already been inserted into m_inPorts)
        m_inPortsBulkElements = java.util.Objects.requireNonNullElse(m_inPortsBulkElements, java.util.List.of());
        m_inPorts.addAll(0, m_inPortsBulkElements);
                
        var inPortsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_inPorts, m_inPortsContainerSupplyException);
        if(inPortsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentDef.Attribute.IN_PORTS, inPortsLoadExceptionTree);
        }
        
        // contains the elements set with #setOutPorts (those added with #addToOutPorts have already been inserted into m_outPorts)
        m_outPortsBulkElements = java.util.Objects.requireNonNullElse(m_outPortsBulkElements, java.util.List.of());
        m_outPorts.addAll(0, m_outPortsBulkElements);
                
        var outPortsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_outPorts, m_outPortsContainerSupplyException);
        if(outPortsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentDef.Attribute.OUT_PORTS, outPortsLoadExceptionTree);
        }
        
        return new FallibleComponentDef(this);
    }    

}
