/*
 * XML Type:  component-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.ComponentMetadata
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;

/**
 * An XML component-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0).
 *
 * This is a complex type.
 */
public class ComponentMetadataImpl extends org.knime.core.node.workflow.metadata.v10.impl.NodeContainerMetadataImpl implements org.knime.core.node.workflow.metadata.v10.ComponentMetadata {
    private static final long serialVersionUID = 1L;

    public ComponentMetadataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "component-type"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "icon"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "in-ports"),
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "out-ports"),
    };


    /**
     * Gets the "component-type" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.ComponentMetadata.ComponentType.Enum getComponentType() {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                return null;
            }
            return (org.knime.core.node.workflow.metadata.v10.ComponentMetadata.ComponentType.Enum)target.getEnumValue();
        }
    }

    /**
     * True if has "component-type" element
     */
    @Override
    public boolean isSetComponentType() {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "component-type" element
     */
    @Override
    public void setComponentType(org.knime.core.node.workflow.metadata.v10.ComponentMetadata.ComponentType.Enum componentType) {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(componentType);
        }
    }

    /**
     * Unsets the "component-type" element
     */
    @Override
    public void unsetComponentType() {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "icon" element
     */
    @Override
    public byte[] getIcon() {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                return null;
            }
            return target.getByteArrayValue();
        }
    }

    /**
     * True if has "icon" element
     */
    @Override
    public boolean isSetIcon() {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "icon" element
     */
    @Override
    public void setIcon(byte[] icon) {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(icon);
        }
    }

    /**
     * Unsets the "icon" element
     */
    @Override
    public void unsetIcon() {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "in-ports" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup getInPorts() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.PortGroup target = null;
            target = (org.knime.core.node.workflow.metadata.v10.PortGroup)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "in-ports" element
     */
    @Override
    public void setInPorts(org.knime.core.node.workflow.metadata.v10.PortGroup inPorts) {
        generatedSetterHelperImpl(inPorts, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "in-ports" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup addNewInPorts() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.PortGroup target = null;
            target = (org.knime.core.node.workflow.metadata.v10.PortGroup)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "out-ports" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup getOutPorts() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.PortGroup target = null;
            target = (org.knime.core.node.workflow.metadata.v10.PortGroup)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "out-ports" element
     */
    @Override
    public void setOutPorts(org.knime.core.node.workflow.metadata.v10.PortGroup outPorts) {
        generatedSetterHelperImpl(outPorts, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "out-ports" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.PortGroup addNewOutPorts() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.PortGroup target = null;
            target = (org.knime.core.node.workflow.metadata.v10.PortGroup)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }
    /**
     * An XML component-type(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.ComponentMetadata$ComponentType.
     */
    public static class ComponentTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.knime.core.node.workflow.metadata.v10.ComponentMetadata.ComponentType {
        private static final long serialVersionUID = 1L;

        public ComponentTypeImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType, false);
        }

        protected ComponentTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
