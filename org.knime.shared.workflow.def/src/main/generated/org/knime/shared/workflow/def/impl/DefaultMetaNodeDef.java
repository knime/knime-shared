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

import org.knime.shared.workflow.def.BoundsDef;
import org.knime.shared.workflow.def.CipherDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.PortDef;
import org.knime.shared.workflow.def.TemplateLinkDef;
import org.knime.shared.workflow.def.TemplateMetadataDef;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.def.impl.DefaultBaseNodeDef;

import org.knime.shared.workflow.def.MetaNodeDef;



// for types that define enums
import org.knime.shared.workflow.def.MetaNodeDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.shared.workflow.def.LoadException;
import org.knime.shared.workflow.def.LoadExceptionTree;
import org.knime.shared.workflow.def.LoadExceptionTreeProvider;
import org.knime.shared.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultMetaNodeDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultMetaNodeDef extends DefaultBaseNodeDef implements MetaNodeDef, LoadExceptionTreeProvider {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<MetaNodeDef.Attribute> instance. */
    private final LoadExceptionTree<?> m_exceptionTree;

    @JsonProperty("workflow")
    protected WorkflowDef m_workflow;

    /** 
     * Defines the endpoints for incoming data connections. 
     */
    @JsonProperty("inPorts")
    protected Optional<java.util.List<PortDef>> m_inPorts;

    /** 
     * Defines the endpoints for outgoing data connections. 
     */
    @JsonProperty("outPorts")
    protected Optional<java.util.List<PortDef>> m_outPorts;

    @JsonProperty("cipher")
    protected Optional<CipherDef> m_cipher;

    @JsonProperty("templateMetadata")
    protected Optional<TemplateMetadataDef> m_templateMetadata;

    @JsonProperty("templateLink")
    protected Optional<TemplateLinkDef> m_templateLink;

    @JsonProperty("inPortsBarBounds")
    protected Optional<BoundsDef> m_inPortsBarBounds;

    @JsonProperty("outPortsBarBounds")
    protected Optional<BoundsDef> m_outPortsBarBounds;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultMetaNodeDef() {
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
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
        m_bounds = builder.m_bounds;
        m_locks = builder.m_locks;
        m_jobManager = builder.m_jobManager;
        m_workflow = builder.m_workflow;
        m_inPorts = builder.m_inPorts;
        m_outPorts = builder.m_outPorts;
        m_cipher = builder.m_cipher;
        m_templateMetadata = builder.m_templateMetadata;
        m_templateLink = builder.m_templateLink;
        m_inPortsBarBounds = builder.m_inPortsBarBounds;
        m_outPortsBarBounds = builder.m_outPortsBarBounds;

        m_exceptionTree = SimpleLoadExceptionTree.map(builder.m_exceptionalChildren);
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
        m_bounds = toCopy.getBounds();
        m_locks = toCopy.getLocks();
        m_jobManager = toCopy.getJobManager();
        m_workflow = toCopy.getWorkflow();
        m_inPorts = toCopy.getInPorts();
        m_outPorts = toCopy.getOutPorts();
        m_cipher = toCopy.getCipher();
        m_templateMetadata = toCopy.getTemplateMetadata();
        m_templateLink = toCopy.getTemplateLink();
        m_inPortsBarBounds = toCopy.getInPortsBarBounds();
        m_outPortsBarBounds = toCopy.getOutPortsBarBounds();
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
    public DefaultMetaNodeDef(MetaNodeDef toCopy) {
        m_id = toCopy.getId();
        m_nodeType = toCopy.getNodeType();
        m_customDescription = toCopy.getCustomDescription();
        m_annotation = toCopy.getAnnotation();
        m_bounds = toCopy.getBounds();
        m_locks = toCopy.getLocks();
        m_jobManager = toCopy.getJobManager();
        m_workflow = toCopy.getWorkflow();
        m_inPorts = toCopy.getInPorts();
        m_outPorts = toCopy.getOutPorts();
        m_cipher = toCopy.getCipher();
        m_templateMetadata = toCopy.getTemplateMetadata();
        m_templateLink = toCopy.getTemplateLink();
        m_inPortsBarBounds = toCopy.getInPortsBarBounds();
        m_outPortsBarBounds = toCopy.getOutPortsBarBounds();
        
        m_exceptionTree = SimpleLoadExceptionTree.EMPTY;
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
    public LoadExceptionTree<?> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants.
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(MetaNodeDef.Attribute attribute){
        if (m_exceptionTree instanceof LoadException) {
            return Optional.empty();
        }
        // if the tree is not a leaf, it is typed to MetaNodeDef.Attribute
        return ((LoadExceptionTree<MetaNodeDef.Attribute>)m_exceptionTree).getExceptionTree(attribute);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<Integer> getId() {
        return m_id;
    }
    @Override
    public NodeTypeEnum getNodeType() {
        return m_nodeType;
    }
    @Override
    public Optional<String> getCustomDescription() {
        return m_customDescription;
    }
    @Override
    public Optional<NodeAnnotationDef> getAnnotation() {
        return m_annotation;
    }
    @Override
    public Optional<BoundsDef> getBounds() {
        return m_bounds;
    }
    @Override
    public Optional<NodeLocksDef> getLocks() {
        return m_locks;
    }
    @Override
    public Optional<JobManagerDef> getJobManager() {
        return m_jobManager;
    }
    @Override
    public WorkflowDef getWorkflow() {
        return m_workflow;
    }
    @Override
    public Optional<java.util.List<PortDef>> getInPorts() {
        return m_inPorts;
    }
    @Override
    public Optional<java.util.List<PortDef>> getOutPorts() {
        return m_outPorts;
    }
    @Override
    public Optional<CipherDef> getCipher() {
        return m_cipher;
    }
    @Override
    public Optional<TemplateMetadataDef> getTemplateMetadata() {
        return m_templateMetadata;
    }
    @Override
    public Optional<TemplateLinkDef> getTemplateLink() {
        return m_templateLink;
    }
    @Override
    public Optional<BoundsDef> getInPortsBarBounds() {
        return m_inPortsBarBounds;
    }
    @Override
    public Optional<BoundsDef> getOutPortsBarBounds() {
        return m_outPortsBarBounds;
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
        if(LoadExceptionTreeProvider.hasExceptions(annotation)) {
            return Optional.of((DefaultNodeAnnotationDef)annotation.get());
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to bounds.
     */
    @JsonIgnore
    public Optional<LoadException> getBoundsSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.BOUNDS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link BoundsDef} returned by {@link #getBounds()}, this
     * returns the bounds as DefaultBoundsDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultBoundsDef> getFaultyBounds(){
    	final var bounds = getBounds(); 
        if(LoadExceptionTreeProvider.hasExceptions(bounds)) {
            return Optional.of((DefaultBoundsDef)bounds.get());
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
        if(LoadExceptionTreeProvider.hasExceptions(locks)) {
            return Optional.of((DefaultNodeLocksDef)locks.get());
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
        if(LoadExceptionTreeProvider.hasExceptions(jobManager)) {
            return Optional.of((DefaultJobManagerDef)jobManager.get());
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
        if(LoadExceptionTreeProvider.hasExceptions(workflow)) {
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
        if(LoadExceptionTreeProvider.hasExceptions(cipher)) {
            return Optional.of((DefaultCipherDef)cipher.get());
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to templateMetadata.
     */
    @JsonIgnore
    public Optional<LoadException> getTemplateMetadataSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.TEMPLATE_METADATA).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link TemplateMetadataDef} returned by {@link #getTemplateMetadata()}, this
     * returns the templateMetadata as DefaultTemplateMetadataDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultTemplateMetadataDef> getFaultyTemplateMetadata(){
    	final var templateMetadata = getTemplateMetadata(); 
        if(LoadExceptionTreeProvider.hasExceptions(templateMetadata)) {
            return Optional.of((DefaultTemplateMetadataDef)templateMetadata.get());
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to templateLink.
     */
    @JsonIgnore
    public Optional<LoadException> getTemplateLinkSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.TEMPLATE_LINK).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link TemplateLinkDef} returned by {@link #getTemplateLink()}, this
     * returns the templateLink as DefaultTemplateLinkDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultTemplateLinkDef> getFaultyTemplateLink(){
    	final var templateLink = getTemplateLink(); 
        if(LoadExceptionTreeProvider.hasExceptions(templateLink)) {
            return Optional.of((DefaultTemplateLinkDef)templateLink.get());
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to inPortsBarBounds.
     */
    @JsonIgnore
    public Optional<LoadException> getInPortsBarBoundsSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.IN_PORTS_BAR_BOUNDS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link BoundsDef} returned by {@link #getInPortsBarBounds()}, this
     * returns the inPortsBarBounds as DefaultBoundsDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultBoundsDef> getFaultyInPortsBarBounds(){
    	final var inPortsBarBounds = getInPortsBarBounds(); 
        if(LoadExceptionTreeProvider.hasExceptions(inPortsBarBounds)) {
            return Optional.of((DefaultBoundsDef)inPortsBarBounds.get());
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to outPortsBarBounds.
     */
    @JsonIgnore
    public Optional<LoadException> getOutPortsBarBoundsSupplyException(){
    	return getLoadExceptionTree(MetaNodeDef.Attribute.OUT_PORTS_BAR_BOUNDS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link BoundsDef} returned by {@link #getOutPortsBarBounds()}, this
     * returns the outPortsBarBounds as DefaultBoundsDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultBoundsDef> getFaultyOutPortsBarBounds(){
    	final var outPortsBarBounds = getOutPortsBarBounds(); 
        if(LoadExceptionTreeProvider.hasExceptions(outPortsBarBounds)) {
            return Optional.of((DefaultBoundsDef)outPortsBarBounds.get());
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
        equalsBuilder.append(m_templateMetadata, other.m_templateMetadata);
        equalsBuilder.append(m_templateLink, other.m_templateLink);
        equalsBuilder.append(m_inPortsBarBounds, other.m_inPortsBarBounds);
        equalsBuilder.append(m_outPortsBarBounds, other.m_outPortsBarBounds);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_id)
                .append(m_nodeType)
                .append(m_customDescription)
                .append(m_annotation)
                .append(m_bounds)
                .append(m_locks)
                .append(m_jobManager)
                .append(m_workflow)
                .append(m_inPorts)
                .append(m_outPorts)
                .append(m_cipher)
                .append(m_templateMetadata)
                .append(m_templateLink)
                .append(m_inPortsBarBounds)
                .append(m_outPortsBarBounds)
                .toHashCode();
    }

} 
