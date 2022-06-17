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

import org.knime.shared.workflow.def.BoundsDef;
import org.knime.shared.workflow.def.CipherDef;
import org.knime.shared.workflow.def.ComponentDialogSettingsDef;
import org.knime.shared.workflow.def.ComponentMetadataDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.PortDef;
import org.knime.shared.workflow.def.TemplateLinkDef;
import org.knime.shared.workflow.def.TemplateMetadataDef;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.def.impl.SingleNodeDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.ComponentNodeDef;
// for types that define enums
import org.knime.shared.workflow.def.ComponentNodeDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * A node that contains a workflow. Similar to a metanode, except it has more flexibility, e.g., filtering the incoming and outgoing flow variables.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class ComponentNodeDefBuilder {

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
    public ComponentNodeDefBuilder strict(){
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
    Map<ComponentNodeDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ComponentNodeDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Optional<Integer> m_id = Optional.empty();
    

    NodeTypeEnum m_nodeType;
    

    Optional<String> m_customDescription = Optional.empty();
    

    Optional<NodeAnnotationDef> m_annotation = Optional.empty();
    
    Optional<BoundsDef> m_bounds = Optional.empty();
    
    Optional<NodeLocksDef> m_locks = Optional.empty();
    
    Optional<JobManagerDef> m_jobManager = Optional.empty();
    
    Optional<ConfigMapDef> m_modelSettings = Optional.empty();
    
    Optional<ConfigMapDef> m_internalNodeSubSettings = Optional.empty();
    
    Optional<ConfigMapDef> m_variableSettings = Optional.empty();
    
    WorkflowDef m_workflow;
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.List<PortDef>> m_inPorts = Optional.of(java.util.List.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     * Setting elements individually is optional.
     */
    Optional<java.util.List<PortDef>> m_inPortsIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setInPorts these are added to m_inPorts in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.List<PortDef>> m_inPortsBulkElements = Optional.empty();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_inPortsContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.List<PortDef>> m_outPorts = Optional.of(java.util.List.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     * Setting elements individually is optional.
     */
    Optional<java.util.List<PortDef>> m_outPortsIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setOutPorts these are added to m_outPorts in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.List<PortDef>> m_outPortsBulkElements = Optional.empty();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_outPortsContainerSupplyException; 
    
    Optional<CipherDef> m_cipher = Optional.empty();
    
    Integer m_virtualInNodeId;
    

    Integer m_virtualOutNodeId;
    

    Optional<ComponentMetadataDef> m_metadata = Optional.empty();
    
    Optional<TemplateMetadataDef> m_templateMetadata = Optional.empty();
    
    Optional<TemplateLinkDef> m_templateLink = Optional.empty();
    
    Optional<ComponentDialogSettingsDef> m_dialogSettings = Optional.empty();
    
    /**
     * Create a new builder.
     */
    public ComponentNodeDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ComponentNodeDefBuilder(final ComponentNodeDef toCopy) {
        setId(toCopy.getId().orElse(null));
        setNodeType(toCopy.getNodeType());
        setCustomDescription(toCopy.getCustomDescription().orElse(null));
        setAnnotation(toCopy.getAnnotation().orElse(null));
        setBounds(toCopy.getBounds().orElse(null));
        setLocks(toCopy.getLocks().orElse(null));
        setJobManager(toCopy.getJobManager().orElse(null));
        setModelSettings(toCopy.getModelSettings().orElse(null));
        setInternalNodeSubSettings(toCopy.getInternalNodeSubSettings().orElse(null));
        setVariableSettings(toCopy.getVariableSettings().orElse(null));
        setWorkflow(toCopy.getWorkflow());
        setInPorts(toCopy.getInPorts().orElse(null));
        setOutPorts(toCopy.getOutPorts().orElse(null));
        setCipher(toCopy.getCipher().orElse(null));
        setVirtualInNodeId(toCopy.getVirtualInNodeId());
        setVirtualOutNodeId(toCopy.getVirtualOutNodeId());
        setMetadata(toCopy.getMetadata().orElse(null));
        setTemplateMetadata(toCopy.getTemplateMetadata().orElse(null));
        setTemplateLink(toCopy.getTemplateLink().orElse(null));
        setDialogSettings(toCopy.getDialogSettings().orElse(null));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for id
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param id Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. Standalone metanodes and components do not have an id since they have no containing workflow. This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setId(final Integer id) {
        setId(() -> id, id);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.ID)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.ID)} will return the exception.
     * 
     * @param id see {@link ComponentNodeDef#getId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setId(Integer)
     */
    public ComponentNodeDefBuilder setId(final FallibleSupplier<Integer> id) {
        setId(id, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.ID)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.ID)} will return the exception.
     * 
     * @param id see {@link ComponentNodeDef#getId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setId(Integer)
     */
    public ComponentNodeDefBuilder setId(final FallibleSupplier<Integer> id, Integer defaultValue) {
        java.util.Objects.requireNonNull(id, () -> "No supplier for id provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.ID);
        try {
            var supplied = id.get();
            m_id = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_id = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.ID, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
    public ComponentNodeDefBuilder setNodeType(final NodeTypeEnum nodeType) {
        setNodeType(() -> nodeType, nodeType);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.NODE_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.NODE_TYPE)} will return the exception.
     * 
     * @param nodeType see {@link ComponentNodeDef#getNodeType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setNodeType(NodeTypeEnum)
     */
    public ComponentNodeDefBuilder setNodeType(final FallibleSupplier<NodeTypeEnum> nodeType, NodeTypeEnum defaultValue) {
        java.util.Objects.requireNonNull(nodeType, () -> "No supplier for nodeType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.NODE_TYPE);
        try {
            var supplied = nodeType.get();
            m_nodeType = supplied;

            if(m_nodeType == null) {
                throw new IllegalArgumentException("nodeType is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_nodeType = defaultValue;
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.NODE_TYPE, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for customDescription
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param customDescription A longer description, provided by the user This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setCustomDescription(final String customDescription) {
        setCustomDescription(() -> customDescription, customDescription);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return the exception.
     * 
     * @param customDescription see {@link ComponentNodeDef#getCustomDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCustomDescription(String)
     */
    public ComponentNodeDefBuilder setCustomDescription(final FallibleSupplier<String> customDescription) {
        setCustomDescription(customDescription, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return the exception.
     * 
     * @param customDescription see {@link ComponentNodeDef#getCustomDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCustomDescription(String)
     */
    public ComponentNodeDefBuilder setCustomDescription(final FallibleSupplier<String> customDescription, String defaultValue) {
        java.util.Objects.requireNonNull(customDescription, () -> "No supplier for customDescription provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.CUSTOM_DESCRIPTION);
        try {
            var supplied = customDescription.get();
            m_customDescription = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_customDescription = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.CUSTOM_DESCRIPTION, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for annotation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param annotation  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setAnnotation(final NodeAnnotationDef annotation) {
        setAnnotation(() -> annotation, annotation);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.ANNOTATION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.ANNOTATION)} will return the exception.
     * 
     * @param annotation see {@link ComponentNodeDef#getAnnotation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotation(NodeAnnotationDef)
     */
    public ComponentNodeDefBuilder setAnnotation(final FallibleSupplier<NodeAnnotationDef> annotation) {
        setAnnotation(annotation, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.ANNOTATION)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.ANNOTATION)} will return the exception.
     * 
     * @param annotation see {@link ComponentNodeDef#getAnnotation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotation(NodeAnnotationDef)
     */
    public ComponentNodeDefBuilder setAnnotation(final FallibleSupplier<NodeAnnotationDef> annotation, NodeAnnotationDef defaultValue) {
        java.util.Objects.requireNonNull(annotation, () -> "No supplier for annotation provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.ANNOTATION);
        try {
            var supplied = annotation.get();
            m_annotation = Optional.ofNullable(supplied);
            if (m_annotation.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_annotation.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.ANNOTATION, (LoadExceptionTree<?>)m_annotation.get());
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
            m_annotation = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.ANNOTATION, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for bounds
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param bounds  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setBounds(final BoundsDef bounds) {
        setBounds(() -> bounds, bounds);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.BOUNDS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.BOUNDS)} will return the exception.
     * 
     * @param bounds see {@link ComponentNodeDef#getBounds}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBounds(BoundsDef)
     */
    public ComponentNodeDefBuilder setBounds(final FallibleSupplier<BoundsDef> bounds) {
        setBounds(bounds, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.BOUNDS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.BOUNDS)} will return the exception.
     * 
     * @param bounds see {@link ComponentNodeDef#getBounds}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBounds(BoundsDef)
     */
    public ComponentNodeDefBuilder setBounds(final FallibleSupplier<BoundsDef> bounds, BoundsDef defaultValue) {
        java.util.Objects.requireNonNull(bounds, () -> "No supplier for bounds provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.BOUNDS);
        try {
            var supplied = bounds.get();
            m_bounds = Optional.ofNullable(supplied);
            if (m_bounds.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_bounds.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.BOUNDS, (LoadExceptionTree<?>)m_bounds.get());
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
            m_bounds = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.BOUNDS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for locks
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param locks  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setLocks(final NodeLocksDef locks) {
        setLocks(() -> locks, locks);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.LOCKS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.LOCKS)} will return the exception.
     * 
     * @param locks see {@link ComponentNodeDef#getLocks}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocks(NodeLocksDef)
     */
    public ComponentNodeDefBuilder setLocks(final FallibleSupplier<NodeLocksDef> locks) {
        setLocks(locks, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.LOCKS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.LOCKS)} will return the exception.
     * 
     * @param locks see {@link ComponentNodeDef#getLocks}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocks(NodeLocksDef)
     */
    public ComponentNodeDefBuilder setLocks(final FallibleSupplier<NodeLocksDef> locks, NodeLocksDef defaultValue) {
        java.util.Objects.requireNonNull(locks, () -> "No supplier for locks provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.LOCKS);
        try {
            var supplied = locks.get();
            m_locks = Optional.ofNullable(supplied);
            if (m_locks.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_locks.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.LOCKS, (LoadExceptionTree<?>)m_locks.get());
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
            m_locks = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.LOCKS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for jobManager
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param jobManager  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setJobManager(final JobManagerDef jobManager) {
        setJobManager(() -> jobManager, jobManager);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.JOB_MANAGER)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.JOB_MANAGER)} will return the exception.
     * 
     * @param jobManager see {@link ComponentNodeDef#getJobManager}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setJobManager(JobManagerDef)
     */
    public ComponentNodeDefBuilder setJobManager(final FallibleSupplier<JobManagerDef> jobManager) {
        setJobManager(jobManager, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.JOB_MANAGER)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.JOB_MANAGER)} will return the exception.
     * 
     * @param jobManager see {@link ComponentNodeDef#getJobManager}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setJobManager(JobManagerDef)
     */
    public ComponentNodeDefBuilder setJobManager(final FallibleSupplier<JobManagerDef> jobManager, JobManagerDef defaultValue) {
        java.util.Objects.requireNonNull(jobManager, () -> "No supplier for jobManager provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.JOB_MANAGER);
        try {
            var supplied = jobManager.get();
            m_jobManager = Optional.ofNullable(supplied);
            if (m_jobManager.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_jobManager.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.JOB_MANAGER, (LoadExceptionTree<?>)m_jobManager.get());
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
            m_jobManager = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.JOB_MANAGER, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for modelSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param modelSettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setModelSettings(final ConfigMapDef modelSettings) {
        setModelSettings(() -> modelSettings, modelSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.MODEL_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.MODEL_SETTINGS)} will return the exception.
     * 
     * @param modelSettings see {@link ComponentNodeDef#getModelSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setModelSettings(ConfigMapDef)
     */
    public ComponentNodeDefBuilder setModelSettings(final FallibleSupplier<ConfigMapDef> modelSettings) {
        setModelSettings(modelSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.MODEL_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.MODEL_SETTINGS)} will return the exception.
     * 
     * @param modelSettings see {@link ComponentNodeDef#getModelSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setModelSettings(ConfigMapDef)
     */
    public ComponentNodeDefBuilder setModelSettings(final FallibleSupplier<ConfigMapDef> modelSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(modelSettings, () -> "No supplier for modelSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.MODEL_SETTINGS);
        try {
            var supplied = modelSettings.get();
            m_modelSettings = Optional.ofNullable(supplied);
            if (m_modelSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_modelSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.MODEL_SETTINGS, (LoadExceptionTree<?>)m_modelSettings.get());
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
            m_modelSettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.MODEL_SETTINGS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for internalNodeSubSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param internalNodeSubSettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setInternalNodeSubSettings(final ConfigMapDef internalNodeSubSettings) {
        setInternalNodeSubSettings(() -> internalNodeSubSettings, internalNodeSubSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return the exception.
     * 
     * @param internalNodeSubSettings see {@link ComponentNodeDef#getInternalNodeSubSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setInternalNodeSubSettings(ConfigMapDef)
     */
    public ComponentNodeDefBuilder setInternalNodeSubSettings(final FallibleSupplier<ConfigMapDef> internalNodeSubSettings) {
        setInternalNodeSubSettings(internalNodeSubSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return the exception.
     * 
     * @param internalNodeSubSettings see {@link ComponentNodeDef#getInternalNodeSubSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setInternalNodeSubSettings(ConfigMapDef)
     */
    public ComponentNodeDefBuilder setInternalNodeSubSettings(final FallibleSupplier<ConfigMapDef> internalNodeSubSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(internalNodeSubSettings, () -> "No supplier for internalNodeSubSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS);
        try {
            var supplied = internalNodeSubSettings.get();
            m_internalNodeSubSettings = Optional.ofNullable(supplied);
            if (m_internalNodeSubSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_internalNodeSubSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS, (LoadExceptionTree<?>)m_internalNodeSubSettings.get());
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
            m_internalNodeSubSettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for variableSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param variableSettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setVariableSettings(final ConfigMapDef variableSettings) {
        setVariableSettings(() -> variableSettings, variableSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.VARIABLE_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.VARIABLE_SETTINGS)} will return the exception.
     * 
     * @param variableSettings see {@link ComponentNodeDef#getVariableSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVariableSettings(ConfigMapDef)
     */
    public ComponentNodeDefBuilder setVariableSettings(final FallibleSupplier<ConfigMapDef> variableSettings) {
        setVariableSettings(variableSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.VARIABLE_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.VARIABLE_SETTINGS)} will return the exception.
     * 
     * @param variableSettings see {@link ComponentNodeDef#getVariableSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVariableSettings(ConfigMapDef)
     */
    public ComponentNodeDefBuilder setVariableSettings(final FallibleSupplier<ConfigMapDef> variableSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(variableSettings, () -> "No supplier for variableSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.VARIABLE_SETTINGS);
        try {
            var supplied = variableSettings.get();
            m_variableSettings = Optional.ofNullable(supplied);
            if (m_variableSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_variableSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.VARIABLE_SETTINGS, (LoadExceptionTree<?>)m_variableSettings.get());
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
            m_variableSettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.VARIABLE_SETTINGS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
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
    public ComponentNodeDefBuilder setWorkflow(final WorkflowDef workflow) {
        setWorkflow(() -> workflow, workflow);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.WORKFLOW)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.WORKFLOW)} will return the exception.
     * 
     * @param workflow see {@link ComponentNodeDef#getWorkflow}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWorkflow(WorkflowDef)
     */
    public ComponentNodeDefBuilder setWorkflow(final FallibleSupplier<WorkflowDef> workflow, WorkflowDef defaultValue) {
        java.util.Objects.requireNonNull(workflow, () -> "No supplier for workflow provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.WORKFLOW);
        try {
            var supplied = workflow.get();
            m_workflow = supplied;

            if(m_workflow == null) {
                throw new IllegalArgumentException("workflow is required and must not be null.");
            }
            if (m_workflow instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_workflow).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.WORKFLOW, (LoadExceptionTree<?>)m_workflow);
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
            m_workflow = defaultValue;
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.WORKFLOW, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
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
    public ComponentNodeDefBuilder setInPorts(final java.util.List<PortDef> inPorts) {
        setInPorts(() -> inPorts);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the inPorts list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the inPorts list). 
     * {@code hasExceptions(ComponentNodeDef.Attribute.IN_PORTS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.IN_PORTS)} will return the exception.
     * 
     * @param inPorts see {@link ComponentNodeDef#getInPorts}
     * 
     * @return this builder for fluent API.
     * @see #setInPorts(java.util.List<PortDef>)
     */
    public ComponentNodeDefBuilder setInPorts(final FallibleSupplier<java.util.List<PortDef>> inPorts) {
        java.util.Objects.requireNonNull(inPorts, () -> "No supplier for inPorts provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.IN_PORTS);
        try {
            var supplied = inPorts.get();
            m_inPorts = Optional.ofNullable(supplied);
            // we set m_inPorts in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_inPortsBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_inPortsContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the inPorts list
     * @return this builder for fluent API
     */
    public ComponentNodeDefBuilder addToInPorts(PortDef value){
    	addToInPorts(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentNodeDef#getInPorts}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the inPorts list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentNodeDefBuilder addToInPorts(FallibleSupplier<PortDef> value, PortDef defaultValue) {
        // we're always adding an element (to have something to link the exception to), so make sure the list is present
        if(m_inPortsIndividualElements.isEmpty()) m_inPortsIndividualElements = Optional.of(new java.util.ArrayList<>());
        PortDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultPortDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_inPortsIndividualElements.get().add(toAdd);
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
    public ComponentNodeDefBuilder setOutPorts(final java.util.List<PortDef> outPorts) {
        setOutPorts(() -> outPorts);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the outPorts list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the outPorts list). 
     * {@code hasExceptions(ComponentNodeDef.Attribute.OUT_PORTS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.OUT_PORTS)} will return the exception.
     * 
     * @param outPorts see {@link ComponentNodeDef#getOutPorts}
     * 
     * @return this builder for fluent API.
     * @see #setOutPorts(java.util.List<PortDef>)
     */
    public ComponentNodeDefBuilder setOutPorts(final FallibleSupplier<java.util.List<PortDef>> outPorts) {
        java.util.Objects.requireNonNull(outPorts, () -> "No supplier for outPorts provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.OUT_PORTS);
        try {
            var supplied = outPorts.get();
            m_outPorts = Optional.ofNullable(supplied);
            // we set m_outPorts in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_outPortsBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_outPortsContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the outPorts list
     * @return this builder for fluent API
     */
    public ComponentNodeDefBuilder addToOutPorts(PortDef value){
    	addToOutPorts(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ComponentNodeDef#getOutPorts}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the outPorts list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ComponentNodeDefBuilder addToOutPorts(FallibleSupplier<PortDef> value, PortDef defaultValue) {
        // we're always adding an element (to have something to link the exception to), so make sure the list is present
        if(m_outPortsIndividualElements.isEmpty()) m_outPortsIndividualElements = Optional.of(new java.util.ArrayList<>());
        PortDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultPortDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_outPortsIndividualElements.get().add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for cipher
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param cipher  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setCipher(final CipherDef cipher) {
        setCipher(() -> cipher, cipher);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.CIPHER)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.CIPHER)} will return the exception.
     * 
     * @param cipher see {@link ComponentNodeDef#getCipher}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCipher(CipherDef)
     */
    public ComponentNodeDefBuilder setCipher(final FallibleSupplier<CipherDef> cipher) {
        setCipher(cipher, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.CIPHER)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.CIPHER)} will return the exception.
     * 
     * @param cipher see {@link ComponentNodeDef#getCipher}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCipher(CipherDef)
     */
    public ComponentNodeDefBuilder setCipher(final FallibleSupplier<CipherDef> cipher, CipherDef defaultValue) {
        java.util.Objects.requireNonNull(cipher, () -> "No supplier for cipher provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.CIPHER);
        try {
            var supplied = cipher.get();
            m_cipher = Optional.ofNullable(supplied);
            if (m_cipher.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_cipher.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.CIPHER, (LoadExceptionTree<?>)m_cipher.get());
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
            m_cipher = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.CIPHER, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
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
    public ComponentNodeDefBuilder setVirtualInNodeId(final Integer virtualInNodeId) {
        setVirtualInNodeId(() -> virtualInNodeId, virtualInNodeId);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.VIRTUAL_IN_NODE_ID)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.VIRTUAL_IN_NODE_ID)} will return the exception.
     * 
     * @param virtualInNodeId see {@link ComponentNodeDef#getVirtualInNodeId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVirtualInNodeId(Integer)
     */
    public ComponentNodeDefBuilder setVirtualInNodeId(final FallibleSupplier<Integer> virtualInNodeId, Integer defaultValue) {
        java.util.Objects.requireNonNull(virtualInNodeId, () -> "No supplier for virtualInNodeId provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.VIRTUAL_IN_NODE_ID);
        try {
            var supplied = virtualInNodeId.get();
            m_virtualInNodeId = supplied;

            if(m_virtualInNodeId == null) {
                throw new IllegalArgumentException("virtualInNodeId is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_virtualInNodeId = defaultValue;
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.VIRTUAL_IN_NODE_ID, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
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
    public ComponentNodeDefBuilder setVirtualOutNodeId(final Integer virtualOutNodeId) {
        setVirtualOutNodeId(() -> virtualOutNodeId, virtualOutNodeId);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.VIRTUAL_OUT_NODE_ID)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.VIRTUAL_OUT_NODE_ID)} will return the exception.
     * 
     * @param virtualOutNodeId see {@link ComponentNodeDef#getVirtualOutNodeId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVirtualOutNodeId(Integer)
     */
    public ComponentNodeDefBuilder setVirtualOutNodeId(final FallibleSupplier<Integer> virtualOutNodeId, Integer defaultValue) {
        java.util.Objects.requireNonNull(virtualOutNodeId, () -> "No supplier for virtualOutNodeId provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.VIRTUAL_OUT_NODE_ID);
        try {
            var supplied = virtualOutNodeId.get();
            m_virtualOutNodeId = supplied;

            if(m_virtualOutNodeId == null) {
                throw new IllegalArgumentException("virtualOutNodeId is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_virtualOutNodeId = defaultValue;
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.VIRTUAL_OUT_NODE_ID, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for metadata
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param metadata  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setMetadata(final ComponentMetadataDef metadata) {
        setMetadata(() -> metadata, metadata);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.METADATA)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.METADATA)} will return the exception.
     * 
     * @param metadata see {@link ComponentNodeDef#getMetadata}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setMetadata(ComponentMetadataDef)
     */
    public ComponentNodeDefBuilder setMetadata(final FallibleSupplier<ComponentMetadataDef> metadata) {
        setMetadata(metadata, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.METADATA)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.METADATA)} will return the exception.
     * 
     * @param metadata see {@link ComponentNodeDef#getMetadata}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setMetadata(ComponentMetadataDef)
     */
    public ComponentNodeDefBuilder setMetadata(final FallibleSupplier<ComponentMetadataDef> metadata, ComponentMetadataDef defaultValue) {
        java.util.Objects.requireNonNull(metadata, () -> "No supplier for metadata provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.METADATA);
        try {
            var supplied = metadata.get();
            m_metadata = Optional.ofNullable(supplied);
            if (m_metadata.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_metadata.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.METADATA, (LoadExceptionTree<?>)m_metadata.get());
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
            m_metadata = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.METADATA, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for templateMetadata
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param templateMetadata  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setTemplateMetadata(final TemplateMetadataDef templateMetadata) {
        setTemplateMetadata(() -> templateMetadata, templateMetadata);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.TEMPLATE_METADATA)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.TEMPLATE_METADATA)} will return the exception.
     * 
     * @param templateMetadata see {@link ComponentNodeDef#getTemplateMetadata}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTemplateMetadata(TemplateMetadataDef)
     */
    public ComponentNodeDefBuilder setTemplateMetadata(final FallibleSupplier<TemplateMetadataDef> templateMetadata) {
        setTemplateMetadata(templateMetadata, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.TEMPLATE_METADATA)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.TEMPLATE_METADATA)} will return the exception.
     * 
     * @param templateMetadata see {@link ComponentNodeDef#getTemplateMetadata}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTemplateMetadata(TemplateMetadataDef)
     */
    public ComponentNodeDefBuilder setTemplateMetadata(final FallibleSupplier<TemplateMetadataDef> templateMetadata, TemplateMetadataDef defaultValue) {
        java.util.Objects.requireNonNull(templateMetadata, () -> "No supplier for templateMetadata provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.TEMPLATE_METADATA);
        try {
            var supplied = templateMetadata.get();
            m_templateMetadata = Optional.ofNullable(supplied);
            if (m_templateMetadata.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_templateMetadata.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.TEMPLATE_METADATA, (LoadExceptionTree<?>)m_templateMetadata.get());
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
            m_templateMetadata = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.TEMPLATE_METADATA, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for templateLink
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param templateLink  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setTemplateLink(final TemplateLinkDef templateLink) {
        setTemplateLink(() -> templateLink, templateLink);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.TEMPLATE_LINK)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.TEMPLATE_LINK)} will return the exception.
     * 
     * @param templateLink see {@link ComponentNodeDef#getTemplateLink}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTemplateLink(TemplateLinkDef)
     */
    public ComponentNodeDefBuilder setTemplateLink(final FallibleSupplier<TemplateLinkDef> templateLink) {
        setTemplateLink(templateLink, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.TEMPLATE_LINK)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.TEMPLATE_LINK)} will return the exception.
     * 
     * @param templateLink see {@link ComponentNodeDef#getTemplateLink}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTemplateLink(TemplateLinkDef)
     */
    public ComponentNodeDefBuilder setTemplateLink(final FallibleSupplier<TemplateLinkDef> templateLink, TemplateLinkDef defaultValue) {
        java.util.Objects.requireNonNull(templateLink, () -> "No supplier for templateLink provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.TEMPLATE_LINK);
        try {
            var supplied = templateLink.get();
            m_templateLink = Optional.ofNullable(supplied);
            if (m_templateLink.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_templateLink.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.TEMPLATE_LINK, (LoadExceptionTree<?>)m_templateLink.get());
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
            m_templateLink = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.TEMPLATE_LINK, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for dialogSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param dialogSettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentNodeDefBuilder setDialogSettings(final ComponentDialogSettingsDef dialogSettings) {
        setDialogSettings(() -> dialogSettings, dialogSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.DIALOG_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.DIALOG_SETTINGS)} will return the exception.
     * 
     * @param dialogSettings see {@link ComponentNodeDef#getDialogSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDialogSettings(ComponentDialogSettingsDef)
     */
    public ComponentNodeDefBuilder setDialogSettings(final FallibleSupplier<ComponentDialogSettingsDef> dialogSettings) {
        setDialogSettings(dialogSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentNodeDef.Attribute.DIALOG_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(ComponentNodeDef.Attribute.DIALOG_SETTINGS)} will return the exception.
     * 
     * @param dialogSettings see {@link ComponentNodeDef#getDialogSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDialogSettings(ComponentDialogSettingsDef)
     */
    public ComponentNodeDefBuilder setDialogSettings(final FallibleSupplier<ComponentDialogSettingsDef> dialogSettings, ComponentDialogSettingsDef defaultValue) {
        java.util.Objects.requireNonNull(dialogSettings, () -> "No supplier for dialogSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentNodeDef.Attribute.DIALOG_SETTINGS);
        try {
            var supplied = dialogSettings.get();
            m_dialogSettings = Optional.ofNullable(supplied);
            if (m_dialogSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_dialogSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(ComponentNodeDef.Attribute.DIALOG_SETTINGS, (LoadExceptionTree<?>)m_dialogSettings.get());
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
            m_dialogSettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.DIALOG_SETTINGS, exceptionTree);
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
	 * @return the {@link ComponentNodeDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultComponentNodeDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_nodeType == null) setNodeType( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_workflow == null) setWorkflow( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_virtualInNodeId == null) setVirtualInNodeId( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_virtualOutNodeId == null) setVirtualOutNodeId( null);
        
    	
        // if bulk elements are present, add them to individual elements
        if(m_inPortsBulkElements.isPresent()){
            if(m_inPortsIndividualElements.isEmpty()) {
                m_inPortsIndividualElements = Optional.of(new java.util.ArrayList<>());
            }
            m_inPortsIndividualElements.get().addAll(m_inPortsBulkElements.get());    
        }
        m_inPorts = m_inPortsIndividualElements;        
        
                
        var inPortsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_inPorts.orElse(new java.util.ArrayList<>()), m_inPortsContainerSupplyException);
        if(inPortsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.IN_PORTS, inPortsLoadExceptionTree);
        }
        
        // if bulk elements are present, add them to individual elements
        if(m_outPortsBulkElements.isPresent()){
            if(m_outPortsIndividualElements.isEmpty()) {
                m_outPortsIndividualElements = Optional.of(new java.util.ArrayList<>());
            }
            m_outPortsIndividualElements.get().addAll(m_outPortsBulkElements.get());    
        }
        m_outPorts = m_outPortsIndividualElements;        
        
                
        var outPortsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_outPorts.orElse(new java.util.ArrayList<>()), m_outPortsContainerSupplyException);
        if(outPortsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ComponentNodeDef.Attribute.OUT_PORTS, outPortsLoadExceptionTree);
        }
        
        return new DefaultComponentNodeDef(this);
    }    

}
