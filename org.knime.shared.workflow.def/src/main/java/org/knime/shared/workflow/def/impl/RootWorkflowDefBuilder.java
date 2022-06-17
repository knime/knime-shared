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

import org.knime.shared.workflow.def.AnnotationDataDef;
import org.knime.shared.workflow.def.AuthorInformationDef;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.ConnectionDef;
import org.knime.shared.workflow.def.CredentialPlaceholderDef;
import org.knime.shared.workflow.def.FlowVariableDef;
import org.knime.shared.workflow.def.WorkflowUISettingsDef;
import org.knime.shared.workflow.def.impl.WorkflowDefBuilder;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.RootWorkflowDef;
// for types that define enums
import org.knime.shared.workflow.def.RootWorkflowDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * RootWorkflowDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class RootWorkflowDefBuilder {

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
    public RootWorkflowDefBuilder strict(){
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
    Map<RootWorkflowDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(RootWorkflowDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Optional<String> m_name = Optional.empty();
    

    Optional<AuthorInformationDef> m_authorInformation = Optional.empty();
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.Map<String, BaseNodeDef>> m_nodes = Optional.of(java.util.Map.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this map.
     * Setting elements individually is optional.
     */
    Optional<java.util.Map<String, BaseNodeDef>> m_nodesIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setNodes these are added to m_nodes in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.Map<String, BaseNodeDef>> m_nodesBulkElements = Optional.empty();
    /** This exception is merged with the exceptions of the elements of this map into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_nodesContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.List<ConnectionDef>> m_connections = Optional.of(java.util.List.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     * Setting elements individually is optional.
     */
    Optional<java.util.List<ConnectionDef>> m_connectionsIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setConnections these are added to m_connections in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.List<ConnectionDef>> m_connectionsBulkElements = Optional.empty();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_connectionsContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.Map<String, AnnotationDataDef>> m_annotations = Optional.of(java.util.Map.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this map.
     * Setting elements individually is optional.
     */
    Optional<java.util.Map<String, AnnotationDataDef>> m_annotationsIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setAnnotations these are added to m_annotations in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.Map<String, AnnotationDataDef>> m_annotationsBulkElements = Optional.empty();
    /** This exception is merged with the exceptions of the elements of this map into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_annotationsContainerSupplyException; 
    
    Optional<WorkflowUISettingsDef> m_workflowEditorSettings = Optional.empty();
    
    Optional<ConfigMapDef> m_tableBackendSettings = Optional.empty();
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.List<FlowVariableDef>> m_flowVariables = Optional.of(java.util.List.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     * Setting elements individually is optional.
     */
    Optional<java.util.List<FlowVariableDef>> m_flowVariablesIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setFlowVariables these are added to m_flowVariables in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.List<FlowVariableDef>> m_flowVariablesBulkElements = Optional.empty();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_flowVariablesContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     */
    Optional<java.util.List<CredentialPlaceholderDef>> m_credentialPlaceholders = Optional.of(java.util.List.of());
    /** 
     * Temporarily holds onto elements added with convenience methods to add individual elements. 
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     * Setting elements individually is optional.
     */
    Optional<java.util.List<CredentialPlaceholderDef>> m_credentialPlaceholdersIndividualElements = Optional.empty();
    /** 
     * Temporarily holds onto elements set as a whole with setCredentialPlaceholders these are added to m_credentialPlaceholders in build.
     * Setting elements in bulk is optional.
     */
    private Optional<java.util.List<CredentialPlaceholderDef>> m_credentialPlaceholdersBulkElements = Optional.empty();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_credentialPlaceholdersContainerSupplyException; 
    
    /**
     * Create a new builder.
     */
    public RootWorkflowDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public RootWorkflowDefBuilder(final RootWorkflowDef toCopy) {
        setName(toCopy.getName().orElse(null));
        setAuthorInformation(toCopy.getAuthorInformation().orElse(null));
        setNodes(toCopy.getNodes().orElse(null));
        setConnections(toCopy.getConnections().orElse(null));
        setAnnotations(toCopy.getAnnotations().orElse(null));
        setWorkflowEditorSettings(toCopy.getWorkflowEditorSettings().orElse(null));
        setTableBackendSettings(toCopy.getTableBackendSettings().orElse(null));
        setFlowVariables(toCopy.getFlowVariables().orElse(null));
        setCredentialPlaceholders(toCopy.getCredentialPlaceholders().orElse(null));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for name
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param name A user-chosen identifier for the workflow. This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setName(final String name) {
        setName(() -> name, name);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.NAME)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.NAME)} will return the exception.
     * 
     * @param name see {@link RootWorkflowDef#getName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setName(String)
     */
    public RootWorkflowDefBuilder setName(final FallibleSupplier<String> name) {
        setName(name, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.NAME)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.NAME)} will return the exception.
     * 
     * @param name see {@link RootWorkflowDef#getName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setName(String)
     */
    public RootWorkflowDefBuilder setName(final FallibleSupplier<String> name, String defaultValue) {
        java.util.Objects.requireNonNull(name, () -> "No supplier for name provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.NAME);
        try {
            var supplied = name.get();
            m_name = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_name = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.NAME, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for authorInformation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param authorInformation  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setAuthorInformation(final AuthorInformationDef authorInformation) {
        setAuthorInformation(() -> authorInformation, authorInformation);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.AUTHOR_INFORMATION)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.AUTHOR_INFORMATION)} will return the exception.
     * 
     * @param authorInformation see {@link RootWorkflowDef#getAuthorInformation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAuthorInformation(AuthorInformationDef)
     */
    public RootWorkflowDefBuilder setAuthorInformation(final FallibleSupplier<AuthorInformationDef> authorInformation) {
        setAuthorInformation(authorInformation, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.AUTHOR_INFORMATION)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.AUTHOR_INFORMATION)} will return the exception.
     * 
     * @param authorInformation see {@link RootWorkflowDef#getAuthorInformation}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAuthorInformation(AuthorInformationDef)
     */
    public RootWorkflowDefBuilder setAuthorInformation(final FallibleSupplier<AuthorInformationDef> authorInformation, AuthorInformationDef defaultValue) {
        java.util.Objects.requireNonNull(authorInformation, () -> "No supplier for authorInformation provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.AUTHOR_INFORMATION);
        try {
            var supplied = authorInformation.get();
            m_authorInformation = Optional.ofNullable(supplied);
            if (m_authorInformation.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_authorInformation.get()).hasExceptions()) {
                m_exceptionalChildren.put(RootWorkflowDef.Attribute.AUTHOR_INFORMATION, (LoadExceptionTree<?>)m_authorInformation.get());
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
            m_authorInformation = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.AUTHOR_INFORMATION, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for nodes
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the nodes map. 
     * Calling this method again will undo the previous call (it is not additive).
     * A mapping previously or subsequently set with {@link #putToNodes} will replace a mapping added with this method
     * if they have the same key.
     * @param nodes The executable blocks in this workflow.
     * @return this for fluent API
     */
    public RootWorkflowDefBuilder setNodes(final java.util.Map<String, BaseNodeDef> nodes) {
        setNodes(() -> nodes);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the nodes map,
     * whereas exceptions thrown in putTo allows to register a {@link LoadException} 
     * for an individual element of the nodes map). 
     * {@code hasExceptions(RootWorkflowDef.Attribute.NODES)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.NODES)} will return the exception.
     * 
     * @param nodes see {@link RootWorkflowDef#getNodes}
     * 
     * @return this builder for fluent API.
     * @see #setNodes(java.util.Map<String, BaseNodeDef>)
     */
    public RootWorkflowDefBuilder setNodes(final FallibleSupplier<java.util.Map<String, BaseNodeDef>> nodes) {
        java.util.Objects.requireNonNull(nodes, () -> "No supplier for nodes provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.NODES);
        try {
            var supplied = nodes.get();
            m_nodes = Optional.ofNullable(supplied);
            // we set m_nodes in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_nodesBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with map element exceptions into a single LoadExceptionTree in #build()
            m_nodesContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param key the key of the entry to add to the nodes map
     * @param value the value of the entry to add to the nodes map
     * @return this builder for fluent API
     */
    public RootWorkflowDefBuilder putToNodes(String key, BaseNodeDef value){
    	putToNodes(key, () -> value, (BaseNodeDef)null);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the map returned by {@link RootWorkflowDef#getNodes}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the given key.
     *
     * @param key the key for the entry added value in the map returned by {@link RootWorkflowDef#getNodes}
     * @param value the value of the entry to add to the nodes map
     * @param defaultValue is added to the map as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public RootWorkflowDefBuilder putToNodes(String key, FallibleSupplier<BaseNodeDef> value, BaseNodeDef defaultValue) {
        // we're always putting an element (to have something to link the exception to), so make sure the list is present
        if(m_nodesIndividualElements.isEmpty()) m_nodesIndividualElements = Optional.of(new java.util.HashMap<>());
        BaseNodeDef toPut = null;
        try {
            toPut = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toPut = DefaultBaseNodeDef.withException(defaultValue, supplyException);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_nodesIndividualElements.get().put(key, toPut);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for connections
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the connections list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToConnections} will be inserted at the end of the list.
     * @param connections Define the data flow between nodes.
     * @return this for fluent API
     */
    public RootWorkflowDefBuilder setConnections(final java.util.List<ConnectionDef> connections) {
        setConnections(() -> connections);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the connections list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the connections list). 
     * {@code hasExceptions(RootWorkflowDef.Attribute.CONNECTIONS)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.CONNECTIONS)} will return the exception.
     * 
     * @param connections see {@link RootWorkflowDef#getConnections}
     * 
     * @return this builder for fluent API.
     * @see #setConnections(java.util.List<ConnectionDef>)
     */
    public RootWorkflowDefBuilder setConnections(final FallibleSupplier<java.util.List<ConnectionDef>> connections) {
        java.util.Objects.requireNonNull(connections, () -> "No supplier for connections provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.CONNECTIONS);
        try {
            var supplied = connections.get();
            m_connections = Optional.ofNullable(supplied);
            // we set m_connections in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_connectionsBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_connectionsContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the connections list
     * @return this builder for fluent API
     */
    public RootWorkflowDefBuilder addToConnections(ConnectionDef value){
    	addToConnections(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link RootWorkflowDef#getConnections}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the connections list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public RootWorkflowDefBuilder addToConnections(FallibleSupplier<ConnectionDef> value, ConnectionDef defaultValue) {
        // we're always adding an element (to have something to link the exception to), so make sure the list is present
        if(m_connectionsIndividualElements.isEmpty()) m_connectionsIndividualElements = Optional.of(new java.util.ArrayList<>());
        ConnectionDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultConnectionDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_connectionsIndividualElements.get().add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for annotations
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the annotations map. 
     * Calling this method again will undo the previous call (it is not additive).
     * A mapping previously or subsequently set with {@link #putToAnnotations} will replace a mapping added with this method
     * if they have the same key.
     * @param annotations Explanatory text boxes that are shown in the workflow editor.
     * @return this for fluent API
     */
    public RootWorkflowDefBuilder setAnnotations(final java.util.Map<String, AnnotationDataDef> annotations) {
        setAnnotations(() -> annotations);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the annotations map,
     * whereas exceptions thrown in putTo allows to register a {@link LoadException} 
     * for an individual element of the annotations map). 
     * {@code hasExceptions(RootWorkflowDef.Attribute.ANNOTATIONS)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.ANNOTATIONS)} will return the exception.
     * 
     * @param annotations see {@link RootWorkflowDef#getAnnotations}
     * 
     * @return this builder for fluent API.
     * @see #setAnnotations(java.util.Map<String, AnnotationDataDef>)
     */
    public RootWorkflowDefBuilder setAnnotations(final FallibleSupplier<java.util.Map<String, AnnotationDataDef>> annotations) {
        java.util.Objects.requireNonNull(annotations, () -> "No supplier for annotations provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.ANNOTATIONS);
        try {
            var supplied = annotations.get();
            m_annotations = Optional.ofNullable(supplied);
            // we set m_annotations in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_annotationsBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with map element exceptions into a single LoadExceptionTree in #build()
            m_annotationsContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param key the key of the entry to add to the annotations map
     * @param value the value of the entry to add to the annotations map
     * @return this builder for fluent API
     */
    public RootWorkflowDefBuilder putToAnnotations(String key, AnnotationDataDef value){
    	putToAnnotations(key, () -> value, (AnnotationDataDef)null);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the map returned by {@link RootWorkflowDef#getAnnotations}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the given key.
     *
     * @param key the key for the entry added value in the map returned by {@link RootWorkflowDef#getAnnotations}
     * @param value the value of the entry to add to the annotations map
     * @param defaultValue is added to the map as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public RootWorkflowDefBuilder putToAnnotations(String key, FallibleSupplier<AnnotationDataDef> value, AnnotationDataDef defaultValue) {
        // we're always putting an element (to have something to link the exception to), so make sure the list is present
        if(m_annotationsIndividualElements.isEmpty()) m_annotationsIndividualElements = Optional.of(new java.util.HashMap<>());
        AnnotationDataDef toPut = null;
        try {
            toPut = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            toPut = new DefaultAnnotationDataDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_annotationsIndividualElements.get().put(key, toPut);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for workflowEditorSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param workflowEditorSettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setWorkflowEditorSettings(final WorkflowUISettingsDef workflowEditorSettings) {
        setWorkflowEditorSettings(() -> workflowEditorSettings, workflowEditorSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS)} will return the exception.
     * 
     * @param workflowEditorSettings see {@link RootWorkflowDef#getWorkflowEditorSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWorkflowEditorSettings(WorkflowUISettingsDef)
     */
    public RootWorkflowDefBuilder setWorkflowEditorSettings(final FallibleSupplier<WorkflowUISettingsDef> workflowEditorSettings) {
        setWorkflowEditorSettings(workflowEditorSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS)} will return the exception.
     * 
     * @param workflowEditorSettings see {@link RootWorkflowDef#getWorkflowEditorSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWorkflowEditorSettings(WorkflowUISettingsDef)
     */
    public RootWorkflowDefBuilder setWorkflowEditorSettings(final FallibleSupplier<WorkflowUISettingsDef> workflowEditorSettings, WorkflowUISettingsDef defaultValue) {
        java.util.Objects.requireNonNull(workflowEditorSettings, () -> "No supplier for workflowEditorSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS);
        try {
            var supplied = workflowEditorSettings.get();
            m_workflowEditorSettings = Optional.ofNullable(supplied);
            if (m_workflowEditorSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_workflowEditorSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS, (LoadExceptionTree<?>)m_workflowEditorSettings.get());
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
            m_workflowEditorSettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for tableBackendSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param tableBackendSettings  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setTableBackendSettings(final ConfigMapDef tableBackendSettings) {
        setTableBackendSettings(() -> tableBackendSettings, tableBackendSettings);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS)} will return the exception.
     * 
     * @param tableBackendSettings see {@link RootWorkflowDef#getTableBackendSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTableBackendSettings(ConfigMapDef)
     */
    public RootWorkflowDefBuilder setTableBackendSettings(final FallibleSupplier<ConfigMapDef> tableBackendSettings) {
        setTableBackendSettings(tableBackendSettings, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS)} will return the exception.
     * 
     * @param tableBackendSettings see {@link RootWorkflowDef#getTableBackendSettings}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setTableBackendSettings(ConfigMapDef)
     */
    public RootWorkflowDefBuilder setTableBackendSettings(final FallibleSupplier<ConfigMapDef> tableBackendSettings, ConfigMapDef defaultValue) {
        java.util.Objects.requireNonNull(tableBackendSettings, () -> "No supplier for tableBackendSettings provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS);
        try {
            var supplied = tableBackendSettings.get();
            m_tableBackendSettings = Optional.ofNullable(supplied);
            if (m_tableBackendSettings.orElse(null) instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_tableBackendSettings.get()).hasExceptions()) {
                m_exceptionalChildren.put(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS, (LoadExceptionTree<?>)m_tableBackendSettings.get());
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
            m_tableBackendSettings = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS, exceptionTree);
                        if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for flowVariables
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the flowVariables list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToFlowVariables} will be inserted at the end of the list.
     * @param flowVariables Allows to define workflow-global flow variables and set their values.
     * @return this for fluent API
     */
    public RootWorkflowDefBuilder setFlowVariables(final java.util.List<FlowVariableDef> flowVariables) {
        setFlowVariables(() -> flowVariables);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the flowVariables list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the flowVariables list). 
     * {@code hasExceptions(RootWorkflowDef.Attribute.FLOW_VARIABLES)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.FLOW_VARIABLES)} will return the exception.
     * 
     * @param flowVariables see {@link RootWorkflowDef#getFlowVariables}
     * 
     * @return this builder for fluent API.
     * @see #setFlowVariables(java.util.List<FlowVariableDef>)
     */
    public RootWorkflowDefBuilder setFlowVariables(final FallibleSupplier<java.util.List<FlowVariableDef>> flowVariables) {
        java.util.Objects.requireNonNull(flowVariables, () -> "No supplier for flowVariables provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.FLOW_VARIABLES);
        try {
            var supplied = flowVariables.get();
            m_flowVariables = Optional.ofNullable(supplied);
            // we set m_flowVariables in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_flowVariablesBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_flowVariablesContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the flowVariables list
     * @return this builder for fluent API
     */
    public RootWorkflowDefBuilder addToFlowVariables(FlowVariableDef value){
    	addToFlowVariables(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link RootWorkflowDef#getFlowVariables}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the flowVariables list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public RootWorkflowDefBuilder addToFlowVariables(FallibleSupplier<FlowVariableDef> value, FlowVariableDef defaultValue) {
        // we're always adding an element (to have something to link the exception to), so make sure the list is present
        if(m_flowVariablesIndividualElements.isEmpty()) m_flowVariablesIndividualElements = Optional.of(new java.util.ArrayList<>());
        FlowVariableDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultFlowVariableDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_flowVariablesIndividualElements.get().add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for credentialPlaceholders
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the credentialPlaceholders list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToCredentialPlaceholders} will be inserted at the end of the list.
     * @param credentialPlaceholders 
     * @return this for fluent API
     */
    public RootWorkflowDefBuilder setCredentialPlaceholders(final java.util.List<CredentialPlaceholderDef> credentialPlaceholders) {
        setCredentialPlaceholders(() -> credentialPlaceholders);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the credentialPlaceholders list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the credentialPlaceholders list). 
     * {@code hasExceptions(RootWorkflowDef.Attribute.CREDENTIAL_PLACEHOLDERS)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.CREDENTIAL_PLACEHOLDERS)} will return the exception.
     * 
     * @param credentialPlaceholders see {@link RootWorkflowDef#getCredentialPlaceholders}
     * 
     * @return this builder for fluent API.
     * @see #setCredentialPlaceholders(java.util.List<CredentialPlaceholderDef>)
     */
    public RootWorkflowDefBuilder setCredentialPlaceholders(final FallibleSupplier<java.util.List<CredentialPlaceholderDef>> credentialPlaceholders) {
        java.util.Objects.requireNonNull(credentialPlaceholders, () -> "No supplier for credentialPlaceholders provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.CREDENTIAL_PLACEHOLDERS);
        try {
            var supplied = credentialPlaceholders.get();
            m_credentialPlaceholders = Optional.ofNullable(supplied);
            // we set m_credentialPlaceholders in addition to bulk elements because
            // if null is passed the validation is triggered for required fields
            // if non-null is passed, the bulk elements will be merged with the individual elements
            m_credentialPlaceholdersBulkElements = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_credentialPlaceholdersContainerSupplyException = supplyException;
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    /**
     * @param value the value to add to the credentialPlaceholders list
     * @return this builder for fluent API
     */
    public RootWorkflowDefBuilder addToCredentialPlaceholders(CredentialPlaceholderDef value){
    	addToCredentialPlaceholders(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link RootWorkflowDef#getCredentialPlaceholders}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the credentialPlaceholders list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public RootWorkflowDefBuilder addToCredentialPlaceholders(FallibleSupplier<CredentialPlaceholderDef> value, CredentialPlaceholderDef defaultValue) {
        // we're always adding an element (to have something to link the exception to), so make sure the list is present
        if(m_credentialPlaceholdersIndividualElements.isEmpty()) m_credentialPlaceholdersIndividualElements = Optional.of(new java.util.ArrayList<>());
        CredentialPlaceholderDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultCredentialPlaceholderDef(defaultValue, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
        }
        m_credentialPlaceholdersIndividualElements.get().add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link RootWorkflowDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultRootWorkflowDef build() {
        
    	
        // if bulk elements are present, add them to individual elements
        if(m_nodesBulkElements.isPresent()){
            if(m_nodesIndividualElements.isEmpty()) {
                m_nodesIndividualElements = Optional.of(new java.util.HashMap<>());
            }
            m_nodesIndividualElements.get().putAll(m_nodesBulkElements.get());    
        }
        m_nodes = m_nodesIndividualElements;        
        
                
        var nodesLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .map(m_nodes.orElse(new java.util.HashMap<>()), m_nodesContainerSupplyException);
        if(nodesLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.NODES, nodesLoadExceptionTree);
        }
        
        // if bulk elements are present, add them to individual elements
        if(m_connectionsBulkElements.isPresent()){
            if(m_connectionsIndividualElements.isEmpty()) {
                m_connectionsIndividualElements = Optional.of(new java.util.ArrayList<>());
            }
            m_connectionsIndividualElements.get().addAll(m_connectionsBulkElements.get());    
        }
        m_connections = m_connectionsIndividualElements;        
        
                
        var connectionsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_connections.orElse(new java.util.ArrayList<>()), m_connectionsContainerSupplyException);
        if(connectionsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.CONNECTIONS, connectionsLoadExceptionTree);
        }
        
        // if bulk elements are present, add them to individual elements
        if(m_annotationsBulkElements.isPresent()){
            if(m_annotationsIndividualElements.isEmpty()) {
                m_annotationsIndividualElements = Optional.of(new java.util.HashMap<>());
            }
            m_annotationsIndividualElements.get().putAll(m_annotationsBulkElements.get());    
        }
        m_annotations = m_annotationsIndividualElements;        
        
                
        var annotationsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .map(m_annotations.orElse(new java.util.HashMap<>()), m_annotationsContainerSupplyException);
        if(annotationsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.ANNOTATIONS, annotationsLoadExceptionTree);
        }
        
        // if bulk elements are present, add them to individual elements
        if(m_flowVariablesBulkElements.isPresent()){
            if(m_flowVariablesIndividualElements.isEmpty()) {
                m_flowVariablesIndividualElements = Optional.of(new java.util.ArrayList<>());
            }
            m_flowVariablesIndividualElements.get().addAll(m_flowVariablesBulkElements.get());    
        }
        m_flowVariables = m_flowVariablesIndividualElements;        
        
                
        var flowVariablesLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_flowVariables.orElse(new java.util.ArrayList<>()), m_flowVariablesContainerSupplyException);
        if(flowVariablesLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.FLOW_VARIABLES, flowVariablesLoadExceptionTree);
        }
        
        // if bulk elements are present, add them to individual elements
        if(m_credentialPlaceholdersBulkElements.isPresent()){
            if(m_credentialPlaceholdersIndividualElements.isEmpty()) {
                m_credentialPlaceholdersIndividualElements = Optional.of(new java.util.ArrayList<>());
            }
            m_credentialPlaceholdersIndividualElements.get().addAll(m_credentialPlaceholdersBulkElements.get());    
        }
        m_credentialPlaceholders = m_credentialPlaceholdersIndividualElements;        
        
                
        var credentialPlaceholdersLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_credentialPlaceholders.orElse(new java.util.ArrayList<>()), m_credentialPlaceholdersContainerSupplyException);
        if(credentialPlaceholdersLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.CREDENTIAL_PLACEHOLDERS, credentialPlaceholdersLoadExceptionTree);
        }
        
        return new DefaultRootWorkflowDef(this);
    }    

}
