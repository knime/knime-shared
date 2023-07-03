/*
 * An XML document type.
 * Localname: component-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.ComponentMetadataDocument
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;

/**
 * A document containing one component-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0) element.
 *
 * This is a complex type.
 */
public class ComponentMetadataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.core.node.workflow.metadata.v10.ComponentMetadataDocument {
    private static final long serialVersionUID = 1L;

    public ComponentMetadataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "component-metadata"),
    };


    /**
     * Gets the "component-metadata" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.ComponentMetadata getComponentMetadata() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.ComponentMetadata target = null;
            target = (org.knime.core.node.workflow.metadata.v10.ComponentMetadata)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "component-metadata" element
     */
    @Override
    public void setComponentMetadata(org.knime.core.node.workflow.metadata.v10.ComponentMetadata componentMetadata) {
        generatedSetterHelperImpl(componentMetadata, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "component-metadata" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.ComponentMetadata addNewComponentMetadata() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.ComponentMetadata target = null;
            target = (org.knime.core.node.workflow.metadata.v10.ComponentMetadata)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
