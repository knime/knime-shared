/*
 * XML Type:  port-group
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.PortGroup
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML port-group(@http://www.knime.org/core/node/workflow/metadata/v1.0).
 *
 * This is a complex type.
 */
public interface PortGroup extends org.apache.xmlbeans.XmlObject
{
    DocumentFactory<org.knime.core.node.workflow.metadata.v10.PortGroup> Factory = new DocumentFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "portgroupb3eatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "port" elements
     */
    java.util.List<org.knime.core.node.workflow.metadata.v10.PortGroup.Port> getPortList();

    /**
     * Gets array of all "port" elements
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup.Port[] getPortArray();

    /**
     * Gets ith "port" element
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup.Port getPortArray(int i);

    /**
     * Returns number of "port" element
     */
    int sizeOfPortArray();

    /**
     * Sets array of all "port" element
     */
    void setPortArray(org.knime.core.node.workflow.metadata.v10.PortGroup.Port[] portArray);

    /**
     * Sets ith "port" element
     */
    void setPortArray(int i, org.knime.core.node.workflow.metadata.v10.PortGroup.Port port);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "port" element
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup.Port insertNewPort(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "port" element
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup.Port addNewPort();

    /**
     * Removes the ith "port" element
     */
    void removePort(int i);

    /**
     * An XML port(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.PortGroup$Port.
     */
    public interface Port extends org.apache.xmlbeans.XmlString
    {
        ElementFactory<org.knime.core.node.workflow.metadata.v10.PortGroup.Port> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "port95adelemtype");
        org.apache.xmlbeans.SchemaType type = Factory.getType();


        /**
         * Gets the "name" attribute
         */
        java.lang.String getName();

        /**
         * Sets the "name" attribute
         */
        void setName(java.lang.String name);
    }
}
