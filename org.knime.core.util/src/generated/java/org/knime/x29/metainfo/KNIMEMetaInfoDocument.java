/*
 * An XML document type.
 * Localname: KNIMEMetaInfo
 * Namespace: http://www.knime.org/2.9/metainfo
 * Java type: org.knime.x29.metainfo.KNIMEMetaInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.knime.x29.metainfo;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 *
 * @since 6.0
 *
 * A document containing one KNIMEMetaInfo(@http://www.knime.org/2.9/metainfo) element.
 *
 * This is a complex type.
 */
public interface KNIMEMetaInfoDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.knime.x29.metainfo.KNIMEMetaInfoDocument> Factory = new DocumentFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "knimemetainfo6bc1doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * @since 6.0
     */
    org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo getKNIMEMetaInfo();

    /**
     * @since 6.0
     */
    void setKNIMEMetaInfo(org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo knimeMetaInfo);

    /**
     * Appends and returns a new empty "KNIMEMetaInfo" element
     */
    org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo addNewKNIMEMetaInfo();

    /**
     * An XML KNIMEMetaInfo(@http://www.knime.org/2.9/metainfo).
     *
     * This is a complex type.
     */
    public interface KNIMEMetaInfo extends org.apache.xmlbeans.XmlObject {
        ElementFactory<org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "knimemetainfo2ebeelemtype");
        org.apache.xmlbeans.SchemaType type = Factory.getType();


        /**
         * Gets a List of "element" elements
         */
        java.util.List<org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element> getElementList();

        /**
         * Gets array of all "element" elements
         */
        org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element[] getElementArray();

        /**
         * Gets ith "element" element
         */
        org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element getElementArray(int i);

        /**
         * Returns number of "element" element
         */
        int sizeOfElementArray();

        /**
         * Sets array of all "element" element
         */
        void setElementArray(org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element[] elementArray);

        /**
         * Sets ith "element" element
         */
        void setElementArray(int i, org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element element);

        /**
         * Inserts and returns a new empty value (as xml) as the ith "element" element
         */
        org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element insertNewElement(int i);

        /**
         * Appends and returns a new empty value (as xml) as the last "element" element
         */
        org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element addNewElement();

        /**
         * Removes the ith "element" element
         */
        void removeElement(int i);

        /**
         * Gets the "nrOfElements" attribute
         */
        java.math.BigInteger getNrOfElements();

        /**
         * True if has "nrOfElements" attribute
         */
        boolean isSetNrOfElements();

        /**
         * Sets the "nrOfElements" attribute
         */
        void setNrOfElements(java.math.BigInteger nrOfElements);

        /**
         * Unsets the "nrOfElements" attribute
         */
        void unsetNrOfElements();

        /**
         * An XML element(@http://www.knime.org/2.9/metainfo).
         *
         * This is an atomic type that is a restriction of org.knime.x29.metainfo.KNIMEMetaInfoDocument$KNIMEMetaInfo$Element.
         */
        public interface Element extends org.apache.xmlbeans.XmlString {
            ElementFactory<org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "element7026elemtype");
            org.apache.xmlbeans.SchemaType type = Factory.getType();


            /**
             * Gets the "name" attribute
             */
            java.lang.String getName();

            /**
             * Sets the "name" attribute
             */
            void setName(java.lang.String name);

            /**
             * Gets the "form" attribute
             */
            java.lang.String getForm();

            /**
             * True if has "form" attribute
             */
            boolean isSetForm();

            /**
             * Sets the "form" attribute
             */
            void setForm(java.lang.String form);

            /**
             * Unsets the "form" attribute
             */
            void unsetForm();

            /**
             * Gets the "read-only" attribute
             */
            boolean getReadOnly();

            /**
             * True if has "read-only" attribute
             */
            boolean isSetReadOnly();

            /**
             * Sets the "read-only" attribute
             */
            void setReadOnly(boolean readOnly);

            /**
             * Unsets the "read-only" attribute
             */
            void unsetReadOnly();
        }
    }
}
