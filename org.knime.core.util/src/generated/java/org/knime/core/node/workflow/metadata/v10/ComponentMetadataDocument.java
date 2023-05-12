/*
 * An XML document type.
 * Localname: component-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.ComponentMetadataDocument
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
 * @since 5.24
 *
 * A document containing one component-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0) element.
 *
 * This is a complex type.
 */
public interface ComponentMetadataDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.knime.core.node.workflow.metadata.v10.ComponentMetadataDocument> Factory = new DocumentFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "componentmetadata07c6doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * @since 5.24
     */
    org.knime.core.node.workflow.metadata.v10.ComponentMetadata getComponentMetadata();

    /**
     * @since 5.24
     */
    void setComponentMetadata(org.knime.core.node.workflow.metadata.v10.ComponentMetadata componentMetadata);

    /**
     * Appends and returns a new empty "component-metadata" element
     */
    org.knime.core.node.workflow.metadata.v10.ComponentMetadata addNewComponentMetadata();
}
