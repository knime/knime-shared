/*
 * An XML document type.
 * Localname: config
 * Namespace: http://www.knime.org/2008/09/XMLConfig
 * Java type: org.knime.x2008.x09.xmlConfig.ConfigDocument
 *
 * Automatically generated - do not modify.
 */
package org.knime.x2008.x09.xmlConfig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one config(@http://www.knime.org/2008/09/XMLConfig) element.
 *
 * This is a complex type.
 */
public interface ConfigDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.knime.x2008.x09.xmlConfig.ConfigDocument> Factory = new DocumentFactory<>(org.apache.xmlbeans.metadata.system.s1536B800F8AE853E306D46C1150E19B5.TypeSystemHolder.typeSystem, "config3e1ddoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "config" element
     */
    org.knime.x2008.x09.xmlConfig.Config getConfig();

    /**
     * Sets the "config" element
     */
    void setConfig(org.knime.x2008.x09.xmlConfig.Config config);

    /**
     * Appends and returns a new empty "config" element
     */
    org.knime.x2008.x09.xmlConfig.Config addNewConfig();
}
