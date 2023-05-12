/*
 * XML Type:  node-container-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 *
 * @since 5.24
 *
 * An XML node-container-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0).
 *
 * This is a complex type.
 */
public interface NodeContainerMetadata extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata> Factory = new DocumentFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "nodecontainermetadata0febtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "author" element
     */
    java.lang.String getAuthor();

    /**
     * True if has "author" element
     */
    boolean isSetAuthor();

    /**
     * Sets the "author" element
     */
    void setAuthor(java.lang.String author);

    /**
     * Unsets the "author" element
     */
    void unsetAuthor();

    /**
     * Gets the "created" element
     */
    java.util.Calendar getCreated();

    /**
     * True if has "created" element
     */
    boolean isSetCreated();

    /**
     * Sets the "created" element
     */
    void setCreated(java.util.Calendar created);

    /**
     * Unsets the "created" element
     */
    void unsetCreated();

    /**
     * Gets the "last-modified" element
     */
    java.util.Calendar getLastModified();

    /**
     * Sets the "last-modified" element
     */
    void setLastModified(java.util.Calendar lastModified);

    /**
     * Gets the "description" element
     */
    java.lang.String getDescription();

    /**
     * Sets the "description" element
     */
    void setDescription(java.lang.String description);

    /**
     * Gets the "tags" element
     */
    org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags getTags();

    /**
     * True if has "tags" element
     */
    boolean isSetTags();

    /**
     * Sets the "tags" element
     */
    void setTags(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags tags);

    /**
     * Appends and returns a new empty "tags" element
     */
    org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags addNewTags();

    /**
     * Unsets the "tags" element
     */
    void unsetTags();

    /**
     * Gets the "links" element
     */
    org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links getLinks();

    /**
     * True if has "links" element
     */
    boolean isSetLinks();

    /**
     * Sets the "links" element
     */
    void setLinks(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links links);

    /**
     * Appends and returns a new empty "links" element
     */
    org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links addNewLinks();

    /**
     * Unsets the "links" element
     */
    void unsetLinks();

    /**
     * Gets the "content-type" attribute
     */
    org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.ContentType.Enum getContentType();

    /**
     * Sets the "content-type" attribute
     */
    void setContentType(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.ContentType.Enum contentType);

    /**
     * An XML tags(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is a complex type.
     */
    public interface Tags extends org.apache.xmlbeans.XmlObject {
        ElementFactory<org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Tags> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "tags0ea0elemtype");
        org.apache.xmlbeans.SchemaType type = Factory.getType();


        /**
         * Gets a List of "tag" elements
         */
        java.util.List<java.lang.String> getTagList();

        /**
         * Gets array of all "tag" elements
         */
        java.lang.String[] getTagArray();

        /**
         * Gets ith "tag" element
         */
        java.lang.String getTagArray(int i);

        /**
         * Gets (as xml) ith "tag" element
         */
        org.apache.xmlbeans.XmlString xgetTagArray(int i);

        /**
         * Returns number of "tag" element
         */
        int sizeOfTagArray();

        /**
         * Sets array of all "tag" element
         */
        void setTagArray(java.lang.String[] tagArray);

        /**
         * Sets ith "tag" element
         */
        void setTagArray(int i, java.lang.String tag);

        /**
         * Sets (as xml) array of all "tag" element
         */
        void xsetTagArray(org.apache.xmlbeans.XmlString[] tagArray);

        /**
         * Sets (as xml) ith "tag" element
         */
        void xsetTagArray(int i, org.apache.xmlbeans.XmlString tag);

        /**
         * Inserts the value as the ith "tag" element
         */
        void insertTag(int i, java.lang.String tag);

        /**
         * Appends the value as the last "tag" element
         */
        void addTag(java.lang.String tag);

        /**
         * Inserts and returns a new empty value (as xml) as the ith "tag" element
         */
        org.apache.xmlbeans.XmlString insertNewTag(int i);

        /**
         * Appends and returns a new empty value (as xml) as the last "tag" element
         */
        org.apache.xmlbeans.XmlString addNewTag();

        /**
         * Removes the ith "tag" element
         */
        void removeTag(int i);
    }

    /**
     * An XML links(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is a complex type.
     */
    public interface Links extends org.apache.xmlbeans.XmlObject {
        ElementFactory<org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "links6410elemtype");
        org.apache.xmlbeans.SchemaType type = Factory.getType();


        /**
         * Gets a List of "link" elements
         */
        java.util.List<org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link> getLinkList();

        /**
         * Gets array of all "link" elements
         */
        org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link[] getLinkArray();

        /**
         * Gets ith "link" element
         */
        org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link getLinkArray(int i);

        /**
         * Returns number of "link" element
         */
        int sizeOfLinkArray();

        /**
         * Sets array of all "link" element
         */
        void setLinkArray(org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link[] linkArray);

        /**
         * Sets ith "link" element
         */
        void setLinkArray(int i, org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link link);

        /**
         * Inserts and returns a new empty value (as xml) as the ith "link" element
         */
        org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link insertNewLink(int i);

        /**
         * Appends and returns a new empty value (as xml) as the last "link" element
         */
        org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link addNewLink();

        /**
         * Removes the ith "link" element
         */
        void removeLink(int i);

        /**
         * An XML link(@http://www.knime.org/core/node/workflow/metadata/v1.0).
         *
         * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata$Links$Link.
         */
        public interface Link extends org.apache.xmlbeans.XmlString {
            ElementFactory<org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.Links.Link> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "link7ea6elemtype");
            org.apache.xmlbeans.SchemaType type = Factory.getType();


            /**
             * Gets the "href" attribute
             */
            java.lang.String getHref();

            /**
             * Sets the "href" attribute
             */
            void setHref(java.lang.String href);
        }
    }

    /**
     * An XML content-type(@).
     *
     * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata$ContentType.
     */
    public interface ContentType extends org.apache.xmlbeans.XmlString {
        ElementFactory<org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata.ContentType> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "contenttypea219attrtype");
        org.apache.xmlbeans.SchemaType type = Factory.getType();


        org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
        void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

        Enum TEXT_PLAIN = Enum.forString("text/plain");
        Enum TEXT_HTML = Enum.forString("text/html");

        int INT_TEXT_PLAIN = Enum.INT_TEXT_PLAIN;
        int INT_TEXT_HTML = Enum.INT_TEXT_HTML;

        /**
         * Enumeration value class for org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata$ContentType.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_TEXT_PLAIN
         * Enum.forString(s); // returns the enum value for a string
         * Enum.forInt(i); // returns the enum value for an int
         * </pre>
         * Enumeration objects are immutable singleton objects that
         * can be compared using == object equality. They have no
         * public constructor. See the constants defined within this
         * class for all the valid values.
         */
        final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {
            /**
             * Returns the enum value for a string, or null if none.
             */
            public static Enum forString(java.lang.String s) {
                return (Enum)table.forString(s);
            }

            /**
             * Returns the enum value corresponding to an int, or null if none.
             */
            public static Enum forInt(int i) {
                return (Enum)table.forInt(i);
            }

            private Enum(java.lang.String s, int i) {
                super(s, i);
            }

            static final int INT_TEXT_PLAIN = 1;
            static final int INT_TEXT_HTML = 2;

            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
                new Enum("text/plain", INT_TEXT_PLAIN),
                new Enum("text/html", INT_TEXT_HTML),
            });
            private static final long serialVersionUID = 1L;
            private java.lang.Object readResolve() {
                return forInt(intValue());
            }
        }
    }
}
