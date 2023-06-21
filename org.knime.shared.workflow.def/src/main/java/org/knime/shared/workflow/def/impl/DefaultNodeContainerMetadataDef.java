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

import java.time.OffsetDateTime;
import org.knime.shared.workflow.def.LinkDef;

import org.knime.shared.workflow.def.NodeContainerMetadataDef;



// for types that define enums
import org.knime.shared.workflow.def.NodeContainerMetadataDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultNodeContainerMetadataDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @since 5.1
 * @noextend This class is not intended to be subclassed by clients.
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultNodeContainerMetadataDef implements NodeContainerMetadataDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<NodeContainerMetadataDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    /**
     * author of the workflow or component, user-provided
     * @since 5.1
     */
    @JsonProperty("author")
    protected String m_author;

    /**
     * creation date of the metadata
     * @since 5.1
     */
    @JsonProperty("created")
    protected OffsetDateTime m_created;

    /**
     * last-modified date of the metadata
     * @since 5.1
     */
    @JsonProperty("lastModified")
    protected OffsetDateTime m_lastModified;

    /**
     * description text of the workflow or component
     */
    @JsonProperty("description")
    protected String m_description;

    /**
     * list of tags
     * @since 5.1
     */
    @JsonProperty("tags")
    protected java.util.List<String> m_tags;

    /**
     * list of links to additional resources
     * @since 5.1
     */
    @JsonProperty("links")
    protected java.util.List<LinkDef> m_links;

    /**
     * The content type of the rich-text fields (currently &#x60;description&#x60;, &#x60;inPortDescriptions&#x60; and &#x60;outPortDescriptions&#x60;).
     * @since 5.1
     */
    @JsonProperty("contentType")
    protected ContentTypeEnum m_contentType;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultNodeContainerMetadataDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link NodeContainerMetadataDefBuilder}.
     * @param builder source
     */
    DefaultNodeContainerMetadataDef(NodeContainerMetadataDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_author = builder.m_author;
        m_created = builder.m_created;
        m_lastModified = builder.m_lastModified;
        m_description = builder.m_description;
        m_tags = builder.m_tags;
        m_links = builder.m_links;
        m_contentType = builder.m_contentType;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link NodeContainerMetadataDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultNodeContainerMetadataDef(NodeContainerMetadataDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new NodeContainerMetadataDefBuilder().build());
        
        m_author = toCopy.getAuthor();
        m_created = toCopy.getCreated();
        m_lastModified = toCopy.getLastModified();
        m_description = toCopy.getDescription();
        m_tags = toCopy.getTags();
        m_links = toCopy.getLinks();
        m_contentType = toCopy.getContentType();
        if(toCopy instanceof DefaultNodeContainerMetadataDef){
            var childTree = ((DefaultNodeContainerMetadataDef)toCopy).getLoadExceptionTree();                
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
    public DefaultNodeContainerMetadataDef(NodeContainerMetadataDef toCopy) {
        m_author = toCopy.getAuthor();
        m_created = toCopy.getCreated();
        m_lastModified = toCopy.getLastModified();
        m_description = toCopy.getDescription();
        m_tags = toCopy.getTags();
        m_links = toCopy.getLinks();
        m_contentType = toCopy.getContentType();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing NodeContainerMetadata
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultNodeContainerMetadataDef withException(NodeContainerMetadataDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(NodeContainerMetadataDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to NodeContainerMetadataDef.Attribute
            return ((LoadExceptionTree<NodeContainerMetadataDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @since 5.1
     */
    @Override
    public String getAuthor() {
        return m_author;
    }
    /**
     * {@inheritDoc}
     * @since 5.1
     */
    @Override
    public OffsetDateTime getCreated() {
        return m_created;
    }
    /**
     * {@inheritDoc}
     * @since 5.1
     */
    @Override
    public OffsetDateTime getLastModified() {
        return m_lastModified;
    }
    @Override
    public String getDescription() {
        return m_description;
    }
    /**
     * {@inheritDoc}
     * @since 5.1
     */
    @Override
    public java.util.List<String> getTags() {
        return m_tags;
    }
    /**
     * {@inheritDoc}
     * @since 5.1
     */
    @Override
    public java.util.List<LinkDef> getLinks() {
        return m_links;
    }
    /**
     * {@inheritDoc}
     * @since 5.1
     */
    @Override
    public ContentTypeEnum getContentType() {
        return m_contentType;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to author.
     * @since 5.1
     */
    @JsonIgnore
    public Optional<LoadException> getAuthorSupplyException(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.AUTHOR).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to created.
     * @since 5.1
     */
    @JsonIgnore
    public Optional<LoadException> getCreatedSupplyException(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.CREATED).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to lastModified.
     * @since 5.1
     */
    @JsonIgnore
    public Optional<LoadException> getLastModifiedSupplyException(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.LAST_MODIFIED).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to description.
     */
    @JsonIgnore
    public Optional<LoadException> getDescriptionSupplyException(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.DESCRIPTION).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to tags.
     * @since 5.1
     */
    @JsonIgnore
    public Optional<LoadException> getTagsSupplyException(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.TAGS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
    /**
     * @return the Tags that have load exceptions or descendants with load exceptions.
     * @since 5.1
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Optional<LoadExceptionTree<Integer>> getTagsExceptionTree(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.TAGS).map(les -> (LoadExceptionTree<Integer>)les);
    }

    /**
     * @param childIdentifier the element in the list to get the exceptions for
     * @return the load exception associated to the given element of the Tags container.
     * @since 5.1
     */
    @JsonIgnore
    public Optional<LoadException> getTagsElementSupplyException(Integer childIdentifier){
    	return getTagsExceptionTree()//
            // optional load exceptions associated to the tags
            .flatMap(iles -> iles.getExceptionTree(childIdentifier))//
            // if present, it is a LoadExceptionTree, but since this is a type without children, just return the terminal supply exception
            .flatMap(LoadExceptionTree::getSupplyException);
    }
     
    /**
     * @return The supply exception associated to links.
     * @since 5.1
     */
    @JsonIgnore
    public Optional<LoadException> getLinksSupplyException(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.LINKS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
         
    /**
     * @return the {@link LoadExceptionTree} associated to this {{vendorExtensions.containerType}, containing the supply exception for the container itself
     * and all its children with load exceptions.
     * @since 5.1
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<Integer>> getLinksExceptionTree(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.LINKS).map(let -> (LoadExceptionTree<Integer>)let);
    }

     
 
    /**
     * @return The supply exception associated to contentType.
     * @since 5.1
     */
    @JsonIgnore
    public Optional<LoadException> getContentTypeSupplyException(){
    	return getLoadExceptionTree(NodeContainerMetadataDef.Attribute.CONTENT_TYPE).flatMap(LoadExceptionTree::getSupplyException);
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
        DefaultNodeContainerMetadataDef other = (DefaultNodeContainerMetadataDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_author, other.m_author);
        equalsBuilder.append(m_created, other.m_created);
        equalsBuilder.append(m_lastModified, other.m_lastModified);
        equalsBuilder.append(m_description, other.m_description);
        equalsBuilder.append(m_tags, other.m_tags);
        equalsBuilder.append(m_links, other.m_links);
        equalsBuilder.append(m_contentType, other.m_contentType);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_author)
                .append(m_created)
                .append(m_lastModified)
                .append(m_description)
                .append(m_tags)
                .append(m_links)
                .append(m_contentType)
                .toHashCode();
    }

} 
