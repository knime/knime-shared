/*
 * XML Type:  config
 * Namespace: http://www.knime.org/2008/09/XMLConfig
 * Java type: org.knime.x2008.x09.xmlConfig.Config
 *
 * Automatically generated - do not modify.
 */
package org.knime.x2008.x09.xmlConfig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML config(@http://www.knime.org/2008/09/XMLConfig).
 *
 * This is a complex type.
 */
public interface Config extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.knime.x2008.x09.xmlConfig.Config> Factory = new DocumentFactory<>(org.apache.xmlbeans.metadata.system.s1536B800F8AE853E306D46C1150E19B5.TypeSystemHolder.typeSystem, "configa22dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "entry" elements
     */
    java.util.List<org.knime.x2008.x09.xmlConfig.Entry> getEntryList();

    /**
     * Gets array of all "entry" elements
     */
    org.knime.x2008.x09.xmlConfig.Entry[] getEntryArray();

    /**
     * Gets ith "entry" element
     */
    org.knime.x2008.x09.xmlConfig.Entry getEntryArray(int i);

    /**
     * Returns number of "entry" element
     */
    int sizeOfEntryArray();

    /**
     * Sets array of all "entry" element
     */
    void setEntryArray(org.knime.x2008.x09.xmlConfig.Entry[] entryArray);

    /**
     * Sets ith "entry" element
     */
    void setEntryArray(int i, org.knime.x2008.x09.xmlConfig.Entry entry);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "entry" element
     */
    org.knime.x2008.x09.xmlConfig.Entry insertNewEntry(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "entry" element
     */
    org.knime.x2008.x09.xmlConfig.Entry addNewEntry();

    /**
     * Removes the ith "entry" element
     */
    void removeEntry(int i);

    /**
     * Gets a List of "config" elements
     */
    java.util.List<org.knime.x2008.x09.xmlConfig.Config> getConfigList();

    /**
     * Gets array of all "config" elements
     */
    org.knime.x2008.x09.xmlConfig.Config[] getConfigArray();

    /**
     * Gets ith "config" element
     */
    org.knime.x2008.x09.xmlConfig.Config getConfigArray(int i);

    /**
     * Returns number of "config" element
     */
    int sizeOfConfigArray();

    /**
     * Sets array of all "config" element
     */
    void setConfigArray(org.knime.x2008.x09.xmlConfig.Config[] configArray);

    /**
     * Sets ith "config" element
     */
    void setConfigArray(int i, org.knime.x2008.x09.xmlConfig.Config config);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "config" element
     */
    org.knime.x2008.x09.xmlConfig.Config insertNewConfig(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "config" element
     */
    org.knime.x2008.x09.xmlConfig.Config addNewConfig();

    /**
     * Removes the ith "config" element
     */
    void removeConfig(int i);

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
}
