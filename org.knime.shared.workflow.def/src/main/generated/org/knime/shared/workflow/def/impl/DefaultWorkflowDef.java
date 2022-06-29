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
import java.util.Objects; 

import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.knime.shared.workflow.def.AnnotationDataDef;
import org.knime.shared.workflow.def.AuthorInformationDef;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.ConnectionDef;
import org.knime.shared.workflow.def.WorkflowUISettingsDef;

import org.knime.shared.workflow.def.WorkflowDef;



// for types that define enums
import org.knime.shared.workflow.def.WorkflowDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.shared.workflow.def.LoadException;
import org.knime.shared.workflow.def.LoadExceptionTree;
import org.knime.shared.workflow.def.LoadExceptionTreeProvider;
import org.knime.shared.workflow.def.SimpleLoadExceptionTree;


/**
 * Defines a data processing pipeline.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultWorkflowDef implements WorkflowDef, LoadExceptionTreeProvider {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<WorkflowDef.Attribute> instance. */
    private final LoadExceptionTree<?> m_exceptionTree;

    /** 
     * A user-chosen identifier for the workflow. 
     */
    @JsonProperty("name")
    protected Optional<String> m_name;

    @JsonProperty("authorInformation")
    protected Optional<AuthorInformationDef> m_authorInformation;

    /** 
     * The executable blocks in this workflow. 
     */
    @JsonProperty("nodes")
    protected Optional<java.util.Map<String, BaseNodeDef>> m_nodes;

    /** 
     * Define the data flow between nodes. 
     */
    @JsonProperty("connections")
    protected Optional<java.util.List<ConnectionDef>> m_connections;

    /** 
     * Explanatory text boxes that are shown in the workflow editor. 
     */
    @JsonProperty("annotations")
    protected Optional<java.util.Map<String, AnnotationDataDef>> m_annotations;

    @JsonProperty("workflowEditorSettings")
    protected Optional<WorkflowUISettingsDef> m_workflowEditorSettings;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultWorkflowDef() {
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    /**
     * Builder constructor. Only for use in {@link WorkflowDefBuilder}.
     * @param builder source
     */
    DefaultWorkflowDef(WorkflowDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_name = builder.m_name;
        m_authorInformation = builder.m_authorInformation;
        m_nodes = builder.m_nodes;
        m_connections = builder.m_connections;
        m_annotations = builder.m_annotations;
        m_workflowEditorSettings = builder.m_workflowEditorSettings;

        m_exceptionTree = SimpleLoadExceptionTree.map(builder.m_exceptionalChildren);
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link WorkflowDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultWorkflowDef(WorkflowDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new WorkflowDefBuilder().build());
        
        m_name = toCopy.getName();
        m_authorInformation = toCopy.getAuthorInformation();
        m_nodes = toCopy.getNodes();
        m_connections = toCopy.getConnections();
        m_annotations = toCopy.getAnnotations();
        m_workflowEditorSettings = toCopy.getWorkflowEditorSettings();
        if(toCopy instanceof LoadExceptionTreeProvider){
            var childTree = ((LoadExceptionTreeProvider)toCopy).getLoadExceptionTree();                
            // if present, merge child tree with supply exception
            var merged = childTree.hasExceptions() ? SimpleLoadExceptionTree.tree(childTree, supplyException) : supplyException;
            m_exceptionTree = merged;
        } else {
            m_exceptionTree = supplyException;
        }
    }



    /**
     * Copy constructor.
     * @param toCopy source
     */
    public DefaultWorkflowDef(WorkflowDef toCopy) {
        m_name = toCopy.getName();
        m_authorInformation = toCopy.getAuthorInformation();
        m_nodes = toCopy.getNodes();
        m_connections = toCopy.getConnections();
        m_annotations = toCopy.getAnnotations();
        m_workflowEditorSettings = toCopy.getWorkflowEditorSettings();
        
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing Workflow
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultWorkflowDef withException(WorkflowDef toCopy, final LoadException exception) {
        Objects.requireNonNull(exception);
        throw new IllegalArgumentException();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree implementation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @return the load exceptions for this instance and its descendants
     */
    @JsonIgnore
    public LoadExceptionTree<?> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants.
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(WorkflowDef.Attribute attribute){
        if (m_exceptionTree instanceof LoadException) {
            return Optional.empty();
        }
        // if the tree is not a leaf, it is typed to WorkflowDef.Attribute
        return ((LoadExceptionTree<WorkflowDef.Attribute>)m_exceptionTree).getExceptionTree(attribute);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<String> getName() {
        return m_name;
    }
    @Override
    public Optional<AuthorInformationDef> getAuthorInformation() {
        return m_authorInformation;
    }
    @Override
    public Optional<java.util.Map<String, BaseNodeDef>> getNodes() {
        return m_nodes;
    }
    @Override
    public Optional<java.util.List<ConnectionDef>> getConnections() {
        return m_connections;
    }
    @Override
    public Optional<java.util.Map<String, AnnotationDataDef>> getAnnotations() {
        return m_annotations;
    }
    @Override
    public Optional<WorkflowUISettingsDef> getWorkflowEditorSettings() {
        return m_workflowEditorSettings;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to name.
     */
    @JsonIgnore
    public Optional<LoadException> getNameSupplyException(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.NAME).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to authorInformation.
     */
    @JsonIgnore
    public Optional<LoadException> getAuthorInformationSupplyException(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.AUTHOR_INFORMATION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link AuthorInformationDef} returned by {@link #getAuthorInformation()}, this
     * returns the authorInformation as DefaultAuthorInformationDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultAuthorInformationDef> getFaultyAuthorInformation(){
    	final var authorInformation = getAuthorInformation(); 
        if(LoadExceptionTreeProvider.hasExceptions(authorInformation)) {
            return Optional.of((DefaultAuthorInformationDef)authorInformation.get());
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to nodes.
     */
    @JsonIgnore
    public Optional<LoadException> getNodesSupplyException(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.NODES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<String>> getNodesExceptionTree(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.NODES).map(let -> (LoadExceptionTree<String>)let);
    }

     
 
    /**
     * @return The supply exception associated to connections.
     */
    @JsonIgnore
    public Optional<LoadException> getConnectionsSupplyException(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.CONNECTIONS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getConnectionsExceptionTree(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.CONNECTIONS).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to annotations.
     */
    @JsonIgnore
    public Optional<LoadException> getAnnotationsSupplyException(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.ANNOTATIONS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<String>> getAnnotationsExceptionTree(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.ANNOTATIONS).map(let -> (LoadExceptionTree<String>)let);
    }

     
 
    /**
     * @return The supply exception associated to workflowEditorSettings.
     */
    @JsonIgnore
    public Optional<LoadException> getWorkflowEditorSettingsSupplyException(){
    	return getLoadExceptionTree(WorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link WorkflowUISettingsDef} returned by {@link #getWorkflowEditorSettings()}, this
     * returns the workflowEditorSettings as DefaultWorkflowUISettingsDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultWorkflowUISettingsDef> getFaultyWorkflowEditorSettings(){
    	final var workflowEditorSettings = getWorkflowEditorSettings(); 
        if(LoadExceptionTreeProvider.hasExceptions(workflowEditorSettings)) {
            return Optional.of((DefaultWorkflowUISettingsDef)workflowEditorSettings.get());
        }
    	return Optional.empty();
    }
         
 
    

    // -----------------------------------------------------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o.getClass().equals(this.getClass()))) {
            return false;
        }
        DefaultWorkflowDef other = (DefaultWorkflowDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_name, other.m_name);
        equalsBuilder.append(m_authorInformation, other.m_authorInformation);
        equalsBuilder.append(m_nodes, other.m_nodes);
        equalsBuilder.append(m_connections, other.m_connections);
        equalsBuilder.append(m_annotations, other.m_annotations);
        equalsBuilder.append(m_workflowEditorSettings, other.m_workflowEditorSettings);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_name)
                .append(m_authorInformation)
                .append(m_nodes)
                .append(m_connections)
                .append(m_annotations)
                .append(m_workflowEditorSettings)
                .toHashCode();
    }

} 
