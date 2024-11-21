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

import java.time.OffsetDateTime;
import org.knime.shared.workflow.def.LinkDef;
import org.knime.shared.workflow.def.impl.NodeContainerMetadataDefBuilder;


import org.knime.shared.workflow.def.NodeContainerMetadataDef.*;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.WorkflowMetadataDef;
// for types that define enums
import org.knime.shared.workflow.def.WorkflowMetadataDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * WorkflowMetadataDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 * @since 5.1
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class WorkflowMetadataDefBuilder {

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
    Map<WorkflowMetadataDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(WorkflowMetadataDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_author;
    

    OffsetDateTime m_created;
    

    OffsetDateTime m_lastModified;
    

    String m_description;
    

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<String> m_tags = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setTags these are added to m_tags in build */
    private java.util.List<String> m_tagsBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_tagsContainerSupplyException; 
    
    /** Associates an offset in {@link #m_tags} to the supply exception that caused its addition to the list (i.e., it's a forced default value). Merged during {@link #build()} with {@link m_tagsContainerSupplyException} into a single {@link LoadExceptionTree} */
    private java.util.Map<Integer, LoadException> m_tagsElementSupplyExceptions = new java.util.HashMap<>();

    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<LinkDef> m_links = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setLinks these are added to m_links in build */
    private java.util.List<LinkDef> m_linksBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_linksContainerSupplyException; 
    
    ContentTypeEnum m_contentType;
    

    /**
     * Create a new builder.
     */
    public WorkflowMetadataDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public WorkflowMetadataDefBuilder(final WorkflowMetadataDef toCopy) {
        m_author = toCopy.getAuthor();
        m_created = toCopy.getCreated();
        m_lastModified = toCopy.getLastModified();
        m_description = toCopy.getDescription();
        m_tags = toCopy.getTags();
        m_links = toCopy.getLinks();
        m_contentType = toCopy.getContentType();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for author
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param author author of the workflow or component, user-provided
     * @return this builder for fluent API.
     * @since 5.1
     */ 
    public WorkflowMetadataDefBuilder setAuthor(final String author) {
        setAuthor(() -> author, author);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowMetadataDef.Attribute.AUTHOR)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowMetadataDef.Attribute.AUTHOR)} will return the exception.
     * 
     * @param author see {@link WorkflowMetadataDef#getAuthor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setAuthor(String)
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setAuthor(final FallibleSupplier<String> author, String defaultValue) {
        java.util.Objects.requireNonNull(author, () -> "No supplier for author provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowMetadataDef.Attribute.AUTHOR);
        try {
            m_author = author.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_author = defaultValue;
            m_exceptionalChildren.put(WorkflowMetadataDef.Attribute.AUTHOR, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for created
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param created creation date of the metadata
     * @return this builder for fluent API.
     * @since 5.1
     */ 
    public WorkflowMetadataDefBuilder setCreated(final OffsetDateTime created) {
        setCreated(() -> created, created);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowMetadataDef.Attribute.CREATED)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowMetadataDef.Attribute.CREATED)} will return the exception.
     * 
     * @param created see {@link WorkflowMetadataDef#getCreated}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCreated(OffsetDateTime)
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setCreated(final FallibleSupplier<OffsetDateTime> created, OffsetDateTime defaultValue) {
        java.util.Objects.requireNonNull(created, () -> "No supplier for created provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowMetadataDef.Attribute.CREATED);
        try {
            m_created = created.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_created = defaultValue;
            m_exceptionalChildren.put(WorkflowMetadataDef.Attribute.CREATED, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for lastModified
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param lastModified last-modified date of the metadata
     * @return this builder for fluent API.
     * @since 5.1
     */ 
    public WorkflowMetadataDefBuilder setLastModified(final OffsetDateTime lastModified) {
        setLastModified(() -> lastModified, lastModified);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowMetadataDef.Attribute.LAST_MODIFIED)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowMetadataDef.Attribute.LAST_MODIFIED)} will return the exception.
     * 
     * @param lastModified see {@link WorkflowMetadataDef#getLastModified}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLastModified(OffsetDateTime)
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setLastModified(final FallibleSupplier<OffsetDateTime> lastModified, OffsetDateTime defaultValue) {
        java.util.Objects.requireNonNull(lastModified, () -> "No supplier for lastModified provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowMetadataDef.Attribute.LAST_MODIFIED);
        try {
            m_lastModified = lastModified.get();

            if(m_lastModified == null) {
                throw new IllegalArgumentException("lastModified is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_lastModified = defaultValue;
            m_exceptionalChildren.put(WorkflowMetadataDef.Attribute.LAST_MODIFIED, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for description
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param description description text of the workflow or component
     * @return this builder for fluent API.
     */ 
    public WorkflowMetadataDefBuilder setDescription(final String description) {
        setDescription(() -> description, description);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowMetadataDef.Attribute.DESCRIPTION)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowMetadataDef.Attribute.DESCRIPTION)} will return the exception.
     * 
     * @param description see {@link WorkflowMetadataDef#getDescription}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setDescription(String)
     */
    public WorkflowMetadataDefBuilder setDescription(final FallibleSupplier<String> description, String defaultValue) {
        java.util.Objects.requireNonNull(description, () -> "No supplier for description provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowMetadataDef.Attribute.DESCRIPTION);
        try {
            m_description = description.get();

            if(m_description == null) {
                throw new IllegalArgumentException("description is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_description = defaultValue;
            m_exceptionalChildren.put(WorkflowMetadataDef.Attribute.DESCRIPTION, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for tags
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the tags list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToTags} will be inserted at the end of the list.
     * @param tags list of tags
     * @return this for fluent API
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setTags(final java.util.List<String> tags) {
        setTags(() -> tags);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the tags list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the tags list). 
     * {@code hasExceptions(WorkflowMetadataDef.Attribute.TAGS)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowMetadataDef.Attribute.TAGS)} will return the exception.
     * 
     * @param tags see {@link WorkflowMetadataDef#getTags}
     * 
     * @return this builder for fluent API.
     * @see #setTags(java.util.List<String>)
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setTags(final FallibleSupplier<java.util.List<String>> tags) {
        java.util.Objects.requireNonNull(tags, () -> "No supplier for tags provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowMetadataDef.Attribute.TAGS);
        try {
            m_tagsBulkElements = tags.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_tagsBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_tagsContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the tags list
     * @return this builder for fluent API
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder addToTags(String value){
    	addToTags(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link WorkflowMetadataDef#getTags}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the tags list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder addToTags(FallibleSupplier<String> value, String defaultValue) {
        String toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            m_tagsElementSupplyExceptions.put(m_tags.size(), supplyException);
            toAdd = defaultValue;
        }
        m_tags.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for links
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the links list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToLinks} will be inserted at the end of the list.
     * @param links list of links to additional resources
     * @return this for fluent API
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setLinks(final java.util.List<LinkDef> links) {
        setLinks(() -> links);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the links list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the links list). 
     * {@code hasExceptions(WorkflowMetadataDef.Attribute.LINKS)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowMetadataDef.Attribute.LINKS)} will return the exception.
     * 
     * @param links see {@link WorkflowMetadataDef#getLinks}
     * 
     * @return this builder for fluent API.
     * @see #setLinks(java.util.List<LinkDef>)
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setLinks(final FallibleSupplier<java.util.List<LinkDef>> links) {
        java.util.Objects.requireNonNull(links, () -> "No supplier for links provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowMetadataDef.Attribute.LINKS);
        try {
            m_linksBulkElements = links.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_linksBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_linksContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the links list
     * @return this builder for fluent API
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder addToLinks(LinkDef value){
    	addToLinks(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link WorkflowMetadataDef#getLinks}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the links list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder addToLinks(FallibleSupplier<LinkDef> value, LinkDef defaultValue) {
        LinkDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new DefaultLinkDef(defaultValue, supplyException);
        }
        m_links.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for contentType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param contentType The content type of the rich-text fields (currently &#x60;description&#x60;, &#x60;inPortDescriptions&#x60; and &#x60;outPortDescriptions&#x60;).
     * @return this builder for fluent API.
     * @since 5.1
     */ 
    public WorkflowMetadataDefBuilder setContentType(final ContentTypeEnum contentType) {
        setContentType(() -> contentType, contentType);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowMetadataDef.Attribute.CONTENT_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowMetadataDef.Attribute.CONTENT_TYPE)} will return the exception.
     * 
     * @param contentType see {@link WorkflowMetadataDef#getContentType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setContentType(ContentTypeEnum)
     * @since 5.1
     */
    public WorkflowMetadataDefBuilder setContentType(final FallibleSupplier<ContentTypeEnum> contentType, ContentTypeEnum defaultValue) {
        java.util.Objects.requireNonNull(contentType, () -> "No supplier for contentType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowMetadataDef.Attribute.CONTENT_TYPE);
        try {
            m_contentType = contentType.get();

            if(m_contentType == null) {
                throw new IllegalArgumentException("contentType is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_contentType = defaultValue;
            m_exceptionalChildren.put(WorkflowMetadataDef.Attribute.CONTENT_TYPE, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link WorkflowMetadataDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultWorkflowMetadataDef build() {
        
        // in case the setter has never been called, the field is still null, but no load exception was recorded. Do that now.
        if(m_lastModified == null) setLastModified(null);
        
        // in case the setter has never been called, the field is still null, but no load exception was recorded. Do that now.
        if(m_description == null) setDescription(null);
        
        // in case the setter has never been called, the field is still null, but no load exception was recorded. Do that now.
        if(m_contentType == null) setContentType(null);
        
    	
        // contains the elements set with #setTags (those added with #addToTags have already been inserted into m_tags)
        m_tagsBulkElements = java.util.Objects.requireNonNullElse(m_tagsBulkElements, java.util.List.of());
        m_tags.addAll(0, m_tagsBulkElements);
        
        var tagsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_tagsElementSupplyExceptions, m_tagsContainerSupplyException);
                if(tagsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(WorkflowMetadataDef.Attribute.TAGS, tagsLoadExceptionTree);
        }
        
        // contains the elements set with #setLinks (those added with #addToLinks have already been inserted into m_links)
        m_linksBulkElements = java.util.Objects.requireNonNullElse(m_linksBulkElements, java.util.List.of());
        m_links.addAll(0, m_linksBulkElements);
                
        var linksLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_links, m_linksContainerSupplyException);
        if(linksLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(WorkflowMetadataDef.Attribute.LINKS, linksLoadExceptionTree);
        }
        
        return new DefaultWorkflowMetadataDef(this);
    }    

}
