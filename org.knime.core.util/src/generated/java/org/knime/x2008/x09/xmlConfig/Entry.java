/*
 * XML Type:  entry
 * Namespace: http://www.knime.org/2008/09/XMLConfig
 * Java type: org.knime.x2008.x09.xmlConfig.Entry
 *
 * Automatically generated - do not modify.
 */
package org.knime.x2008.x09.xmlConfig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML entry(@http://www.knime.org/2008/09/XMLConfig).
 *
 * This is a complex type.
 */
public interface Entry extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.knime.x2008.x09.xmlConfig.Entry> Factory = new DocumentFactory<>(org.apache.xmlbeans.metadata.system.s1536B800F8AE853E306D46C1150E19B5.TypeSystemHolder.typeSystem, "entry2baftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "key" attribute
     */
    java.lang.String getKey();

    /**
     * Gets (as xml) the "key" attribute
     */
    org.apache.xmlbeans.XmlString xgetKey();

    /**
     * Sets the "key" attribute
     */
    void setKey(java.lang.String key);

    /**
     * Sets (as xml) the "key" attribute
     */
    void xsetKey(org.apache.xmlbeans.XmlString key);

    /**
     * Gets the "value" attribute
     */
    java.lang.String getValue();

    /**
     * Gets (as xml) the "value" attribute
     */
    org.apache.xmlbeans.XmlString xgetValue();

    /**
     * Sets the "value" attribute
     */
    void setValue(java.lang.String value);

    /**
     * Sets (as xml) the "value" attribute
     */
    void xsetValue(org.apache.xmlbeans.XmlString value);

    /**
     * Gets the "type" attribute
     */
    org.knime.x2008.x09.xmlConfig.EntryType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.knime.x2008.x09.xmlConfig.EntryType xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.knime.x2008.x09.xmlConfig.EntryType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.knime.x2008.x09.xmlConfig.EntryType type);

    /**
     * Gets the "isnull" attribute
     */
    boolean getIsnull();

    /**
     * Gets (as xml) the "isnull" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetIsnull();

    /**
     * True if has "isnull" attribute
     */
    boolean isSetIsnull();

    /**
     * Sets the "isnull" attribute
     */
    void setIsnull(boolean isnull);

    /**
     * Sets (as xml) the "isnull" attribute
     */
    void xsetIsnull(org.apache.xmlbeans.XmlBoolean isnull);

    /**
     * Unsets the "isnull" attribute
     */
    void unsetIsnull();
}
