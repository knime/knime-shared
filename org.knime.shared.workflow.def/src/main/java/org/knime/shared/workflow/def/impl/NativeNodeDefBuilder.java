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
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.FilestoreDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.VendorDef;
import org.knime.shared.workflow.def.impl.SingleNodeDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.NativeNodeDef;
// for types that define enums
import org.knime.shared.workflow.def.NativeNodeDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * The basic executable building block of a workflow. 
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class NativeNodeDefBuilder {

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
    public NativeNodeDefBuilder strict(){
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
    Map<NativeNodeDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(NativeNodeDef.Attribute.class);

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
    
    String m_nodeName;
    

    String m_factory;
    

    Optional<ConfigMapDef> m_factorySettings = Optional.empty();
    
    VendorDef m_feature;
    
    VendorDef m_bundle;
    
    Optional<ConfigMapDef> m_nodeCreationConfig = Optional.empty();
    
    Optional<FilestoreDef> m_filestore = Optional.empty();
    
    /**
     * Create a new builder.
     */
    public NativeNodeDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public NativeNodeDefBuilder(final NativeNodeDef toCopy) {
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
        setNodeName(toCopy.getNodeName());
        setFactory(toCopy.getFactory());
        setFactorySettings(toCopy.getFactorySettings().orElse(null));
        setFeature(toCopy.getFeature());
        setBundle(toCopy.getBundle());
        setNodeCreationConfig(toCopy.getNodeCreationConfig().orElse(null));
        setFilestore(toCopy.getFilestore().orElse(null));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for id
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param id Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. Standalone metanodes and components do not have an id since they have no containing workflow. This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setId(final Integer id) {
        setId(() -> id, id);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.ID)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.ID)} will return the exception.
     * 
     * @param id see {@link NativeNodeDef#getId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setId(Integer)
     */
    public NativeNodeDefBuilder setId(final FallibleSupplier<Integer> id) {
        setId(id, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.ID)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.ID)} will return the exception.
     * 
     * @param id see {@link NativeNodeDef#getId}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setId(Integer)
     */
    public NativeNodeDefBuilder setId(final FallibleSupplier<Integer> id, Integer defaultValue) {
        java.util.Objects.requireNonNull(id, () -> "No supplier for id provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.ID);
        try {
            var supplied = id.get();
            m_id = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_id = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(NativeNodeDef.Attribute.ID, supplyException);
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
    public NativeNodeDefBuilder setNodeType(final NodeTypeEnum nodeType) {
        setNodeType(() -> nodeType, nodeType);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.NODE_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.NODE_TYPE)} will return the exception.
     * 
     * @param nodeType see {@link NativeNodeDef#getNodeType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setNodeType(NodeTypeEnum)
     */
    public NativeNodeDefBuilder setNodeType(final FallibleSupplier<NodeTypeEnum> nodeType, NodeTypeEnum defaultValue) {
        java.util.Objects.requireNonNull(nodeType, () -> "No supplier for nodeType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.NODE_TYPE);
        try {
            var supplied = nodeType.get();
            m_nodeType = supplied;

            if(m_nodeType == null) {
                throw new IllegalArgumentException("nodeType is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_nodeType = defaultValue;
            m_exceptionalChildren.put(NativeNodeDef.Attribute.NODE_TYPE, supplyException);
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
    public NativeNodeDefBuilder setCustomDescription(final String customDescription) {
        setCustomDescription(() -> customDescription, customDescription);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return the exception.
     * 
     * @param customDescription see {@link NativeNodeDef#getCustomDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCustomDescription(String)
     */
    public NativeNodeDefBuilder setCustomDescription(final FallibleSupplier<String> customDescription) {
        setCustomDescription(customDescription, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.CUSTOM_DESCRIPTION)} will return the exception.
     * 
     * @param customDescription see {@link NativeNodeDef#getCustomDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCustomDescription(String)
     */
    public NativeNodeDefBuilder setCustomDescription(final FallibleSupplier<String> customDescription, String defaultValue) {
        java.util.Objects.requireNonNull(customDescription, () -> "No supplier for customDescription provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.CUSTOM_DESCRIPTION);
        try {
            var supplied = customDescription.get();
            m_customDescription = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_customDescription = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(NativeNodeDef.Attribute.CUSTOM_DESCRIPTION, supplyException);
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
    public NativeNodeDefBuilder setAnnotation(final NodeAnnotationDef annotation) {
        setAnnotation(() -> annotation, annotation);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.ANNOTATION)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.ANNOTATION)} will return the exception.
     * 
     * @param annotation see {@link NativeNodeDef#getAnnotation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotation(NodeAnnotationDef)
     */
    public NativeNodeDefBuilder setAnnotation(final FallibleSupplier<NodeAnnotationDef> annotation) {
        setAnnotation(annotation, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.ANNOTATION)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.ANNOTATION)} will return the exception.
     * 
     * @param annotation see {@link NativeNodeDef#getAnnotation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAnnotation(NodeAnnotationDef)
     */
    public NativeNodeDefBuilder setAnnotation(final FallibleSupplier<NodeAnnotationDef> annotation, NodeAnnotationDef defaultValue) {
        java.util.Objects.requireNonNull(annotation, () -> "No supplier for annotation provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.ANNOTATION);
        try {
            var supplied = annotation.get();
            m_annotation = Optional.ofNullable(supplied);
            if (m_annotation.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_annotation.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.ANNOTATION, (LoadExceptionTree<?>)m_annotation.get());
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
            m_exceptionalChildren.put(NativeNodeDef.Attribute.ANNOTATION, exceptionTree);
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
    public NativeNodeDefBuilder setBounds(final BoundsDef bounds) {
        setBounds(() -> bounds, bounds);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.BOUNDS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.BOUNDS)} will return the exception.
     * 
     * @param bounds see {@link NativeNodeDef#getBounds}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBounds(BoundsDef)
     */
    public NativeNodeDefBuilder setBounds(final FallibleSupplier<BoundsDef> bounds) {
        setBounds(bounds, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.BOUNDS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.BOUNDS)} will return the exception.
     * 
     * @param bounds see {@link NativeNodeDef#getBounds}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBounds(BoundsDef)
     */
    public NativeNodeDefBuilder setBounds(final FallibleSupplier<BoundsDef> bounds, BoundsDef defaultValue) {
        java.util.Objects.requireNonNull(bounds, () -> "No supplier for bounds provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.BOUNDS);
        try {
            var supplied = bounds.get();
            m_bounds = Optional.ofNullable(supplied);
            if (m_bounds.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_bounds.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.BOUNDS, (LoadExceptionTree<?>)m_bounds.get());
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
            m_exceptionalChildren.put(NativeNodeDef.Attribute.BOUNDS, exceptionTree);
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
    public NativeNodeDefBuilder setLocks(final NodeLocksDef locks) {
        setLocks(() -> locks, locks);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.LOCKS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.LOCKS)} will return the exception.
     * 
     * @param locks see {@link NativeNodeDef#getLocks}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocks(NodeLocksDef)
     */
    public NativeNodeDefBuilder setLocks(final FallibleSupplier<NodeLocksDef> locks) {
        setLocks(locks, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.LOCKS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.LOCKS)} will return the exception.
     * 
     * @param locks see {@link NativeNodeDef#getLocks}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLocks(NodeLocksDef)
     */
    public NativeNodeDefBuilder setLocks(final FallibleSupplier<NodeLocksDef> locks, NodeLocksDef defaultValue) {
        java.util.Objects.requireNonNull(locks, () -> "No supplier for locks provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.LOCKS);
        try {
            var supplied = locks.get();
            m_locks = Optional.ofNullable(supplied);
            if (m_locks.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_locks.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.LOCKS, (LoadExceptionTree<?>)m_locks.get());
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
            m_exceptionalChildren.put(NativeNodeDef.Attribute.LOCKS, exceptionTree);
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
    public NativeNodeDefBuilder setJobManager(final JobManagerDef jobManager) {
        setJobManager(() -> jobManager, jobManager);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.JOB_MANAGER)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.JOB_MANAGER)} will return the exception.
     * 
     * @param jobManager see {@link NativeNodeDef#getJobManager}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setJobManager(JobManagerDef)
     */
    public NativeNodeDefBuilder setJobManager(final FallibleSupplier<JobManagerDef> jobManager) {
        setJobManager(jobManager, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.JOB_MANAGER)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.JOB_MANAGER)} will return the exception.
     * 
     * @param jobManager see {@link NativeNodeDef#getJobManager}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setJobManager(JobManagerDef)
     */
    public NativeNodeDefBuilder setJobManager(final FallibleSupplier<JobManagerDef> jobManager, JobManagerDef defaultValue) {
        java.util.Objects.requireNonNull(jobManager, () -> "No supplier for jobManager provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.JOB_MANAGER);
        try {
            var supplied = jobManager.get();
            m_jobManager = Optional.ofNullable(supplied);
            if (m_jobManager.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_jobManager.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.JOB_MANAGER, (LoadExceptionTree<?>)m_jobManager.get());
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
            m_exceptionalChildren.put(NativeNodeDef.Attribute.JOB_MANAGER, exceptionTree);
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
    public NativeNodeDefBuilder setModelSettings(final ConfigMapDef modelSettings) {
        setModelSettings(() -> modelSettings, modelSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.MODEL_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.MODEL_SETTINGS)} will return the exception.
     * 
     * @param modelSettings see {@link NativeNodeDef#getModelSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setModelSettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setModelSettings(final FallibleSupplier<ConfigMapDef> modelSettings) {
        setModelSettings(modelSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.MODEL_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.MODEL_SETTINGS)} will return the exception.
     * 
     * @param modelSettings see {@link NativeNodeDef#getModelSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setModelSettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setModelSettings(final FallibleSupplier<ConfigMapDef> modelSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(modelSettings, () -> "No supplier for modelSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.MODEL_SETTINGS);
        try {
            var supplied = modelSettings.get();
            m_modelSettings = Optional.ofNullable(supplied);
            if (m_modelSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_modelSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.MODEL_SETTINGS, (LoadExceptionTree<?>)m_modelSettings.get());
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
            m_exceptionalChildren.put(NativeNodeDef.Attribute.MODEL_SETTINGS, exceptionTree);
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
    public NativeNodeDefBuilder setInternalNodeSubSettings(final ConfigMapDef internalNodeSubSettings) {
        setInternalNodeSubSettings(() -> internalNodeSubSettings, internalNodeSubSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return the exception.
     * 
     * @param internalNodeSubSettings see {@link NativeNodeDef#getInternalNodeSubSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setInternalNodeSubSettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setInternalNodeSubSettings(final FallibleSupplier<ConfigMapDef> internalNodeSubSettings) {
        setInternalNodeSubSettings(internalNodeSubSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS)} will return the exception.
     * 
     * @param internalNodeSubSettings see {@link NativeNodeDef#getInternalNodeSubSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setInternalNodeSubSettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setInternalNodeSubSettings(final FallibleSupplier<ConfigMapDef> internalNodeSubSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(internalNodeSubSettings, () -> "No supplier for internalNodeSubSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS);
        try {
            var supplied = internalNodeSubSettings.get();
            m_internalNodeSubSettings = Optional.ofNullable(supplied);
            if (m_internalNodeSubSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_internalNodeSubSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS, (LoadExceptionTree<?>)m_internalNodeSubSettings.get());
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
            m_exceptionalChildren.put(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS, exceptionTree);
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
    public NativeNodeDefBuilder setVariableSettings(final ConfigMapDef variableSettings) {
        setVariableSettings(() -> variableSettings, variableSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.VARIABLE_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.VARIABLE_SETTINGS)} will return the exception.
     * 
     * @param variableSettings see {@link NativeNodeDef#getVariableSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVariableSettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setVariableSettings(final FallibleSupplier<ConfigMapDef> variableSettings) {
        setVariableSettings(variableSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.VARIABLE_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.VARIABLE_SETTINGS)} will return the exception.
     * 
     * @param variableSettings see {@link NativeNodeDef#getVariableSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVariableSettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setVariableSettings(final FallibleSupplier<ConfigMapDef> variableSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(variableSettings, () -> "No supplier for variableSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.VARIABLE_SETTINGS);
        try {
            var supplied = variableSettings.get();
            m_variableSettings = Optional.ofNullable(supplied);
            if (m_variableSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_variableSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.VARIABLE_SETTINGS, (LoadExceptionTree<?>)m_variableSettings.get());
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
            m_exceptionalChildren.put(NativeNodeDef.Attribute.VARIABLE_SETTINGS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for nodeName
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param nodeName Describes and identifies the node in the node repository 
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setNodeName(final String nodeName) {
        setNodeName(() -> nodeName, nodeName);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.NODE_NAME)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.NODE_NAME)} will return the exception.
     * 
     * @param nodeName see {@link NativeNodeDef#getNodeName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setNodeName(String)
     */
    public NativeNodeDefBuilder setNodeName(final FallibleSupplier<String> nodeName, String defaultValue) {
        java.util.Objects.requireNonNull(nodeName, () -> "No supplier for nodeName provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.NODE_NAME);
        try {
            var supplied = nodeName.get();
            m_nodeName = supplied;

            if(m_nodeName == null) {
                throw new IllegalArgumentException("nodeName is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_nodeName = defaultValue;
            m_exceptionalChildren.put(NativeNodeDef.Attribute.NODE_NAME, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for factory
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param factory Qualified class name 
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setFactory(final String factory) {
        setFactory(() -> factory, factory);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.FACTORY)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.FACTORY)} will return the exception.
     * 
     * @param factory see {@link NativeNodeDef#getFactory}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFactory(String)
     */
    public NativeNodeDefBuilder setFactory(final FallibleSupplier<String> factory, String defaultValue) {
        java.util.Objects.requireNonNull(factory, () -> "No supplier for factory provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.FACTORY);
        try {
            var supplied = factory.get();
            m_factory = supplied;

            if(m_factory == null) {
                throw new IllegalArgumentException("factory is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_factory = defaultValue;
            m_exceptionalChildren.put(NativeNodeDef.Attribute.FACTORY, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for factorySettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param factorySettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setFactorySettings(final ConfigMapDef factorySettings) {
        setFactorySettings(() -> factorySettings, factorySettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.FACTORY_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.FACTORY_SETTINGS)} will return the exception.
     * 
     * @param factorySettings see {@link NativeNodeDef#getFactorySettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFactorySettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setFactorySettings(final FallibleSupplier<ConfigMapDef> factorySettings) {
        setFactorySettings(factorySettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.FACTORY_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.FACTORY_SETTINGS)} will return the exception.
     * 
     * @param factorySettings see {@link NativeNodeDef#getFactorySettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFactorySettings(ConfigMapDef)
     */
    public NativeNodeDefBuilder setFactorySettings(final FallibleSupplier<ConfigMapDef> factorySettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(factorySettings, () -> "No supplier for factorySettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.FACTORY_SETTINGS);
        try {
            var supplied = factorySettings.get();
            m_factorySettings = Optional.ofNullable(supplied);
            if (m_factorySettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_factorySettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.FACTORY_SETTINGS, (LoadExceptionTree<?>)m_factorySettings.get());
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
            m_factorySettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(NativeNodeDef.Attribute.FACTORY_SETTINGS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for feature
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param feature  
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setFeature(final VendorDef feature) {
        setFeature(() -> feature, feature);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.FEATURE)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.FEATURE)} will return the exception.
     * 
     * @param feature see {@link NativeNodeDef#getFeature}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFeature(VendorDef)
     */
    public NativeNodeDefBuilder setFeature(final FallibleSupplier<VendorDef> feature, VendorDef defaultValue) {
        java.util.Objects.requireNonNull(feature, () -> "No supplier for feature provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.FEATURE);
        try {
            var supplied = feature.get();
            m_feature = supplied;

            if(m_feature == null) {
                throw new IllegalArgumentException("feature is required and must not be null.");
            }
            if (m_feature instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_feature).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.FEATURE, (LoadExceptionTree<?>)m_feature);
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
            m_feature = defaultValue;
            m_exceptionalChildren.put(NativeNodeDef.Attribute.FEATURE, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for bundle
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param bundle  
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setBundle(final VendorDef bundle) {
        setBundle(() -> bundle, bundle);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.BUNDLE)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.BUNDLE)} will return the exception.
     * 
     * @param bundle see {@link NativeNodeDef#getBundle}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setBundle(VendorDef)
     */
    public NativeNodeDefBuilder setBundle(final FallibleSupplier<VendorDef> bundle, VendorDef defaultValue) {
        java.util.Objects.requireNonNull(bundle, () -> "No supplier for bundle provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.BUNDLE);
        try {
            var supplied = bundle.get();
            m_bundle = supplied;

            if(m_bundle == null) {
                throw new IllegalArgumentException("bundle is required and must not be null.");
            }
            if (m_bundle instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_bundle).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.BUNDLE, (LoadExceptionTree<?>)m_bundle);
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
            m_bundle = defaultValue;
            m_exceptionalChildren.put(NativeNodeDef.Attribute.BUNDLE, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for nodeCreationConfig
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param nodeCreationConfig  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setNodeCreationConfig(final ConfigMapDef nodeCreationConfig) {
        setNodeCreationConfig(() -> nodeCreationConfig, nodeCreationConfig);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.NODE_CREATION_CONFIG)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.NODE_CREATION_CONFIG)} will return the exception.
     * 
     * @param nodeCreationConfig see {@link NativeNodeDef#getNodeCreationConfig}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setNodeCreationConfig(ConfigMapDef)
     */
    public NativeNodeDefBuilder setNodeCreationConfig(final FallibleSupplier<ConfigMapDef> nodeCreationConfig) {
        setNodeCreationConfig(nodeCreationConfig, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.NODE_CREATION_CONFIG)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.NODE_CREATION_CONFIG)} will return the exception.
     * 
     * @param nodeCreationConfig see {@link NativeNodeDef#getNodeCreationConfig}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setNodeCreationConfig(ConfigMapDef)
     */
    public NativeNodeDefBuilder setNodeCreationConfig(final FallibleSupplier<ConfigMapDef> nodeCreationConfig, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(nodeCreationConfig, () -> "No supplier for nodeCreationConfig provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.NODE_CREATION_CONFIG);
        try {
            var supplied = nodeCreationConfig.get();
            m_nodeCreationConfig = Optional.ofNullable(supplied);
            if (m_nodeCreationConfig.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_nodeCreationConfig.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.NODE_CREATION_CONFIG, (LoadExceptionTree<?>)m_nodeCreationConfig.get());
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
            m_nodeCreationConfig = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(NativeNodeDef.Attribute.NODE_CREATION_CONFIG, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for filestore
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param filestore  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public NativeNodeDefBuilder setFilestore(final FilestoreDef filestore) {
        setFilestore(() -> filestore, filestore);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.FILESTORE)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.FILESTORE)} will return the exception.
     * 
     * @param filestore see {@link NativeNodeDef#getFilestore}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFilestore(FilestoreDef)
     */
    public NativeNodeDefBuilder setFilestore(final FallibleSupplier<FilestoreDef> filestore) {
        setFilestore(filestore, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(NativeNodeDef.Attribute.FILESTORE)} will return true and and
     * {@code getExceptionalChildren().get(NativeNodeDef.Attribute.FILESTORE)} will return the exception.
     * 
     * @param filestore see {@link NativeNodeDef#getFilestore}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setFilestore(FilestoreDef)
     */
    public NativeNodeDefBuilder setFilestore(final FallibleSupplier<FilestoreDef> filestore, FilestoreDef defaultValue) {
        java.util.Objects.requireNonNull(filestore, () -> "No supplier for filestore provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(NativeNodeDef.Attribute.FILESTORE);
        try {
            var supplied = filestore.get();
            m_filestore = Optional.ofNullable(supplied);
            if (m_filestore.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_filestore.get()).hasExceptions()) {
                m_exceptionalChildren.put(NativeNodeDef.Attribute.FILESTORE, (LoadExceptionTree<?>)m_filestore.get());
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
            m_filestore = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(NativeNodeDef.Attribute.FILESTORE, exceptionTree);
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
	 * @return the {@link NativeNodeDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultNativeNodeDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_nodeType == null) setNodeType( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_nodeName == null) setNodeName( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_factory == null) setFactory( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_feature == null) setFeature( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_bundle == null) setBundle( null);
        
    	
        return new DefaultNativeNodeDef(this);
    }    

}
