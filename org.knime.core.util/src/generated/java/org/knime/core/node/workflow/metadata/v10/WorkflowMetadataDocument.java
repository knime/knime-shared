/*
 * An XML document type.
 * Localname: workflow-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.WorkflowMetadataDocument
 *
 * Automatically generated - do not modify.
 */
package org.knime.core.node.workflow.metadata.v10;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 *
 * @since 6.0
 *
 * A document containing one workflow-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0) element.
 *
 * This is a complex type.
 */
public interface WorkflowMetadataDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.knime.core.node.workflow.metadata.v10.WorkflowMetadataDocument> Factory = new DocumentFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "workflowmetadata3ab2doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * @since 6.0
     */
    org.knime.core.node.workflow.metadata.v10.WorkflowMetadata getWorkflowMetadata();

    /**
     * @since 6.0
     */
    void setWorkflowMetadata(org.knime.core.node.workflow.metadata.v10.WorkflowMetadata workflowMetadata);

    /**
     * Appends and returns a new empty "workflow-metadata" element
     */
    org.knime.core.node.workflow.metadata.v10.WorkflowMetadata addNewWorkflowMetadata();
}
