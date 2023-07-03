/*
 * XML Type:  config
 * Namespace: http://www.knime.org/2008/09/XMLConfig
 * Java type: org.knime.x2008.x09.xmlConfig.Config
 *
 * Automatically generated - do not modify.
 */
package org.knime.x2008.x09.xmlConfig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML config(@http://www.knime.org/2008/09/XMLConfig).
 *
 * This is a complex type.
 */
public class ConfigImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.x2008.x09.xmlConfig.Config {
    private static final long serialVersionUID = 1L;

    public ConfigImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/2008/09/XMLConfig", "entry"),
        new QName("http://www.knime.org/2008/09/XMLConfig", "config"),
        new QName("", "key"),
    };


    /**
     * Gets a List of "entry" elements
     */
    @Override
    public java.util.List<org.knime.x2008.x09.xmlConfig.Entry> getEntryList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEntryArray,
                this::setEntryArray,
                this::insertNewEntry,
                this::removeEntry,
                this::sizeOfEntryArray
            );
        }
    }

    /**
     * Gets array of all "entry" elements
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Entry[] getEntryArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.knime.x2008.x09.xmlConfig.Entry[0]);
    }

    /**
     * Gets ith "entry" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Entry getEntryArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Entry target = null;
            target = (org.knime.x2008.x09.xmlConfig.Entry)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "entry" element
     */
    @Override
    public int sizeOfEntryArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "entry" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEntryArray(org.knime.x2008.x09.xmlConfig.Entry[] entryArray) {
        check_orphaned();
        arraySetterHelper(entryArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "entry" element
     */
    @Override
    public void setEntryArray(int i, org.knime.x2008.x09.xmlConfig.Entry entry) {
        generatedSetterHelperImpl(entry, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "entry" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Entry insertNewEntry(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Entry target = null;
            target = (org.knime.x2008.x09.xmlConfig.Entry)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "entry" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Entry addNewEntry() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Entry target = null;
            target = (org.knime.x2008.x09.xmlConfig.Entry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "entry" element
     */
    @Override
    public void removeEntry(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "config" elements
     */
    @Override
    public java.util.List<org.knime.x2008.x09.xmlConfig.Config> getConfigList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getConfigArray,
                this::setConfigArray,
                this::insertNewConfig,
                this::removeConfig,
                this::sizeOfConfigArray
            );
        }
    }

    /**
     * Gets array of all "config" elements
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Config[] getConfigArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.knime.x2008.x09.xmlConfig.Config[0]);
    }

    /**
     * Gets ith "config" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Config getConfigArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Config target = null;
            target = (org.knime.x2008.x09.xmlConfig.Config)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "config" element
     */
    @Override
    public int sizeOfConfigArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "config" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setConfigArray(org.knime.x2008.x09.xmlConfig.Config[] configArray) {
        check_orphaned();
        arraySetterHelper(configArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "config" element
     */
    @Override
    public void setConfigArray(int i, org.knime.x2008.x09.xmlConfig.Config config) {
        generatedSetterHelperImpl(config, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "config" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Config insertNewConfig(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Config target = null;
            target = (org.knime.x2008.x09.xmlConfig.Config)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "config" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Config addNewConfig() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Config target = null;
            target = (org.knime.x2008.x09.xmlConfig.Config)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "config" element
     */
    @Override
    public void removeConfig(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "key" attribute
     */
    @Override
    public java.lang.String getKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "key" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "key" attribute
     */
    @Override
    public void setKey(java.lang.String key) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(key);
        }
    }

    /**
     * Sets (as xml) the "key" attribute
     */
    @Override
    public void xsetKey(org.apache.xmlbeans.XmlString key) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(key);
        }
    }
}
