/*
 * An XML document type.
 * Localname: workflow-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.WorkflowMetadataDocument
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;

/**
 * A document containing one workflow-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0) element.
 *
 * This is a complex type.
 */
public class WorkflowMetadataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.knime.core.node.workflow.metadata.v10.WorkflowMetadataDocument {
    private static final long serialVersionUID = 1L;

    public WorkflowMetadataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.knime.org/core/node/workflow/metadata/v1.0", "workflow-metadata"),
    };


    /**
     * Gets the "workflow-metadata" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.WorkflowMetadata getWorkflowMetadata() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.WorkflowMetadata target = null;
            target = (org.knime.core.node.workflow.metadata.v10.WorkflowMetadata)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "workflow-metadata" element
     */
    @Override
    public void setWorkflowMetadata(org.knime.core.node.workflow.metadata.v10.WorkflowMetadata workflowMetadata) {
        generatedSetterHelperImpl(workflowMetadata, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "workflow-metadata" element
     */
    @Override
    public org.knime.core.node.workflow.metadata.v10.WorkflowMetadata addNewWorkflowMetadata() {
        synchronized (monitor())
        {
            check_orphaned();
            org.knime.core.node.workflow.metadata.v10.WorkflowMetadata target = null;
            target = (org.knime.core.node.workflow.metadata.v10.WorkflowMetadata)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
