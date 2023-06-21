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

import org.knime.shared.workflow.def.CreatorDef;

import org.knime.shared.workflow.def.StandaloneDef;



// for types that define enums
import org.knime.shared.workflow.def.StandaloneDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * Represents a standalone, loadable unit for editing in the workflow editor. In case this is a workflow, it is also executable. Standalone components and metanodes cannot be executed but referenced into a workflow and updated when the referenced component/metanode changed. 
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @noextend This class is not intended to be subclassed by clients.
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultStandaloneDef implements StandaloneDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<StandaloneDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    /**
     */
    @JsonProperty("creator")
    protected CreatorDef m_creator;

    /**
     * This is either a RootWorkflow or a Component or a Metanode, which one is indicated by contentType. Having a workflow is useful when adding version information to copied content. 
     */
    @JsonProperty("contents")
    protected Object m_contents;

    /**
     * Describes the type of the contents field.
     */
    @JsonProperty("contentType")
    protected ContentTypeEnum m_contentType;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultStandaloneDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link StandaloneDefBuilder}.
     * @param builder source
     */
    DefaultStandaloneDef(StandaloneDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_creator = builder.m_creator;
        m_contents = builder.m_contents;
        m_contentType = builder.m_contentType;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link StandaloneDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultStandaloneDef(StandaloneDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new StandaloneDefBuilder().build());
        
        m_creator = toCopy.getCreator();
        m_contents = toCopy.getContents();
        m_contentType = toCopy.getContentType();
        if(toCopy instanceof DefaultStandaloneDef){
            var childTree = ((DefaultStandaloneDef)toCopy).getLoadExceptionTree();                
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
    public DefaultStandaloneDef(StandaloneDef toCopy) {
        m_creator = toCopy.getCreator();
        m_contents = toCopy.getContents();
        m_contentType = toCopy.getContentType();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing Standalone
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultStandaloneDef withException(StandaloneDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(StandaloneDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to StandaloneDef.Attribute
            return ((LoadExceptionTree<StandaloneDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public CreatorDef getCreator() {
        return m_creator;
    }
    @Override
    public Object getContents() {
        return m_contents;
    }
    @Override
    public ContentTypeEnum getContentType() {
        return m_contentType;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to creator.
     */
    @JsonIgnore
    public Optional<LoadException> getCreatorSupplyException(){
    	return getLoadExceptionTree(StandaloneDef.Attribute.CREATOR).flatMap(LoadExceptionTree::getSupplyException);
    }
    
     
    /**
     * @return If there are {@link LoadException}s related to the {@link CreatorDef} returned by {@link #getCreator()}, this
     * returns the creator as DefaultCreatorDef which provides getters for the exceptions. Otherwise an empty optional.
     */
    @JsonIgnore
    public Optional<DefaultCreatorDef> getFaultyCreator(){
    	final var creator = getCreator(); 
        if(creator instanceof DefaultCreatorDef && ((DefaultCreatorDef)creator).getLoadExceptionTree().map(LoadExceptionTree::hasExceptions).orElse(false)) {
            return Optional.of((DefaultCreatorDef)creator);
        }
    	return Optional.empty();
    }
         
 
    /**
     * @return The supply exception associated to contents.
     */
    @JsonIgnore
    public Optional<LoadException> getContentsSupplyException(){
    	return getLoadExceptionTree(StandaloneDef.Attribute.CONTENTS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to contentType.
     */
    @JsonIgnore
    public Optional<LoadException> getContentTypeSupplyException(){
    	return getLoadExceptionTree(StandaloneDef.Attribute.CONTENT_TYPE).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultStandaloneDef other = (DefaultStandaloneDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_creator, other.m_creator);
        equalsBuilder.append(m_contents, other.m_contents);
        equalsBuilder.append(m_contentType, other.m_contentType);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_creator)
                .append(m_contents)
                .append(m_contentType)
                .toHashCode();
    }

} 
