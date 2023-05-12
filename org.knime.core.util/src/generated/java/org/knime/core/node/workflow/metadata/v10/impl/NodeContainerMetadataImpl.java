/*
 * XML Type:  node-container-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @since 6.0
 *
 * An XML node-container-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0).
 *
 * This is a complex type.
 */
public class NodeContainerMetadataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata {
    private static final long serialVersionUID = 1L;

    public NodeContainerMetadataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "author"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "created"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "last-modified"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "description"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "tags"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "links"),
        new QName("", "content-type"),
    };


    /**
     * Gets the "author" element
     */
    @Override
    public java.lang.String getAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * True if has "author" element
     */
    @Override
    public boolean isSetAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "author" element
     */
    @Override
    public void setAuthor(java.lang.String author) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(author);
        }
    }

    /**
     * Unsets the "author" element
     */
    @Override
    public void unsetAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "created" element
     */
    @Override
    public java.util.Calendar getCreated() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * True if has "created" element
     */
    @Override
    public boolean isSetCreated() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "created" element
     */
    @Override
    public void setCreated(java.util.Calendar created) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setCalendarValue(created);
        }
    }

    /**
     * Unsets the "created" element
     */
    @Override
    public void unsetCreated() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "last-modified" element
     */
    @Override
    public java.util.Calendar getLastModified() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Sets the "last-modified" element
     */
    @Override
    public void setLastModified(java.util.Calendar lastModified) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.setCalendarValue(lastModified);
        }
    }

    /**
     * Gets the "description" element
     */
    @Override
    public java.lang.String getDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Sets the "description" element
     */
    @Override
    public void setDescription(java.lang.String description) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(description);
        }
    }

    /**
     * Gets the "tags" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags getTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags target = null;
            target = (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tags" element
     */
    @Override
    public boolean isSetTags() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "tags" element
     */
    @Override
    public void setTags(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags tags) {
        generatedSetterHelperImpl(tags, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tags" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags addNewTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags target = null;
            target = (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "tags" element
     */
    @Override
    public void unsetTags() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "links" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links getLinks() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links target = null;
            target = (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "links" element
     */
    @Override
    public boolean isSetLinks() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "links" element
     */
    @Override
    public void setLinks(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links links) {
        generatedSetterHelperImpl(links, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "links" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links addNewLinks() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links target = null;
            target = (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "links" element
     */
    @Override
    public void unsetLinks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "content-type" attribute
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.ContentType.Enum getContentType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.ContentType.Enum)target.getEnumValue();
        }
    }

    /**
     * Sets the "content-type" attribute
     */
    @Override
    public void setContentType(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.ContentType.Enum contentType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(contentType);
        }
    }
    /**
     * An XML tags(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is a complex type.
     */
    public static class TagsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags {
        private static final long serialVersionUID = 1L;

        public TagsImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final QName[] PROPERTY_QNAME = {
            new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "tag"),
        };


        /**
         * Gets a List of "tag" elements
         */
        @Override
        public java.util.List<java.lang.String> getTagList() {
            synchronized (monitor()) {
                check_orphaned();
                return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                    this::getTagArray,
                    this::setTagArray,
                    this::insertTag,
                    this::removeTag,
                    this::sizeOfTagArray
                );
            }
        }

        /**
         * Gets array of all "tag" elements
         */
        @Override
        public java.lang.String[] getTagArray() {
            return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
        }

        /**
         * Gets ith "tag" element
         */
        @Override
        public java.lang.String getTagArray(int i) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target.getStringValue();
            }
        }

        /**
         * Gets (as xml) ith "tag" element
         */
        @Override
        public org.apache.xmlbeans.XmlString xgetTagArray(int i) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }

        /**
         * Returns number of "tag" element
         */
        @Override
        public int sizeOfTagArray() {
            synchronized (monitor()) {
                check_orphaned();
                return get_store().count_elements(PROPERTY_QNAME[0]);
            }
        }

        /**
         * Sets array of all "tag" element
         */
        @Override
        public void setTagArray(java.lang.String[] tagArray) {
            synchronized (monitor()) {
                check_orphaned();
                arraySetterHelper(tagArray, PROPERTY_QNAME[0]);
            }
        }

        /**
         * Sets ith "tag" element
         */
        @Override
        public void setTagArray(int i, java.lang.String tag) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                target.setStringValue(tag);
            }
        }

        /**
         * Sets (as xml) array of all "tag" element
         */
        @Override
        public void xsetTagArray(org.apache.xmlbeans.XmlString[]tagArray) {
            synchronized (monitor()) {
                check_orphaned();
                arraySetterHelper(tagArray, PROPERTY_QNAME[0]);
            }
        }

        /**
         * Sets (as xml) ith "tag" element
         */
        @Override
        public void xsetTagArray(int i, org.apache.xmlbeans.XmlString tag) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                target.set(tag);
            }
        }

        /**
         * Inserts the value as the ith "tag" element
         */
        @Override
        public void insertTag(int i, java.lang.String tag) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target =
                    (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
                target.setStringValue(tag);
            }
        }

        /**
         * Appends the value as the last "tag" element
         */
        @Override
        public void addTag(java.lang.String tag) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
                target.setStringValue(tag);
            }
        }

        /**
         * Inserts and returns a new empty value (as xml) as the ith "tag" element
         */
        @Override
        public org.apache.xmlbeans.XmlString insertNewTag(int i) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
                return target;
            }
        }

        /**
         * Appends and returns a new empty value (as xml) as the last "tag" element
         */
        @Override
        public org.apache.xmlbeans.XmlString addNewTag() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
                return target;
            }
        }

        /**
         * Removes the ith "tag" element
         */
        @Override
        public void removeTag(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(PROPERTY_QNAME[0], i);
            }
        }
    }
    /**
     * An XML links(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is a complex type.
     */
    public static class LinksImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links {
        private static final long serialVersionUID = 1L;

        public LinksImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final QName[] PROPERTY_QNAME = {
            new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "link"),
        };


        /**
         * Gets a List of "link" elements
         */
        @Override
        public java.util.List<org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link> getLinkList() {
            synchronized (monitor()) {
                check_orphaned();
                return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                    this::getLinkArray,
                    this::setLinkArray,
                    this::insertNewLink,
                    this::removeLink,
                    this::sizeOfLinkArray
                );
            }
        }

        /**
         * Gets array of all "link" elements
         */
        @Override
        public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link[] getLinkArray() {
            return getXmlObjectArray(PROPERTY_QNAME[0], new org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link[0]);
        }

        /**
         * Gets ith "link" element
         */
        @Override
        public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link getLinkArray(int i) {
            synchronized (monitor()) {
                check_orphaned();
                org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link target = null;
                target = (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link)get_store().find_element_user(PROPERTY_QNAME[0], i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }

        /**
         * Returns number of "link" element
         */
        @Override
        public int sizeOfLinkArray() {
            synchronized (monitor()) {
                check_orphaned();
                return get_store().count_elements(PROPERTY_QNAME[0]);
            }
        }

        /**
         * Sets array of all "link" element  WARNING: This method is not atomicaly synchronized.
         */
        @Override
        public void setLinkArray(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link[] linkArray) {
            check_orphaned();
            arraySetterHelper(linkArray, PROPERTY_QNAME[0]);
        }

        /**
         * Sets ith "link" element
         */
        @Override
        public void setLinkArray(int i, org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link link) {
            generatedSetterHelperImpl(link, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }

        /**
         * Inserts and returns a new empty value (as xml) as the ith "link" element
         */
        @Override
        public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link insertNewLink(int i) {
            synchronized (monitor()) {
                check_orphaned();
                org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link target = null;
                target = (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link)get_store().insert_element_user(PROPERTY_QNAME[0], i);
                return target;
            }
        }

        /**
         * Appends and returns a new empty value (as xml) as the last "link" element
         */
        @Override
        public org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link addNewLink() {
            synchronized (monitor()) {
                check_orphaned();
                org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link target = null;
                target = (org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link)get_store().add_element_user(PROPERTY_QNAME[0]);
                return target;
            }
        }

        /**
         * Removes the ith "link" element
         */
        @Override
        public void removeLink(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(PROPERTY_QNAME[0], i);
            }
        }
        /**
         * An XML link(@http://www.knime.org/core/node/workflow/metadata/v1.0).
         *
         * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata$Links$Link.
         */
        public static class LinkImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link {
            private static final long serialVersionUID = 1L;

            public LinkImpl(org.apache.xmlbeans.SchemaType sType) {
                super(sType, true);
            }

            protected LinkImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
                super(sType, b);
            }

            private static final QName[] PROPERTY_QNAME = {
                new QName("", "href"),
            };


            /**
             * Gets the "href" attribute
             */
            @Override
            public java.lang.String getHref() {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
                    return (target == null) ? null : target.getStringValue();
                }
            }

            /**
             * Sets the "href" attribute
             */
            @Override
            public void setHref(java.lang.String href) {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
                    if (target == null) {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
                    }
                    target.setStringValue(href);
                }
            }
        }
    }
    /**
     * An XML content-type(@).
     *
     * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata$ContentType.
     */
    public static class ContentTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.ContentType {
        private static final long serialVersionUID = 1L;

        public ContentTypeImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType, false);
        }

        protected ContentTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
