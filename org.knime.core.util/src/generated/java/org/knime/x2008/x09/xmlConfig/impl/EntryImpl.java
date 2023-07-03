/*
 * XML Type:  entry
 * Namespace: http://www.knime.org/2008/09/XMLConfig
 * Java type: org.knime.x2008.x09.xmlConfig.Entry
 *
 * Automatically generated - do not modify.
 */
package org.knime.x2008.x09.xmlConfig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML entry(@http://www.knime.org/2008/09/XMLConfig).
 *
 * This is a complex type.
 */
public class EntryImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.x2008.x09.xmlConfig.Entry {
    private static final long serialVersionUID = 1L;

    public EntryImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "key"),
        new QName("", "value"),
        new QName("", "type"),
        new QName("", "isnull"),
    };


    /**
     * Gets the "key" attribute
     */
    @Override
    public java.lang.String getKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(key);
        }
    }

    /**
     * Gets the "value" attribute
     */
    @Override
    public java.lang.String getValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "value" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "value" attribute
     */
    @Override
    public void setValue(java.lang.String value) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(value);
        }
    }

    /**
     * Sets (as xml) the "value" attribute
     */
    @Override
    public void xsetValue(org.apache.xmlbeans.XmlString value) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(value);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.EntryType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.knime.x2008.x09.xmlConfig.EntryType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.EntryType xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.EntryType target = null;
            target = (org.knime.x2008.x09.xmlConfig.EntryType)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.knime.x2008.x09.xmlConfig.EntryType.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.knime.x2008.x09.xmlConfig.EntryType type) {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.EntryType target = null;
            target = (org.knime.x2008.x09.xmlConfig.EntryType)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.knime.x2008.x09.xmlConfig.EntryType)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(type);
        }
    }

    /**
     * Gets the "isnull" attribute
     */
    @Override
    public boolean getIsnull() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "isnull" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetIsnull() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "isnull" attribute
     */
    @Override
    public boolean isSetIsnull() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "isnull" attribute
     */
    @Override
    public void setIsnull(boolean isnull) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(isnull);
        }
    }

    /**
     * Sets (as xml) the "isnull" attribute
     */
    @Override
    public void xsetIsnull(org.apache.xmlbeans.XmlBoolean isnull) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(isnull);
        }
    }

    /**
     * Unsets the "isnull" attribute
     */
    @Override
    public void unsetIsnull() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
