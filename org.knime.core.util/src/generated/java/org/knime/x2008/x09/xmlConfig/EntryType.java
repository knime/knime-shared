/*
 * XML Type:  entry-type
 * Namespace: http://www.knime.org/2008/09/XMLConfig
 * Java type: org.knime.x2008.x09.xmlConfig.EntryType
 *
 * Automatically generated - do not modify.
 */
package org.knime.x2008.x09.xmlConfig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML entry-type(@http://www.knime.org/2008/09/XMLConfig).
 *
 * This is an atomic type that is a restriction of org.knime.x2008.x09.xmlConfig.EntryType.
 */
public interface EntryType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.knime.x2008.x09.xmlConfig.EntryType> Factory = new SimpleTypeFactory<>(org.apache.xmlbeans.metadata.system.s1536B800F8AE853E306D46C1150E19B5.TypeSystemHolder.typeSystem, "entrytypec6datype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum XSTRING = Enum.forString("xstring");
    Enum XINT = Enum.forString("xint");
    Enum XDOUBLE = Enum.forString("xdouble");
    Enum XFLOAT = Enum.forString("xfloat");
    Enum XBOOLEAN = Enum.forString("xboolean");
    Enum XBYTE = Enum.forString("xbyte");
    Enum XCHAR = Enum.forString("xchar");
    Enum XLONG = Enum.forString("xlong");
    Enum XSHORT = Enum.forString("xshort");

    int INT_XSTRING = Enum.INT_XSTRING;
    int INT_XINT = Enum.INT_XINT;
    int INT_XDOUBLE = Enum.INT_XDOUBLE;
    int INT_XFLOAT = Enum.INT_XFLOAT;
    int INT_XBOOLEAN = Enum.INT_XBOOLEAN;
    int INT_XBYTE = Enum.INT_XBYTE;
    int INT_XCHAR = Enum.INT_XCHAR;
    int INT_XLONG = Enum.INT_XLONG;
    int INT_XSHORT = Enum.INT_XSHORT;

    /**
     * Enumeration value class for org.knime.x2008.x09.xmlConfig.EntryType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_XSTRING
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

        static final int INT_XSTRING = 1;
        static final int INT_XINT = 2;
        static final int INT_XDOUBLE = 3;
        static final int INT_XFLOAT = 4;
        static final int INT_XBOOLEAN = 5;
        static final int INT_XBYTE = 6;
        static final int INT_XCHAR = 7;
        static final int INT_XLONG = 8;
        static final int INT_XSHORT = 9;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("xstring", INT_XSTRING),
            new Enum("xint", INT_XINT),
            new Enum("xdouble", INT_XDOUBLE),
            new Enum("xfloat", INT_XFLOAT),
            new Enum("xboolean", INT_XBOOLEAN),
            new Enum("xbyte", INT_XBYTE),
            new Enum("xchar", INT_XCHAR),
            new Enum("xlong", INT_XLONG),
            new Enum("xshort", INT_XSHORT),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
