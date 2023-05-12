/*
 * XML Type:  component-metadata
 * Namespace: http://www.knime.org/core/node/workflow/metadata/v1.0
 * Java type: org.knime.core.node.workflow.metadata.v10.ComponentMetadata
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
 * An XML component-metadata(@http://www.knime.org/core/node/workflow/metadata/v1.0).
 *
 * This is a complex type.
 */
public interface ComponentMetadata extends org.knime.core.node.workflow.metadata.v10.NodeContainerMetadata {
    DocumentFactory<org.knime.core.node.workflow.metadata.v10.ComponentMetadata> Factory = new DocumentFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "componentmetadata99b6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "component-type" element
     */
    org.knime.core.node.workflow.metadata.v10.ComponentMetadata.ComponentType.Enum getComponentType();

    /**
     * True if has "component-type" element
     */
    boolean isSetComponentType();

    /**
     * Sets the "component-type" element
     */
    void setComponentType(org.knime.core.node.workflow.metadata.v10.ComponentMetadata.ComponentType.Enum componentType);

    /**
     * Unsets the "component-type" element
     */
    void unsetComponentType();

    /**
     * Gets the "icon" element
     */
    byte[] getIcon();

    /**
     * True if has "icon" element
     */
    boolean isSetIcon();

    /**
     * Sets the "icon" element
     */
    void setIcon(byte[] icon);

    /**
     * Unsets the "icon" element
     */
    void unsetIcon();

    /**
     * Gets the "in-ports" element
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup getInPorts();

    /**
     * Sets the "in-ports" element
     */
    void setInPorts(org.knime.core.node.workflow.metadata.v10.PortGroup inPorts);

    /**
     * Appends and returns a new empty "in-ports" element
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup addNewInPorts();

    /**
     * Gets the "out-ports" element
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup getOutPorts();

    /**
     * Sets the "out-ports" element
     */
    void setOutPorts(org.knime.core.node.workflow.metadata.v10.PortGroup outPorts);

    /**
     * Appends and returns a new empty "out-ports" element
     */
    org.knime.core.node.workflow.metadata.v10.PortGroup addNewOutPorts();

    /**
     * An XML component-type(@http://www.knime.org/core/node/workflow/metadata/v1.0).
     *
     * This is an atomic type that is a restriction of org.knime.core.node.workflow.metadata.v10.ComponentMetadata$ComponentType.
     */
    public interface ComponentType extends org.apache.xmlbeans.XmlString {
        ElementFactory<org.knime.core.node.workflow.metadata.v10.ComponentMetadata.ComponentType> Factory = new ElementFactory<>(org.knime.core.util.metadata.system.metadata.TypeSystemHolder.typeSystem, "componenttypebd50elemtype");
        org.apache.xmlbeans.SchemaType type = Factory.getType();


        org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
        void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

        Enum SOURCE = Enum.forString("Source");
        Enum SINK = Enum.forString("Sink");
        Enum LEARNER = Enum.forString("Learner");
        Enum PREDICTOR = Enum.forString("Predictor");
        Enum MANIPULATOR = Enum.forString("Manipulator");
        Enum VISUALIZER = Enum.forString("Visualizer");
        Enum OTHER = Enum.forString("Other");

        int INT_SOURCE = Enum.INT_SOURCE;
        int INT_SINK = Enum.INT_SINK;
        int INT_LEARNER = Enum.INT_LEARNER;
        int INT_PREDICTOR = Enum.INT_PREDICTOR;
        int INT_MANIPULATOR = Enum.INT_MANIPULATOR;
        int INT_VISUALIZER = Enum.INT_VISUALIZER;
        int INT_OTHER = Enum.INT_OTHER;

        /**
         * Enumeration value class for org.knime.core.node.workflow.metadata.v10.ComponentMetadata$ComponentType.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_SOURCE
         * Enum.forString(s); // returns the enum value for a string
         * Enum.forInt(i); // returns the enum value for an int
         * </pre>
         * Enumeration objects are immutable singleton objects that
         * can be compared using == object equality. They have no
         * public constructor. See the constants defined within this
         * class for all the valid values.
         */
        final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {
            /**
             * Returns the enum value for a string, or null if none.
             */
            public static Enum forString(java.lang.String s) {
                return (Enum)table.forString(s);
            }

            /**
             * Returns the enum value corresponding to an int, or null if none.
             */
            public static Enum forInt(int i) {
                return (Enum)table.forInt(i);
            }

            private Enum(java.lang.String s, int i) {
                super(s, i);
            }

            static final int INT_SOURCE = 1;
            static final int INT_SINK = 2;
            static final int INT_LEARNER = 3;
            static final int INT_PREDICTOR = 4;
            static final int INT_MANIPULATOR = 5;
            static final int INT_VISUALIZER = 6;
            static final int INT_OTHER = 7;

            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
                new Enum("Source", INT_SOURCE),
                new Enum("Sink", INT_SINK),
                new Enum("Learner", INT_LEARNER),
                new Enum("Predictor", INT_PREDICTOR),
                new Enum("Manipulator", INT_MANIPULATOR),
                new Enum("Visualizer", INT_VISUALIZER),
                new Enum("Other", INT_OTHER),
            });
            private static final long serialVersionUID = 1L;
            private java.lang.Object readResolve() {
                return forInt(intValue());
            }
        }
    }
}
