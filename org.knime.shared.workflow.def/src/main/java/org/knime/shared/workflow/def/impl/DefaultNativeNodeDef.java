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

import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.FilestoreDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.NodeUIInfoDef;
import org.knime.shared.workflow.def.VendorDef;
import org.knime.shared.workflow.def.impl.DefaultConfigurableNodeDef;

import org.knime.shared.workflow.def.NativeNodeDef;



// for types that define enums
import org.knime.shared.workflow.def.NativeNodeDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * The basic executable building block of a workflow. 
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @noextend This class is not intended to be subclassed by clients.
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultNativeNodeDef extends DefaultConfigurableNodeDef implements NativeNodeDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<NativeNodeDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    /**
     * Example value: Table Creator
     */
    @JsonProperty("nodeName")
    protected String m_nodeName;

    /**
     * Qualified class name
     * Example value: org.knime.base.node.io.tablecreator.TableCreator2NodeFactory
     */
    @JsonProperty("factory")
    protected String m_factory;

    /**
     */
    @JsonProperty("factorySettings")
    protected ConfigMapDef m_factorySettings;

    /**
     */
    @JsonProperty("feature")
    protected VendorDef m_feature;

    /**
     */
    @JsonProperty("bundle")
    protected VendorDef m_bundle;

    /**
     */
    @JsonProperty("nodeCreationConfig")
    protected ConfigMapDef m_nodeCreationConfig;

    /**
     */
    @JsonProperty("filestore")
    protected FilestoreDef m_filestore;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultNativeNodeDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link NativeNodeDefBuilder}.
     * @param builder source
     */
    DefaultNativeNodeDef(NativeNodeDefBuilder builder) {
        // TODO make immutable copies!!
        super();
            
        m_id = builder.m_id;
        m_nodeType = builder.m_nodeType;
        m_customDescription = builder.m_customDescription;
        m_annotation = builder.m_annotation;
        m_uiInfo = builder.m_uiInfo;
        m_locks = builder.m_locks;
        m_jobManager = builder.m_jobManager;
        m_modelSettings = builder.m_modelSettings;
        m_internalNodeSubSettings = builder.m_internalNodeSubSettings;
        m_variableSettings = builder.m_variableSettings;
        m_nodeName = builder.m_nodeName;
        m_factory = builder.m_factory;
        m_factorySettings = builder.m_factorySettings;
        m_feature = builder.m_feature;
        m_bundle = builder.m_bundle;
        m_nodeCreationConfig = builder.m_nodeCreationConfig;
        m_filestore = builder.m_filestore;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link NativeNodeDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultNativeNodeDef(NativeNodeDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new NativeNodeDefBuilder().build());
        
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
        m_nodeName = toCopy.getNodeName();
        m_factory = toCopy.getFactory();
        m_factorySettings = toCopy.getFactorySettings();
        m_feature = toCopy.getFeature();
        m_bundle = toCopy.getBundle();
        m_nodeCreationConfig = toCopy.getNodeCreationConfig();
        m_filestore = toCopy.getFilestore();
        if(toCopy instanceof DefaultNativeNodeDef){
            var childTree = ((DefaultNativeNodeDef)toCopy).getLoadExceptionTree();                
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
    public DefaultNativeNodeDef(NativeNodeDef toCopy) {
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
        m_nodeName = toCopy.getNodeName();
        m_factory = toCopy.getFactory();
        m_factorySettings = toCopy.getFactorySettings();
        m_feature = toCopy.getFeature();
        m_bundle = toCopy.getBundle();
        m_nodeCreationConfig = toCopy.getNodeCreationConfig();
        m_filestore = toCopy.getFilestore();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing NativeNode
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultNativeNodeDef withException(NativeNodeDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(NativeNodeDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to NativeNodeDef.Attribute
            return ((LoadExceptionTree<NativeNodeDef.Attribute>)t).getExceptionTree(attribute);
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
    public ConfigMapDef getModelSettings() {
        return m_modelSettings;
    }
    @Override
    public ConfigMapDef getInternalNodeSubSettings() {
        return m_internalNodeSubSettings;
    }
    @Override
    public ConfigMapDef getVariableSettings() {
        return m_variableSettings;
    }
    @Override
    public String getNodeName() {
        return m_nodeName;
    }
    @Override
    public String getFactory() {
        return m_factory;
    }
    @Override
    public ConfigMapDef getFactorySettings() {
        return m_factorySettings;
    }
    @Override
    public VendorDef getFeature() {
        return m_feature;
    }
    @Override
    public VendorDef getBundle() {
        return m_bundle;
    }
    @Override
    public ConfigMapDef getNodeCreationConfig() {
        return m_nodeCreationConfig;
    }
    @Override
    public FilestoreDef getFilestore() {
        return m_filestore;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to id.
     */
    @JsonIgnore
    public Optional<LoadException> getIdSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.ID).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to nodeType.
     */
    @JsonIgnore
    public Optional<LoadException> getNodeTypeSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.NODE_TYPE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to customDescription.
     */
    @JsonIgnore
    public Optional<LoadException> getCustomDescriptionSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.CUSTOM_DESCRIPTION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to annotation.
     */
    @JsonIgnore
    public Optional<LoadException> getAnnotationSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.ANNOTATION).flatMap(LoadExceptionTree::getSupplyException);
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
    	return getLoadExceptionTree(NativeNodeDef.Attribute.UI_INFO).flatMap(LoadExceptionTree::getSupplyException);
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
    	return getLoadExceptionTree(NativeNodeDef.Attribute.LOCKS).flatMap(LoadExceptionTree::getSupplyException);
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
    	return getLoadExceptionTree(NativeNodeDef.Attribute.JOB_MANAGER).flatMap(LoadExceptionTree::getSupplyException);
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
     * @return The supply exception associated to modelSettings.
     */
    @JsonIgnore
    public Optional<LoadException> getModelSettingsSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.MODEL_SETTINGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link ConfigMapDef} returned by {@link #getModelSettings()}, this
     * returns the modelSettings as DefaultConfigMapDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultConfigMapDef> getFaultyModelSettings(){
    	final var modelSettings = getModelSettings(); 
        if(modelSettings instanceof DefaultConfigMapDef && ((DefaultConfigMapDef)modelSettings).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultConfigMapDef)modelSettings);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to internalNodeSubSettings.
     */
    @JsonIgnore
    public Optional<LoadException> getInternalNodeSubSettingsSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.INTERNAL_NODE_SUB_SETTINGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link ConfigMapDef} returned by {@link #getInternalNodeSubSettings()}, this
     * returns the internalNodeSubSettings as DefaultConfigMapDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultConfigMapDef> getFaultyInternalNodeSubSettings(){
    	final var internalNodeSubSettings = getInternalNodeSubSettings(); 
        if(internalNodeSubSettings instanceof DefaultConfigMapDef && ((DefaultConfigMapDef)internalNodeSubSettings).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultConfigMapDef)internalNodeSubSettings);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to variableSettings.
     */
    @JsonIgnore
    public Optional<LoadException> getVariableSettingsSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.VARIABLE_SETTINGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link ConfigMapDef} returned by {@link #getVariableSettings()}, this
     * returns the variableSettings as DefaultConfigMapDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultConfigMapDef> getFaultyVariableSettings(){
    	final var variableSettings = getVariableSettings(); 
        if(variableSettings instanceof DefaultConfigMapDef && ((DefaultConfigMapDef)variableSettings).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultConfigMapDef)variableSettings);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to nodeName.
     */
    @JsonIgnore
    public Optional<LoadException> getNodeNameSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.NODE_NAME).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to factory.
     */
    @JsonIgnore
    public Optional<LoadException> getFactorySupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.FACTORY).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to factorySettings.
     */
    @JsonIgnore
    public Optional<LoadException> getFactorySettingsSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.FACTORY_SETTINGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link ConfigMapDef} returned by {@link #getFactorySettings()}, this
     * returns the factorySettings as DefaultConfigMapDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultConfigMapDef> getFaultyFactorySettings(){
    	final var factorySettings = getFactorySettings(); 
        if(factorySettings instanceof DefaultConfigMapDef && ((DefaultConfigMapDef)factorySettings).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultConfigMapDef)factorySettings);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to feature.
     */
    @JsonIgnore
    public Optional<LoadException> getFeatureSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.FEATURE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link VendorDef} returned by {@link #getFeature()}, this
     * returns the feature as DefaultVendorDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultVendorDef> getFaultyFeature(){
    	final var feature = getFeature(); 
        if(feature instanceof DefaultVendorDef && ((DefaultVendorDef)feature).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultVendorDef)feature);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to bundle.
     */
    @JsonIgnore
    public Optional<LoadException> getBundleSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.BUNDLE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link VendorDef} returned by {@link #getBundle()}, this
     * returns the bundle as DefaultVendorDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultVendorDef> getFaultyBundle(){
    	final var bundle = getBundle(); 
        if(bundle instanceof DefaultVendorDef && ((DefaultVendorDef)bundle).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultVendorDef)bundle);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to nodeCreationConfig.
     */
    @JsonIgnore
    public Optional<LoadException> getNodeCreationConfigSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.NODE_CREATION_CONFIG).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link ConfigMapDef} returned by {@link #getNodeCreationConfig()}, this
     * returns the nodeCreationConfig as DefaultConfigMapDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultConfigMapDef> getFaultyNodeCreationConfig(){
    	final var nodeCreationConfig = getNodeCreationConfig(); 
        if(nodeCreationConfig instanceof DefaultConfigMapDef && ((DefaultConfigMapDef)nodeCreationConfig).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultConfigMapDef)nodeCreationConfig);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to filestore.
     */
    @JsonIgnore
    public Optional<LoadException> getFilestoreSupplyException(){
    	return getLoadExceptionTree(NativeNodeDef.Attribute.FILESTORE).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link FilestoreDef} returned by {@link #getFilestore()}, this
     * returns the filestore as DefaultFilestoreDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultFilestoreDef> getFaultyFilestore(){
    	final var filestore = getFilestore(); 
        if(filestore instanceof DefaultFilestoreDef && ((DefaultFilestoreDef)filestore).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultFilestoreDef)filestore);
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
        DefaultNativeNodeDef other = (DefaultNativeNodeDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_nodeName, other.m_nodeName);
        equalsBuilder.append(m_factory, other.m_factory);
        equalsBuilder.append(m_factorySettings, other.m_factorySettings);
        equalsBuilder.append(m_feature, other.m_feature);
        equalsBuilder.append(m_bundle, other.m_bundle);
        equalsBuilder.append(m_nodeCreationConfig, other.m_nodeCreationConfig);
        equalsBuilder.append(m_filestore, other.m_filestore);
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
                .append(m_modelSettings)
                .append(m_internalNodeSubSettings)
                .append(m_variableSettings)
                .append(m_nodeName)
                .append(m_factory)
                .append(m_factorySettings)
                .append(m_feature)
                .append(m_bundle)
                .append(m_nodeCreationConfig)
                .append(m_filestore)
                .toHashCode();
    }

} 
