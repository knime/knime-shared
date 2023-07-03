/*
 * An XML document type.
 * Localname: config
 * Namespace: http://www.knime.org/2008/09/XMLConfig
 * Java type: org.knime.x2008.x09.xmlConfig.ConfigDocument
 *
 * Automatically generated - do not modify.
 */
package org.knime.x2008.x09.xmlConfig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one config(@http://www.knime.org/2008/09/XMLConfig) element.
 *
 * This is a complex type.
 */
public class ConfigDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.x2008.x09.xmlConfig.ConfigDocument {
    private static final long serialVersionUID = 1L;

    public ConfigDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/2008/09/XMLConfig", "config"),
    };


    /**
     * Gets the "config" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Config getConfig() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Config target = null;
            target = (org.knime.x2008.x09.xmlConfig.Config)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "config" element
     */
    @Override
    public void setConfig(org.knime.x2008.x09.xmlConfig.Config config) {
        generatedSetterHelperImpl(config, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "config" element
     */
    @Override
    public org.knime.x2008.x09.xmlConfig.Config addNewConfig() {
        synchronized (monitor()) {
            check_orphaned();
            org.knime.x2008.x09.xmlConfig.Config target = null;
            target = (org.knime.x2008.x09.xmlConfig.Config)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
