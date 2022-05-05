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

import org.knime.shared.workflow.def.CipherDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.NodeUIInfoDef;
import org.knime.shared.workflow.def.PortDef;
import org.knime.shared.workflow.def.TemplateInfoDef;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.def.impl.DefaultBaseNodeDef;

import org.knime.shared.workflow.def.MetaNodeDef;



// for types that define enums
import org.knime.shared.workflow.def.MetaNodeDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultMetaNodeDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultMetaNodeDef extends DefaultBaseNodeDef implements MetaNodeDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<MetaNodeDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    @JsonProperty("workflow")
    protected WorkflowDef m_workflow;

    /** 
     * Defines the endpoints for incoming data connections. 
     */
    @JsonProperty("inPorts")
    protected java.util.List<PortDef> m_inPorts;

    /** 
     * Defines the endpoints for outgoing data connections. 
     */
    @JsonProperty("outPorts")
    protected java.util.List<PortDef> m_outPorts;

    @JsonProperty("cipher")
    protected CipherDef m_cipher;

    @JsonProperty("link")
    protected TemplateInfoDef m_link;

    @JsonProperty("inPortsBarUIInfo")
    protected NodeUIInfoDef m_inPortsBarUIInfo;

    @JsonProperty("outPortsBarUIInfo")
    protected NodeUIInfoDef m_outPortsBarUIInfo;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultMetaNodeDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link MetaNodeDefBuilder}.
     * @param builder source
     */
    DefaultMetaNodeDef(MetaNodeDefBuilder builder) {
        // TODO make immutable copies!!
        super();
            
        m_id = builder.m_id;
        m_nodeType = builder.m_nodeType;
        m_customDescription = builder.m_customDescription;
        m_annotation = builder.m_annotation;
        m_uiInfo = builder.m_uiInfo;
        m_locks = builder.m_locks;
        m_jobManager = builder.m_jobManager;
        m_workflow = builder.m_workflow;
        m_inPorts = builder.m_inPorts;
        m_outPorts = builder.m_outPorts;
        m_cipher = builder.m_cipher;
        m_link = builder.m_link;
        m_inPortsBarUIInfo = builder.m_inPortsBarUIInfo;
        m_outPortsBarUIInfo = builder.m_outPortsBarUIInfo;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link MetaNodeDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultMetaNodeDef(MetaNodeDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new MetaNodeDefBuilder().build());
        
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
        if(toCopy instanceof DefaultMetaNodeDef){
            var childTree = ((DefaultMetaNodeDef)toCopy).getLoadExceptionTree();                
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
    public DefaultMetaNodeDef(MetaNodeDef toCopy) {
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
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing MetaNode
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultMetaNodeDef withException(MetaNodeDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(MetaNodeDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to MetaNodeDef.Attribute
            return ((LoadExceptionTree<MetaNodeDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Integer getId() {
        return m_id;
    }
    @Override
    public NodeTypeEnum getNodeType() {
        return m_nodeType;
    }
    @Override
    public String getCustomDescription() {
        return m_customDescription;
    }
    @Override
    public NodeAnnotationDef getAnnotation() {
        return m_annotation;
    }
    @Override
    public NodeUIInfoDef getUiInfo() {
        return m_uiInfo;
    }
    @Override
    public NodeLocksDef getLocks() {
        return m_locks;
    }
    @Override
    public JobManagerDef getJobManager() {
        return m_jobManager;
    }
    @Override
    public WorkflowDef getWorkflow() {
        return m_workflow;
    }
    @Override
    public java.util.List<PortDef> getInPorts() {
        return m_inPorts;
    }
    @Override
    public java.util.List<PortDef> getOutPorts() {
        return m_outPorts;
    }
    @Override
    public CipherDef getCipher() {
        return m_cipher;
    }
    @Override
    public TemplateInfoDef getLink() {
        return m_link;
    }
    @Override
    public NodeUIInfoDef getInPortsBarUIInfo() {
        return m_inPortsBarUIInfo;
    }
    @Override
    public NodeUIInfoDef getOutPortsBarUIInfo() {
        return m_outPortsBarUIInfo;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to id.
     */
    @JsonIgnore
    public Optional<LoadException> getIdSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.ID).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to nodeType.
     */
    @JsonIgnore
    public Optional<LoadException> getNodeTypeSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.NODE_TYPE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to customDescription.
     */
    @JsonIgnore
    public Optional<LoadException> getCustomDescriptionSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.CUSTOM_DESCRIPTION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to annotation.
     */
    @JsonIgnore
    public Optional<LoadException> getAnnotationSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.ANNOTATION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link NodeAnnotationDef} returned by {@link #getAnnotation()}, this
     * returns the annotation as DefaultNodeAnnotationDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultNodeAnnotationDef> getFaultyAnnotation(){
    	final var annotation = getAnnotation(); 
        if(annotation instanceof DefaultNodeAnnotationDef && ((DefaultNodeAnnotationDef)annotation).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultNodeAnnotationDef)annotation);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to uiInfo.
     */
    @JsonIgnore
    public Optional<LoadException> getUiInfoSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.UI_INFO).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link NodeUIInfoDef} returned by {@link #getUiInfo()}, this
     * returns the uiInfo as DefaultNodeUIInfoDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultNodeUIInfoDef> getFaultyUiInfo(){
    	final var uiInfo = getUiInfo(); 
        if(uiInfo instanceof DefaultNodeUIInfoDef && ((DefaultNodeUIInfoDef)uiInfo).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultNodeUIInfoDef)uiInfo);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to locks.
     */
    @JsonIgnore
    public Optional<LoadException> getLocksSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.LOCKS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link NodeLocksDef} returned by {@link #getLocks()}, this
     * returns the locks as DefaultNodeLocksDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultNodeLocksDef> getFaultyLocks(){
    	final var locks = getLocks(); 
        if(locks instanceof DefaultNodeLocksDef && ((DefaultNodeLocksDef)locks).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultNodeLocksDef)locks);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to jobManager.
     */
    @JsonIgnore
    public Optional<LoadException> getJobManagerSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.JOB_MANAGER).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link JobManagerDef} returned by {@link #getJobManager()}, this
     * returns the jobManager as DefaultJobManagerDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultJobManagerDef> getFaultyJobManager(){
    	final var jobManager = getJobManager(); 
        if(jobManager instanceof DefaultJobManagerDef && ((DefaultJobManagerDef)jobManager).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultJobManagerDef)jobManager);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to workflow.
     */
    @JsonIgnore
    public Optional<LoadException> getWorkflowSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.WORKFLOW).flatMap(LoadExceptionTree::getSupplyException);
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
         
 
    /**
     * @return The supply exception associated to inPorts.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortsSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.IN_PORTS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getInPortsExceptionTree(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.IN_PORTS).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to outPorts.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortsSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.OUT_PORTS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getOutPortsExceptionTree(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.OUT_PORTS).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to cipher.
     */
    @JsonIgnore
    public Optional<LoadException> getCipherSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.CIPHER).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link CipherDef} returned by {@link #getCipher()}, this
     * returns the cipher as DefaultCipherDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultCipherDef> getFaultyCipher(){
    	final var cipher = getCipher(); 
        if(cipher instanceof DefaultCipherDef && ((DefaultCipherDef)cipher).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultCipherDef)cipher);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to link.
     */
    @JsonIgnore
    public Optional<LoadException> getLinkSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.LINK).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link TemplateInfoDef} returned by {@link #getLink()}, this
     * returns the link as DefaultTemplateInfoDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultTemplateInfoDef> getFaultyLink(){
    	final var link = getLink(); 
        if(link instanceof DefaultTemplateInfoDef && ((DefaultTemplateInfoDef)link).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultTemplateInfoDef)link);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to inPortsBarUIInfo.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortsBarUIInfoSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.IN_PORTS_BAR_UI_INFO).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link NodeUIInfoDef} returned by {@link #getInPortsBarUIInfo()}, this
     * returns the inPortsBarUIInfo as DefaultNodeUIInfoDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultNodeUIInfoDef> getFaultyInPortsBarUIInfo(){
    	final var inPortsBarUIInfo = getInPortsBarUIInfo(); 
        if(inPortsBarUIInfo instanceof DefaultNodeUIInfoDef && ((DefaultNodeUIInfoDef)inPortsBarUIInfo).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultNodeUIInfoDef)inPortsBarUIInfo);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to outPortsBarUIInfo.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortsBarUIInfoSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.OUT_PORTS_BAR_UI_INFO).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link NodeUIInfoDef} returned by {@link #getOutPortsBarUIInfo()}, this
     * returns the outPortsBarUIInfo as DefaultNodeUIInfoDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultNodeUIInfoDef> getFaultyOutPortsBarUIInfo(){
    	final var outPortsBarUIInfo = getOutPortsBarUIInfo(); 
        if(outPortsBarUIInfo instanceof DefaultNodeUIInfoDef && ((DefaultNodeUIInfoDef)outPortsBarUIInfo).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultNodeUIInfoDef)outPortsBarUIInfo);
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
        DefaultMetaNodeDef other = (DefaultMetaNodeDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_workflow, other.m_workflow);
        equalsBuilder.append(m_inPorts, other.m_inPorts);
        equalsBuilder.append(m_outPorts, other.m_outPorts);
        equalsBuilder.append(m_cipher, other.m_cipher);
        equalsBuilder.append(m_link, other.m_link);
        equalsBuilder.append(m_inPortsBarUIInfo, other.m_inPortsBarUIInfo);
        equalsBuilder.append(m_outPortsBarUIInfo, other.m_outPortsBarUIInfo);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_id)
                .append(m_nodeType)
                .append(m_customDescription)
                .append(m_annotation)
                .append(m_uiInfo)
                .append(m_locks)
                .append(m_jobManager)
                .append(m_workflow)
                .append(m_inPorts)
                .append(m_outPorts)
                .append(m_cipher)
                .append(m_link)
                .append(m_inPortsBarUIInfo)
                .append(m_outPortsBarUIInfo)
                .toHashCode();
    }

} 
