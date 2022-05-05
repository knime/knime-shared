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
import java.util.Optional;
import java.util.Objects; 

import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.knime.core.workflow.def.AnnotationDataDef;
import org.knime.core.workflow.def.AuthorInformationDef;
import org.knime.core.workflow.def.BaseNodeDef;
import org.knime.core.workflow.def.ConfigMapDef;
import org.knime.core.workflow.def.ConnectionDef;
import org.knime.core.workflow.def.CredentialPlaceholderDef;
import org.knime.core.workflow.def.FlowVariableDef;
import org.knime.core.workflow.def.WorkflowDef;
import org.knime.core.workflow.def.WorkflowUISettingsDef;
import org.knime.core.workflow.def.impl.DefaultWorkflowDef;

import org.knime.core.workflow.def.RootWorkflowDef;



// for types that define enums
import org.knime.core.workflow.def.RootWorkflowDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultRootWorkflowDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultRootWorkflowDef extends DefaultWorkflowDef implements RootWorkflowDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<RootWorkflowDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    @JsonProperty("tableBackendSettings")
    protected ConfigMapDef m_tableBackendSettings;

    /** 
     * Allows to define workflow-global flow variables and set their values. 
     */
    @JsonProperty("flowVariables")
    protected java.util.List<FlowVariableDef> m_flowVariables;

    @JsonProperty("credentialPlaceholders")
    protected java.util.List<CredentialPlaceholderDef> m_credentialPlaceholders;

    @JsonProperty("workflow")
    protected WorkflowDef m_workflow;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultRootWorkflowDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link RootWorkflowDefBuilder}.
     * @param builder source
     */
    DefaultRootWorkflowDef(RootWorkflowDefBuilder builder) {
        // TODO make immutable copies!!
        super();
            
        m_name = builder.m_name;
        m_authorInformation = builder.m_authorInformation;
        m_nodes = builder.m_nodes;
        m_connections = builder.m_connections;
        m_annotations = builder.m_annotations;
        m_workflowEditorSettings = builder.m_workflowEditorSettings;
        m_tableBackendSettings = builder.m_tableBackendSettings;
        m_flowVariables = builder.m_flowVariables;
        m_credentialPlaceholders = builder.m_credentialPlaceholders;
        m_workflow = builder.m_workflow;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link RootWorkflowDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultRootWorkflowDef(RootWorkflowDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new RootWorkflowDefBuilder().build());
        
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
        if(toCopy instanceof DefaultRootWorkflowDef){
            var childTree = ((DefaultRootWorkflowDef)toCopy).getLoadExceptionTree();                
            // if present, merge child tree with supply exception
            var merged = childTree.isEmpty() ? supplyException : SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            m_exceptionTree = Optional.of(merged);
        } else {
            m_exceptionTree = Optional.of(supplyException);
        }
    }



    /**
     * Copy constructor.
     * @param toCopy source
     */
    public DefaultRootWorkflowDef(RootWorkflowDef toCopy) {
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
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing RootWorkflow
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultRootWorkflowDef withException(RootWorkflowDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(RootWorkflowDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to RootWorkflowDef.Attribute
            return ((LoadExceptionTree<RootWorkflowDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getName() {
        return m_name;
    }
    @Override
    public AuthorInformationDef getAuthorInformation() {
        return m_authorInformation;
    }
    @Override
    public java.util.Map<String, BaseNodeDef> getNodes() {
        return m_nodes;
    }
    @Override
    public java.util.List<ConnectionDef> getConnections() {
        return m_connections;
    }
    @Override
    public java.util.Map<String, AnnotationDataDef> getAnnotations() {
        return m_annotations;
    }
    @Override
    public WorkflowUISettingsDef getWorkflowEditorSettings() {
        return m_workflowEditorSettings;
    }
    @Override
    public ConfigMapDef getTableBackendSettings() {
        return m_tableBackendSettings;
    }
    @Override
    public java.util.List<FlowVariableDef> getFlowVariables() {
        return m_flowVariables;
    }
    @Override
    public java.util.List<CredentialPlaceholderDef> getCredentialPlaceholders() {
        return m_credentialPlaceholders;
    }
    @Override
    public WorkflowDef getWorkflow() {
        return m_workflow;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to name.
     */
    @JsonIgnore
    public Optional<LoadException> getNameSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.NAME).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to authorInformation.
     */
    @JsonIgnore
    public Optional<LoadException> getAuthorInformationSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.AUTHOR_INFORMATION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link AuthorInformationDef} returned by {@link #getAuthorInformation()}, this
     * returns the authorInformation as DefaultAuthorInformationDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultAuthorInformationDef> getFaultyAuthorInformation(){
    	final var authorInformation = getAuthorInformation(); 
        if(authorInformation instanceof DefaultAuthorInformationDef && ((DefaultAuthorInformationDef)authorInformation).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultAuthorInformationDef)authorInformation);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to nodes.
     */
    @JsonIgnore
    public Optional<LoadException> getNodesSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.NODES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<String>> getNodesExceptionTree(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.NODES).map(let -> (LoadExceptionTree<String>)let);
    }

     
 
    /**
     * @return The supply exception associated to connections.
     */
    @JsonIgnore
    public Optional<LoadException> getConnectionsSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.CONNECTIONS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getConnectionsExceptionTree(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.CONNECTIONS).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to annotations.
     */
    @JsonIgnore
    public Optional<LoadException> getAnnotationsSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.ANNOTATIONS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<String>> getAnnotationsExceptionTree(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.ANNOTATIONS).map(let -> (LoadExceptionTree<String>)let);
    }

     
 
    /**
     * @return The supply exception associated to workflowEditorSettings.
     */
    @JsonIgnore
    public Optional<LoadException> getWorkflowEditorSettingsSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.WORKFLOW_EDITOR_SETTINGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link WorkflowUISettingsDef} returned by {@link #getWorkflowEditorSettings()}, this
     * returns the workflowEditorSettings as DefaultWorkflowUISettingsDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultWorkflowUISettingsDef> getFaultyWorkflowEditorSettings(){
    	final var workflowEditorSettings = getWorkflowEditorSettings(); 
        if(workflowEditorSettings instanceof DefaultWorkflowUISettingsDef && ((DefaultWorkflowUISettingsDef)workflowEditorSettings).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultWorkflowUISettingsDef)workflowEditorSettings);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to tableBackendSettings.
     */
    @JsonIgnore
    public Optional<LoadException> getTableBackendSettingsSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.TABLE_BACKEND_SETTINGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link ConfigMapDef} returned by {@link #getTableBackendSettings()}, this
     * returns the tableBackendSettings as DefaultConfigMapDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultConfigMapDef> getFaultyTableBackendSettings(){
    	final var tableBackendSettings = getTableBackendSettings(); 
        if(tableBackendSettings instanceof DefaultConfigMapDef && ((DefaultConfigMapDef)tableBackendSettings).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultConfigMapDef)tableBackendSettings);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to flowVariables.
     */
    @JsonIgnore
    public Optional<LoadException> getFlowVariablesSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.FLOW_VARIABLES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getFlowVariablesExceptionTree(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.FLOW_VARIABLES).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to credentialPlaceholders.
     */
    @JsonIgnore
    public Optional<LoadException> getCredentialPlaceholdersSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.CREDENTIAL_PLACEHOLDERS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getCredentialPlaceholdersExceptionTree(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.CREDENTIAL_PLACEHOLDERS).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to workflow.
     */
    @JsonIgnore
    public Optional<LoadException> getWorkflowSupplyException(){
    	return getLoadExceptionTree(RootWorkflowDef.Attribute.WORKFLOW).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link WorkflowDef} returned by {@link #getWorkflow()}, this
     * returns the workflow as DefaultWorkflowDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultWorkflowDef> getFaultyWorkflow(){
    	final var workflow = getWorkflow(); 
        if(workflow instanceof DefaultWorkflowDef && ((DefaultWorkflowDef)workflow).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultWorkflowDef)workflow);
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
        DefaultRootWorkflowDef other = (DefaultRootWorkflowDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_tableBackendSettings, other.m_tableBackendSettings);
        equalsBuilder.append(m_flowVariables, other.m_flowVariables);
        equalsBuilder.append(m_credentialPlaceholders, other.m_credentialPlaceholders);
        equalsBuilder.append(m_workflow, other.m_workflow);
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
                .append(m_tableBackendSettings)
                .append(m_flowVariables)
                .append(m_credentialPlaceholders)
                .append(m_workflow)
                .toHashCode();
    }

} 
