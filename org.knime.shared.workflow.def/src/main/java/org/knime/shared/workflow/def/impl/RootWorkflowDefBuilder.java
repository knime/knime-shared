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

import org.knime.shared.workflow.def.AnnotationDataDef;
import org.knime.shared.workflow.def.AuthorInformationDef;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.ConnectionDef;
import org.knime.shared.workflow.def.CredentialPlaceholderDef;
import org.knime.shared.workflow.def.FlowVariableDef;
import org.knime.shared.workflow.def.WorkflowDef;
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
/**
 * RootWorkflowDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class RootWorkflowDefBuilder {

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
    String m_name;
    

    AuthorInformationDef m_authorInformation;
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this map.
     */
    java.util.Map<String, BaseNodeDef> m_nodes = new java.util.LinkedHashMap<>();
    /** Temporarily holds onto elements set as a whole with setNodes these are added to m_nodes in build */
    private java.util.Map<String, BaseNodeDef> m_nodesBulkElements = new java.util.HashMap<>();
    /** This exception is merged with the exceptions of the elements of this map into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_nodesContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<ConnectionDef> m_connections = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setConnections these are added to m_connections in build */
    private java.util.List<ConnectionDef> m_connectionsBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_connectionsContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this map.
     */
    java.util.Map<String, AnnotationDataDef> m_annotations = new java.util.LinkedHashMap<>();
    /** Temporarily holds onto elements set as a whole with setAnnotations these are added to m_annotations in build */
    private java.util.Map<String, AnnotationDataDef> m_annotationsBulkElements = new java.util.HashMap<>();
    /** This exception is merged with the exceptions of the elements of this map into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_annotationsContainerSupplyException; 
    
    WorkflowUISettingsDef m_workflowEditorSettings;
    
    ConfigMapDef m_tableBackendSettings;
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<FlowVariableDef> m_flowVariables = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setFlowVariables these are added to m_flowVariables in build */
    private java.util.List<FlowVariableDef> m_flowVariablesBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_flowVariablesContainerSupplyException; 
    
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<CredentialPlaceholderDef> m_credentialPlaceholders = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setCredentialPlaceholders these are added to m_credentialPlaceholders in build */
    private java.util.List<CredentialPlaceholderDef> m_credentialPlaceholdersBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_credentialPlaceholdersContainerSupplyException; 
    
    WorkflowDef m_workflow;
    
    /**
     * Create a new builder.
     */
    public RootWorkflowDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public RootWorkflowDefBuilder(final RootWorkflowDef toCopy) {
        m_name = toCopy.getName();
        m_authorInformation = toCopy.getAuthorInformation();
        m_nodes = toCopy.getNodes();
        m_connections = toCopy.getConnections();
        m_annotations = toCopy.getAnnotations();
        m_workflowEditorSettings = toCopy.getWorkflowEditorSettings();
        m_tableBackendSettings = toCopy.getTableBackendSettings();
        m_flowVariables = toCopy.getFlowVariables();
        m_credentialPlaceholders = toCopy.getCredentialPlaceholders();
        m_workflow = toCopy.getWorkflow();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for name
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param name A user-chosen identifier for the workflow.
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setName(final String name) {
        setName(() -> name, name);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_name = name.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_name = defaultValue;
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.NAME, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for authorInformation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param authorInformation 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setAuthorInformation(final AuthorInformationDef authorInformation) {
        setAuthorInformation(() -> authorInformation, authorInformation);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_authorInformation = authorInformation.get();
            if (m_authorInformation instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_authorInformation).hasExceptions()) {
                m_exceptionalChildren.put(RootWorkflowDef.Attribute.AUTHOR_INFORMATION, (LoadExceptionTree<?>)m_authorInformation);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultAuthorInformationDef){
                var childTree = ((DefaultAuthorInformationDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_authorInformation = defaultValue;
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.AUTHOR_INFORMATION, exceptionTree);
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
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_nodesBulkElements = nodes.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_nodesBulkElements = java.util.Map.of();
            // merged together with map element exceptions into a single LoadExceptionTree in #build()
            m_nodesContainerSupplyException = supplyException;
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
        BaseNodeDef toPut = null;
        try {
            toPut = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            // copies values to a new def (of the appropriate subtype, if any) and adds the load exception
            toPut = DefaultBaseNodeDef.withException(defaultValue, supplyException);
        }
        m_nodes.put(key, toPut);
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
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_connectionsBulkElements = connections.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_connectionsBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_connectionsContainerSupplyException = supplyException;
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
        ConnectionDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultConnectionDef(defaultValue, supplyException);
        }
        m_connections.add(toAdd);
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
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_annotationsBulkElements = annotations.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_annotationsBulkElements = java.util.Map.of();
            // merged together with map element exceptions into a single LoadExceptionTree in #build()
            m_annotationsContainerSupplyException = supplyException;
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
        AnnotationDataDef toPut = null;
        try {
            toPut = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            // copies values to a new def (of the appropriate subtype, if any) and adds the load exception
            toPut = DefaultAnnotationDataDef.withException(defaultValue, supplyException);
        }
        m_annotations.put(key, toPut);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for workflowEditorSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param workflowEditorSettings 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setWorkflowEditorSettings(final WorkflowUISettingsDef workflowEditorSettings) {
        setWorkflowEditorSettings(() -> workflowEditorSettings, workflowEditorSettings);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_workflowEditorSettings = workflowEditorSettings.get();
            if (m_workflowEditorSettings instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_workflowEditorSettings).hasExceptions()) {
                m_exceptionalChildren.put(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS, (LoadExceptionTree<?>)m_workflowEditorSettings);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultWorkflowUISettingsDef){
                var childTree = ((DefaultWorkflowUISettingsDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_workflowEditorSettings = defaultValue;
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for tableBackendSettings
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param tableBackendSettings 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setTableBackendSettings(final ConfigMapDef tableBackendSettings) {
        setTableBackendSettings(() -> tableBackendSettings, tableBackendSettings);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_tableBackendSettings = tableBackendSettings.get();
            if (m_tableBackendSettings instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_tableBackendSettings).hasExceptions()) {
                m_exceptionalChildren.put(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS, (LoadExceptionTree<?>)m_tableBackendSettings);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultConfigMapDef){
                var childTree = ((DefaultConfigMapDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_tableBackendSettings = defaultValue;
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS, exceptionTree);
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
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_flowVariablesBulkElements = flowVariables.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_flowVariablesBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_flowVariablesContainerSupplyException = supplyException;
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
        FlowVariableDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultFlowVariableDef(defaultValue, supplyException);
        }
        m_flowVariables.add(toAdd);
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
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
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
            m_credentialPlaceholdersBulkElements = credentialPlaceholders.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_credentialPlaceholdersBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_credentialPlaceholdersContainerSupplyException = supplyException;
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
        CredentialPlaceholderDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultCredentialPlaceholderDef(defaultValue, supplyException);
        }
        m_credentialPlaceholders.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for workflow
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param workflow 
     * @return this builder for fluent API.
     */ 
    public RootWorkflowDefBuilder setWorkflow(final WorkflowDef workflow) {
        setWorkflow(() -> workflow, workflow);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(RootWorkflowDef.Attribute.WORKFLOW)} will return true and and
     * {@code getExceptionalChildren().get(RootWorkflowDef.Attribute.WORKFLOW)} will return the exception.
     * 
     * @param workflow see {@link RootWorkflowDef#getWorkflow}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setWorkflow(WorkflowDef)
     */
    public RootWorkflowDefBuilder setWorkflow(final FallibleSupplier<WorkflowDef> workflow, WorkflowDef defaultValue) {
        java.util.Objects.requireNonNull(workflow, () -> "No supplier for workflow provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(RootWorkflowDef.Attribute.WORKFLOW);
        try {
            m_workflow = workflow.get();
            if (m_workflow instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_workflow).hasExceptions()) {
                m_exceptionalChildren.put(RootWorkflowDef.Attribute.WORKFLOW, (LoadExceptionTree<?>)m_workflow);
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
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.WORKFLOW, exceptionTree);
            	    }   
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
        
    	
        // contains the elements set with #setFlowVariables (those added with #addToFlowVariables have already been inserted into m_flowVariables)
        m_flowVariablesBulkElements = java.util.Objects.requireNonNullElse(m_flowVariablesBulkElements, java.util.List.of());
        m_flowVariables.addAll(0, m_flowVariablesBulkElements);
                
        var flowVariablesLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_flowVariables, m_flowVariablesContainerSupplyException);
        if(flowVariablesLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.FLOW_VARIABLES, flowVariablesLoadExceptionTree);
        }
        
        // contains the elements set with #setCredentialPlaceholders (those added with #addToCredentialPlaceholders have already been inserted into m_credentialPlaceholders)
        m_credentialPlaceholdersBulkElements = java.util.Objects.requireNonNullElse(m_credentialPlaceholdersBulkElements, java.util.List.of());
        m_credentialPlaceholders.addAll(0, m_credentialPlaceholdersBulkElements);
                
        var credentialPlaceholdersLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_credentialPlaceholders, m_credentialPlaceholdersContainerSupplyException);
        if(credentialPlaceholdersLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(RootWorkflowDef.Attribute.CREDENTIAL_PLACEHOLDERS, credentialPlaceholdersLoadExceptionTree);
        }
        
        return new DefaultRootWorkflowDef(this);
    }    

}
