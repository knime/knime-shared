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

import org.knime.shared.workflow.def.CipherDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.NodeUIInfoDef;
import org.knime.shared.workflow.def.PortDef;
import org.knime.shared.workflow.def.TemplateInfoDef;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.def.impl.BaseNodeDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.MetaNodeDef;
// for types that define enums
import org.knime.shared.workflow.def.MetaNodeDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * MetaNodeDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class MetaNodeDefBuilder {

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
    Map<MetaNodeDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(MetaNodeDef.Attribute.class);

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
    
    TemplateInfoDef m_link;
    
    NodeUIInfoDef m_inPortsBarUIInfo;
    
    NodeUIInfoDef m_outPortsBarUIInfo;
    
    /**
     * Create a new builder.
     */
    public MetaNodeDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public MetaNodeDefBuilder(final MetaNodeDef toCopy) {
        m_id = toCopy.getId();
        m_nodeType = toCopy.getNodeType();
        m_customDescription = toCopy.getCustomDescription();
        m_annotation = toCopy.getAnnotation();
        m_uiInfo = toCopy.getUiInfo();
        m_locks = toCopy.getLocks();
        m_jobManager = toCopy.getJobManager();
        m_workflow = toCopy.getWorkflow();
        m_inPorts = toCopy.getInPorts();
        m_outPorts = toCopy.getOutPorts();
        m_cipher = toCopy.getCipher();
        m_link = toCopy.getLink();
        m_inPortsBarUIInfo = toCopy.getInPortsBarUIInfo();
        m_outPortsBarUIInfo = toCopy.getOutPortsBarUIInfo();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for id
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param id Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. 
     * @return this builder for fluent API.
     */ 
    public MetaNodeDefBuilder setId(final Integer id) {
        setId(() -> id, id);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.ID)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.ID)} will return the exception.
     * 
     * @param id see {@link MetaNodeDef#getId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setId(Integer)
     */
    public MetaNodeDefBuilder setId(final FallibleSupplier<Integer> id, Integer defaultValue) {
        java.util.Objects.requireNonNull(id, () -> "No supplier for id provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.ID);
        try {
            m_id = id.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_id = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.ID, supplyException);
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
    public MetaNodeDefBuilder setNodeType(final NodeTypeEnum nodeType) {
        setNodeType(() -> nodeType, nodeType);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.NODE_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.NODE_TYPE)} will return the exception.
     * 
     * @param nodeType see {@link MetaNodeDef#getNodeType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setNodeType(NodeTypeEnum)
     */
    public MetaNodeDefBuilder setNodeType(final FallibleSupplier<NodeTypeEnum> nodeType, NodeTypeEnum defaultValue) {
        java.util.Objects.requireNonNull(nodeType, () -> "No supplier for nodeType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.NODE_TYPE);
        try {
            m_nodeType = nodeType.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_nodeType = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.NODE_TYPE, supplyException);
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
    public MetaNodeDefBuilder setCustomDescription(final String customDescription) {
        setCustomDescription(() -> customDescription, customDescription);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return the exception.
     * 
     * @param customDescription see {@link MetaNodeDef#getCustomDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCustomDescription(String)
     */
    public MetaNodeDefBuilder setCustomDescription(final FallibleSupplier<String> customDescription, String defaultValue) {
        java.util.Objects.requireNonNull(customDescription, () -> "No supplier for customDescription provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.CUSTOM_DESCRIPTION);
        try {
            m_customDescription = customDescription.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_customDescription = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.CUSTOM_DESCRIPTION, supplyException);
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
    public MetaNodeDefBuilder setAnnotation(final NodeAnnotationDef annotation) {
        setAnnotation(() -> annotation, annotation);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.ANNOTATION)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.ANNOTATION)} will return the exception.
     * 
     * @param annotation see {@link MetaNodeDef#getAnnotation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotation(NodeAnnotationDef)
     */
    public MetaNodeDefBuilder setAnnotation(final FallibleSupplier<NodeAnnotationDef> annotation, NodeAnnotationDef defaultValue) {
        java.util.Objects.requireNonNull(annotation, () -> "No supplier for annotation provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.ANNOTATION);
        try {
            m_annotation = annotation.get();
            if (m_annotation instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_annotation).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.ANNOTATION, (LoadExceptionTree<?>)m_annotation);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultNodeAnnotationDef){
                var childTree = ((DefaultNodeAnnotationDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_annotation = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.ANNOTATION, exceptionTree);
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
    public MetaNodeDefBuilder setUiInfo(final NodeUIInfoDef uiInfo) {
        setUiInfo(() -> uiInfo, uiInfo);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.UI_INFO)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.UI_INFO)} will return the exception.
     * 
     * @param uiInfo see {@link MetaNodeDef#getUiInfo}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setUiInfo(NodeUIInfoDef)
     */
    public MetaNodeDefBuilder setUiInfo(final FallibleSupplier<NodeUIInfoDef> uiInfo, NodeUIInfoDef defaultValue) {
        java.util.Objects.requireNonNull(uiInfo, () -> "No supplier for uiInfo provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.UI_INFO);
        try {
            m_uiInfo = uiInfo.get();
            if (m_uiInfo instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_uiInfo).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.UI_INFO, (LoadExceptionTree<?>)m_uiInfo);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultNodeUIInfoDef){
                var childTree = ((DefaultNodeUIInfoDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_uiInfo = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.UI_INFO, exceptionTree);
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
    public MetaNodeDefBuilder setLocks(final NodeLocksDef locks) {
        setLocks(() -> locks, locks);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.LOCKS)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.LOCKS)} will return the exception.
     * 
     * @param locks see {@link MetaNodeDef#getLocks}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocks(NodeLocksDef)
     */
    public MetaNodeDefBuilder setLocks(final FallibleSupplier<NodeLocksDef> locks, NodeLocksDef defaultValue) {
        java.util.Objects.requireNonNull(locks, () -> "No supplier for locks provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.LOCKS);
        try {
            m_locks = locks.get();
            if (m_locks instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_locks).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.LOCKS, (LoadExceptionTree<?>)m_locks);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultNodeLocksDef){
                var childTree = ((DefaultNodeLocksDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_locks = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.LOCKS, exceptionTree);
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
    public MetaNodeDefBuilder setJobManager(final JobManagerDef jobManager) {
        setJobManager(() -> jobManager, jobManager);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.JOB_MANAGER)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.JOB_MANAGER)} will return the exception.
     * 
     * @param jobManager see {@link MetaNodeDef#getJobManager}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setJobManager(JobManagerDef)
     */
    public MetaNodeDefBuilder setJobManager(final FallibleSupplier<JobManagerDef> jobManager, JobManagerDef defaultValue) {
        java.util.Objects.requireNonNull(jobManager, () -> "No supplier for jobManager provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.JOB_MANAGER);
        try {
            m_jobManager = jobManager.get();
            if (m_jobManager instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_jobManager).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.JOB_MANAGER, (LoadExceptionTree<?>)m_jobManager);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultJobManagerDef){
                var childTree = ((DefaultJobManagerDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_jobManager = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.JOB_MANAGER, exceptionTree);
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
    public MetaNodeDefBuilder setWorkflow(final WorkflowDef workflow) {
        setWorkflow(() -> workflow, workflow);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.WORKFLOW)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.WORKFLOW)} will return the exception.
     * 
     * @param workflow see {@link MetaNodeDef#getWorkflow}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWorkflow(WorkflowDef)
     */
    public MetaNodeDefBuilder setWorkflow(final FallibleSupplier<WorkflowDef> workflow, WorkflowDef defaultValue) {
        java.util.Objects.requireNonNull(workflow, () -> "No supplier for workflow provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.WORKFLOW);
        try {
            m_workflow = workflow.get();
            if (m_workflow instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_workflow).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.WORKFLOW, (LoadExceptionTree<?>)m_workflow);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultWorkflowDef){
                var childTree = ((DefaultWorkflowDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_workflow = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.WORKFLOW, exceptionTree);
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
     * @param inPorts Defines the endpoints for incoming data connections.
     * @return this for fluent API
     */
    public MetaNodeDefBuilder setInPorts(final java.util.List<PortDef> inPorts) {
        setInPorts(() -> inPorts);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the inPorts list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the inPorts list). 
     * {@code hasExceptions(MetaNodeDef.Attribute.IN_PORTS)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.IN_PORTS)} will return the exception.
     * 
     * @param inPorts see {@link MetaNodeDef#getInPorts}
     * 
     * @return this builder for fluent API.
     * @see #setInPorts(java.util.List<PortDef>)
     */
    public MetaNodeDefBuilder setInPorts(final FallibleSupplier<java.util.List<PortDef>> inPorts) {
        java.util.Objects.requireNonNull(inPorts, () -> "No supplier for inPorts provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.IN_PORTS);
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
    public MetaNodeDefBuilder addToInPorts(PortDef value){
    	addToInPorts(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link MetaNodeDef#getInPorts}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the inPorts list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public MetaNodeDefBuilder addToInPorts(FallibleSupplier<PortDef> value, PortDef defaultValue) {
        PortDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultPortDef(defaultValue, supplyException);
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
     * @param outPorts Defines the endpoints for outgoing data connections.
     * @return this for fluent API
     */
    public MetaNodeDefBuilder setOutPorts(final java.util.List<PortDef> outPorts) {
        setOutPorts(() -> outPorts);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the outPorts list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the outPorts list). 
     * {@code hasExceptions(MetaNodeDef.Attribute.OUT_PORTS)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.OUT_PORTS)} will return the exception.
     * 
     * @param outPorts see {@link MetaNodeDef#getOutPorts}
     * 
     * @return this builder for fluent API.
     * @see #setOutPorts(java.util.List<PortDef>)
     */
    public MetaNodeDefBuilder setOutPorts(final FallibleSupplier<java.util.List<PortDef>> outPorts) {
        java.util.Objects.requireNonNull(outPorts, () -> "No supplier for outPorts provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.OUT_PORTS);
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
    public MetaNodeDefBuilder addToOutPorts(PortDef value){
    	addToOutPorts(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link MetaNodeDef#getOutPorts}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the outPorts list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public MetaNodeDefBuilder addToOutPorts(FallibleSupplier<PortDef> value, PortDef defaultValue) {
        PortDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultPortDef(defaultValue, supplyException);
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
    public MetaNodeDefBuilder setCipher(final CipherDef cipher) {
        setCipher(() -> cipher, cipher);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.CIPHER)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.CIPHER)} will return the exception.
     * 
     * @param cipher see {@link MetaNodeDef#getCipher}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCipher(CipherDef)
     */
    public MetaNodeDefBuilder setCipher(final FallibleSupplier<CipherDef> cipher, CipherDef defaultValue) {
        java.util.Objects.requireNonNull(cipher, () -> "No supplier for cipher provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.CIPHER);
        try {
            m_cipher = cipher.get();
            if (m_cipher instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_cipher).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.CIPHER, (LoadExceptionTree<?>)m_cipher);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultCipherDef){
                var childTree = ((DefaultCipherDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_cipher = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.CIPHER, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for link
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param link 
     * @return this builder for fluent API.
     */ 
    public MetaNodeDefBuilder setLink(final TemplateInfoDef link) {
        setLink(() -> link, link);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.LINK)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.LINK)} will return the exception.
     * 
     * @param link see {@link MetaNodeDef#getLink}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLink(TemplateInfoDef)
     */
    public MetaNodeDefBuilder setLink(final FallibleSupplier<TemplateInfoDef> link, TemplateInfoDef defaultValue) {
        java.util.Objects.requireNonNull(link, () -> "No supplier for link provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.LINK);
        try {
            m_link = link.get();
            if (m_link instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_link).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.LINK, (LoadExceptionTree<?>)m_link);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultTemplateInfoDef){
                var childTree = ((DefaultTemplateInfoDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_link = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.LINK, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for inPortsBarUIInfo
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param inPortsBarUIInfo 
     * @return this builder for fluent API.
     */ 
    public MetaNodeDefBuilder setInPortsBarUIInfo(final NodeUIInfoDef inPortsBarUIInfo) {
        setInPortsBarUIInfo(() -> inPortsBarUIInfo, inPortsBarUIInfo);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.IN_PORTS_BAR_UI_INFO)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.IN_PORTS_BAR_UI_INFO)} will return the exception.
     * 
     * @param inPortsBarUIInfo see {@link MetaNodeDef#getInPortsBarUIInfo}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setInPortsBarUIInfo(NodeUIInfoDef)
     */
    public MetaNodeDefBuilder setInPortsBarUIInfo(final FallibleSupplier<NodeUIInfoDef> inPortsBarUIInfo, NodeUIInfoDef defaultValue) {
        java.util.Objects.requireNonNull(inPortsBarUIInfo, () -> "No supplier for inPortsBarUIInfo provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.IN_PORTS_BAR_UI_INFO);
        try {
            m_inPortsBarUIInfo = inPortsBarUIInfo.get();
            if (m_inPortsBarUIInfo instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_inPortsBarUIInfo).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.IN_PORTS_BAR_UI_INFO, (LoadExceptionTree<?>)m_inPortsBarUIInfo);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultNodeUIInfoDef){
                var childTree = ((DefaultNodeUIInfoDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_inPortsBarUIInfo = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.IN_PORTS_BAR_UI_INFO, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for outPortsBarUIInfo
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param outPortsBarUIInfo 
     * @return this builder for fluent API.
     */ 
    public MetaNodeDefBuilder setOutPortsBarUIInfo(final NodeUIInfoDef outPortsBarUIInfo) {
        setOutPortsBarUIInfo(() -> outPortsBarUIInfo, outPortsBarUIInfo);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(MetaNodeDef.Attribute.OUT_PORTS_BAR_UI_INFO)} will return true and and
     * {@code getExceptionalChildren().get(MetaNodeDef.Attribute.OUT_PORTS_BAR_UI_INFO)} will return the exception.
     * 
     * @param outPortsBarUIInfo see {@link MetaNodeDef#getOutPortsBarUIInfo}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setOutPortsBarUIInfo(NodeUIInfoDef)
     */
    public MetaNodeDefBuilder setOutPortsBarUIInfo(final FallibleSupplier<NodeUIInfoDef> outPortsBarUIInfo, NodeUIInfoDef defaultValue) {
        java.util.Objects.requireNonNull(outPortsBarUIInfo, () -> "No supplier for outPortsBarUIInfo provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(MetaNodeDef.Attribute.OUT_PORTS_BAR_UI_INFO);
        try {
            m_outPortsBarUIInfo = outPortsBarUIInfo.get();
            if (m_outPortsBarUIInfo instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_outPortsBarUIInfo).hasExceptions()) {
                m_exceptionalChildren.put(MetaNodeDef.Attribute.OUT_PORTS_BAR_UI_INFO, (LoadExceptionTree<?>)m_outPortsBarUIInfo);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultNodeUIInfoDef){
                var childTree = ((DefaultNodeUIInfoDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_outPortsBarUIInfo = defaultValue;
            m_exceptionalChildren.put(MetaNodeDef.Attribute.OUT_PORTS_BAR_UI_INFO, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link MetaNodeDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultMetaNodeDef build() {
        
    	
        // contains the elements set with #setInPorts (those added with #addToInPorts have already been inserted into m_inPorts)
        m_inPortsBulkElements = java.util.Objects.requireNonNullElse(m_inPortsBulkElements, java.util.List.of());
        m_inPorts.addAll(0, m_inPortsBulkElements);
                
        var inPortsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_inPorts, m_inPortsContainerSupplyException);
        if(inPortsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(MetaNodeDef.Attribute.IN_PORTS, inPortsLoadExceptionTree);
        }
        
        // contains the elements set with #setOutPorts (those added with #addToOutPorts have already been inserted into m_outPorts)
        m_outPortsBulkElements = java.util.Objects.requireNonNullElse(m_outPortsBulkElements, java.util.List.of());
        m_outPorts.addAll(0, m_outPortsBulkElements);
                
        var outPortsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_outPorts, m_outPortsContainerSupplyException);
        if(outPortsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(MetaNodeDef.Attribute.OUT_PORTS, outPortsLoadExceptionTree);
        }
        
        return new DefaultMetaNodeDef(this);
    }    

}
