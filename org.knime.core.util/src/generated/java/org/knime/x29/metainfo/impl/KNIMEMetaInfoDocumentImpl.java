/*
 * An XML document type.
 * Localname: KNIMEMetaInfo
 * Namespace: http://www.knime.org/2.9/metainfo
 * Java type: org.knime.x29.metainfo.KNIMEMetaInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.knime.x29.metainfo.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @since 6.0
 *
 * A document containing one KNIMEMetaInfo(@http://www.knime.org/2.9/metainfo) element.
 *
 * This is a complex type.
 */
public class KNIMEMetaInfoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.x29.metainfo.KNIMEMetaInfoDocument {
    private static final long serialVersionUID = 1L;

    public KNIMEMetaInfoDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/2.9/metainfo", "KNIMEMetaInfo"),
    };


    /**
     * @since 6.0
     */
    @Override
    public org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo getKNIMEMetaInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo target = null;
            target = (org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "KNIMEMetaInfo" element
     */
    @Override
    public void setKNIMEMetaInfo(org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo knimeMetaInfo) {
        generatedSetterHelperImpl(knimeMetaInfo, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "KNIMEMetaInfo" element
     */
    @Override
    public org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo addNewKNIMEMetaInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo target = null;
            target = (org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
    /**
     * An XML KNIMEMetaInfo(@http://www.knime.org/2.9/metainfo).
     *
     * This is a complex type.
     */
    public static class KNIMEMetaInfoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo {
        private static final long serialVersionUID = 1L;

        public KNIMEMetaInfoImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final QName[] PROPERTY_QNAME = {
            new QName("http://www.knime.org/2.9/metainfo", "element"),
            new QName("", "nrOfElements"),
        };


        /**
         * Gets a List of "element" elements
         */
        @Override
        public java.util.List<org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element> getElementList() {
            synchronized (monitor()) {
                check_orphaned();
                return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                    this::getElementArray,
                    this::setElementArray,
                    this::insertNewElement,
                    this::removeElement,
                    this::sizeOfElementArray
                );
            }
        }

        /**
         * Gets array of all "element" elements
         */
        @Override
        public org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element[] getElementArray() {
            return getXmlObjectArray(PROPERTY_QNAME[0], new org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element[0]);
        }

        /**
         * Gets ith "element" element
         */
        @Override
        public org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element getElementArray(int i) {
            synchronized (monitor()) {
                check_orphaned();
                org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element target = null;
                target = (org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element)get_store().find_element_user(PROPERTY_QNAME[0], i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }

        /**
         * Returns number of "element" element
         */
        @Override
        public int sizeOfElementArray() {
            synchronized (monitor()) {
                check_orphaned();
                return get_store().count_elements(PROPERTY_QNAME[0]);
            }
        }

        /**
         * Sets array of all "element" element  WARNING: This method is not atomicaly synchronized.
         */
        @Override
        public void setElementArray(org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element[] elementArray) {
            check_orphaned();
            arraySetterHelper(elementArray, PROPERTY_QNAME[0]);
        }

        /**
         * Sets ith "element" element
         */
        @Override
        public void setElementArray(int i, org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element element) {
            generatedSetterHelperImpl(element, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }

        /**
         * Inserts and returns a new empty value (as xml) as the ith "element" element
         */
        @Override
        public org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element insertNewElement(int i) {
            synchronized (monitor()) {
                check_orphaned();
                org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element target = null;
                target = (org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element)get_store().insert_element_user(PROPERTY_QNAME[0], i);
                return target;
            }
        }

        /**
         * Appends and returns a new empty value (as xml) as the last "element" element
         */
        @Override
        public org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element addNewElement() {
            synchronized (monitor()) {
                check_orphaned();
                org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element target = null;
                target = (org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element)get_store().add_element_user(PROPERTY_QNAME[0]);
                return target;
            }
        }

        /**
         * Removes the ith "element" element
         */
        @Override
        public void removeElement(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(PROPERTY_QNAME[0], i);
            }
        }

        /**
         * Gets the "nrOfElements" attribute
         */
        @Override
        public java.math.BigInteger getNrOfElements() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
                return (target == null) ? null : target.getBigIntegerValue();
            }
        }

        /**
         * True if has "nrOfElements" attribute
         */
        @Override
        public boolean isSetNrOfElements() {
            synchronized (monitor()) {
                check_orphaned();
                return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
            }
        }

        /**
         * Sets the "nrOfElements" attribute
         */
        @Override
        public void setNrOfElements(java.math.BigInteger nrOfElements) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
                }
                target.setBigIntegerValue(nrOfElements);
            }
        }

        /**
         * Unsets the "nrOfElements" attribute
         */
        @Override
        public void unsetNrOfElements() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(PROPERTY_QNAME[1]);
            }
        }
        /**
         * An XML element(@http://www.knime.org/2.9/metainfo).
         *
         * This is an atomic type that is a restriction of org.knime.x29.metainfo.KNIMEMetaInfoDocument$KNIMEMetaInfo$Element.
         */
        public static class ElementImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.knime.x29.metainfo.KNIMEMetaInfoDocument.KNIMEMetaInfo.Element {
            private static final long serialVersionUID = 1L;

            public ElementImpl(org.apache.xmlbeans.SchemaType sType) {
                super(sType, true);
            }

            protected ElementImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
                super(sType, b);
            }

            private static final QName[] PROPERTY_QNAME = {
                new QName("", "name"),
                new QName("", "form"),
                new QName("", "read-only"),
            };


            /**
             * Gets the "name" attribute
             */
            @Override
            public java.lang.String getName() {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
                    return (target == null) ? null : target.getStringValue();
                }
            }

            /**
             * Sets the "name" attribute
             */
            @Override
            public void setName(java.lang.String name) {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
                    if (target == null) {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
                    }
                    target.setStringValue(name);
                }
            }

            /**
             * Gets the "form" attribute
             */
            @Override
            public java.lang.String getForm() {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
                    return (target == null) ? null : target.getStringValue();
                }
            }

            /**
             * True if has "form" attribute
             */
            @Override
            public boolean isSetForm() {
                synchronized (monitor()) {
                    check_orphaned();
                    return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
                }
            }

            /**
             * Sets the "form" attribute
             */
            @Override
            public void setForm(java.lang.String form) {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
                    if (target == null) {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
                    }
                    target.setStringValue(form);
                }
            }

            /**
             * Unsets the "form" attribute
             */
            @Override
            public void unsetForm() {
                synchronized (monitor()) {
                    check_orphaned();
                    get_store().remove_attribute(PROPERTY_QNAME[1]);
                }
            }

            /**
             * Gets the "read-only" attribute
             */
            @Override
            public boolean getReadOnly() {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
                    return (target == null) ? false : target.getBooleanValue();
                }
            }

            /**
             * True if has "read-only" attribute
             */
            @Override
            public boolean isSetReadOnly() {
                synchronized (monitor()) {
                    check_orphaned();
                    return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
                }
            }

            /**
             * Sets the "read-only" attribute
             */
            @Override
            public void setReadOnly(boolean readOnly) {
                synchronized (monitor()) {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
                    if (target == null) {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
                    }
                    target.setBooleanValue(readOnly);
                }
            }

            /**
             * Unsets the "read-only" attribute
             */
            @Override
            public void unsetReadOnly() {
                synchronized (monitor()) {
                    check_orphaned();
                    get_store().remove_attribute(PROPERTY_QNAME[2]);
                }
            }
        }
    }
}
