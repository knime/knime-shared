/*
 * XML Type:  port-group
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.PortGroup
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;

/**
 * An XML port-group(@http://www.knime.org/core/node/workflow/metadata/v1.0).
 *
 * This is a complex type.
 */
public class PortGroupImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.core.node.workflow.metadata.v10.PortGroup {
    private static final long serialVersionUID = 1L;

    public PortGroupImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "port"),
    };


    /**
     * Gets a List of "port" elements
     */
    @Override
    public java.util.List<org.knime.core.node.workflow.metadata.v10.PortGroup.Port> getPortList() {
        synchronized (monitor())
        {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPortArray,
                this::setPortArray,
                this::insertNewPort,
                this::removePort,
                this::sizeOfPortArray
            );
        }
    }

    /**
     * Gets array of all "port" elements
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup.Port[] getPortArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.knime.core.node.workflow.metadata.v10.PortGroup.Port[0]);
    }

    /**
     * Gets ith "port" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup.Port getPortArray(int i) {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.PortGroup.Port target = null;
            target = (org.knime.core.node.workflow.metadata.v10.PortGroup.Port)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "port" element
     */
    @Override
    public int sizeOfPortArray() {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "port" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPortArray(org.knime.core.node.workflow.metadata.v10.PortGroup.Port[] portArray) {
        check_orphaned();
        arraySetterHelper(portArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "port" element
     */
    @Override
    public void setPortArray(int i, org.knime.core.node.workflow.metadata.v10.PortGroup.Port port) {
        generatedSetterHelperImpl(port, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "port" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup.Port insertNewPort(int i) {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.PortGroup.Port target = null;
            target = (org.knime.core.node.workflow.metadata.v10.PortGroup.Port)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "port" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup.Port addNewPort() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.PortGroup.Port target = null;
            target = (org.knime.core.node.workflow.metadata.v10.PortGroup.Port)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "port" element
     */
    @Override
    public void removePort(int i) {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
    /**
     * An XML port(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.PortGroup$Port.
     */
    public static class PortImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.knime.core.node.workflow.metadata.v10.PortGroup.Port {
        private static final long serialVersionUID = 1L;

        public PortImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType, true);
        }

        protected PortImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
            super(sType, b);
        }

        private static final QName[] PROPERTY_QNAME = {
            new QName("", "name"),
        };


        /**
         * Gets the "name" attribute
         */
        @Override
        public java.lang.String getName() {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        /**
         * Sets the "name" attribute
         */
        @Override
        public void setName(java.lang.String name) {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
                }
                target.setStringValue(name);
            }
        }
    }
}
