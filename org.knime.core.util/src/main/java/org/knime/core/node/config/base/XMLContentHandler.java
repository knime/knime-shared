/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * -------------------------------------------------------------------
 *
 * History
 *   Jun 13, 2006 (wiswedel): created
 */
package org.knime.core.node.config.base;

import java.io.IOException;
import java.util.Stack;

import org.knime.core.util.XMLUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Utility class to handle SAX events while parsing the xml file.
 *
 * @author wiswedel, University of Konstanz
 */
class XMLContentHandler extends DefaultHandler {

    private final Stack<ConfigBase> m_elementStack;

    private final String m_fileName;

    private boolean m_isFirst = true;

    /**
     * Creates new instance.
     *
     * @param config The config object as root of the xml tree, this class adds
     *            sub-entrys to this root node.
     * @param fileName The file name for eventual error messages.
     */
    XMLContentHandler(final ConfigBase config, final String fileName) {
        m_elementStack = new Stack<ConfigBase>();
        m_elementStack.push(config);
        m_fileName = fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void characters(final char[] ch, final int start, final int length)
            throws SAXException {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(final String uri, final String localName,
            final String qName) throws SAXException {
        if (ConfigEntries.config.name().equals(qName)) {
            m_elementStack.pop();
        }
        // ignore closing of "entry" tags
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final SAXParseException e) throws SAXException {
        String message = getParseExceptionInfo(e);
        throw new SAXException(message, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(final SAXParseException e) throws SAXException {
        String message = getParseExceptionInfo(e);
        throw new SAXException(message, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(final SAXParseException e) throws SAXException {
        String message = getParseExceptionInfo(e);
        throw new SAXException(message, e);
    }

    /**
     * Returns a string describing parse exception details.
     *
     * @param spe <code>SAXParseException</code>.
     * @return String describing parse exception details.
     */
    private String getParseExceptionInfo(final SAXParseException spe) {
        String systemId = spe.getSystemId();
        if (systemId == null) {
            systemId = "null";
        }
        return "line=" + spe.getLineNumber() + ": " + spe.getMessage() + "\n"
                + "xml: URI=" + m_fileName + "\n" + "dtd: URI=" + systemId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startElement(final String uri, final String localName,
            final String qName, final Attributes attributes)
            throws SAXException {
        ConfigBase peek = m_elementStack.peek();
        if (ConfigEntries.config.name().equals(qName)) {
            if (m_isFirst) {
                m_isFirst = false;
                peek.setKey(attributes.getValue("key"));
            } else {
                // create sub base-config
                ConfigBase subConfig = peek.addConfigBase(
                        attributes.getValue("key"));
                m_elementStack.push(subConfig);
            }
        } else if ("entry".equals(qName)) {
            assert !m_isFirst : "First element in xml is not a config";
            String key = attributes.getValue("key");
            String type = attributes.getValue("type");
            String value = attributes.getValue("value");

            value = XMLUtils.unescape(value);

            ConfigEntries configEntryType;
            // transform runtime IllegalArgumentException into a IOException
            // to force exception handling in caller methods.
            try {
                configEntryType = ConfigEntries.valueOf(type);
            } catch (IllegalArgumentException iae) {
                throw new SAXException("Invalid type ('" + type
                        + "') for key '" + key + "' in XML file.");
            }
            // handle null values and be backward compatible
            boolean isNull = "true".equals(attributes.getValue("isnull"));
            if (isNull) {
                value = null;
            }
            AbstractConfigEntry ab = configEntryType.createEntry(key, value);
            peek.addEntry(ab);
        } else {
            // only "config" and "entry" are valid tag names
            throw new SAXException("\"" + qName + "\" is not a valid tag name.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws IOException, SAXException {
        return XMLConfig.DTD_RESOLVER.resolveEntity(publicId, systemId);
    }

    /**
     * Utility method that writes the given config object to a content handler.
     * The content handler will take care to write to a file.
     *
     * @param c The config to write, must not be <code>null</code>.
     * @param handler To write to.
     * @throws SAXException If that fails.
     */
    static void asXML(final ConfigBase c, final ContentHandler handler)
            throws SAXException {
        handler.startDocument();
        internalAsXML(c, handler, 0);
        handler.endDocument();
    }

    private static void internalAsXML(final ConfigBase c,
            final ContentHandler handler, final int depth) throws SAXException {
        AttributesImpl attr = new AttributesImpl();
        if (depth == 0) {
            attr.addAttribute(null, null, "xmlns", "CDATA",
                    "http://www.knime.org/2008/09/XMLConfig");
            attr.addAttribute(null, null, "xmlns:xsi", "CDATA",
                    "http://www.w3.org/2001/XMLSchema-instance");
            attr.addAttribute(null, null, "xsi:schemaLocation", "CDATA",
                    "http://www.knime.org/2008/09/XMLConfig "
                    + "http://www.knime.org/XMLConfig_2008_09.xsd");
        }

        attr.addAttribute("", "", "key", "CDATA", c.getKey());
        handler.startElement("", "", ConfigEntries.config.name(), attr);
        for (String key : c.keySet()) {
            AbstractConfigEntry e = c.getEntry(key);
            if (e instanceof ConfigBase) {
                internalAsXML((ConfigBase)e, handler, depth + 1);
            } else if (e instanceof ConfigTransientStringEntry) {
                // don't save
            } else {
                AttributesImpl a = new AttributesImpl();
                a.addAttribute("", "", "key", "CDATA", key);
                a.addAttribute("", "", "type", "CDATA", e.getType().name());
                String value = e.toStringValue();

                if (value == null) {
                    a.addAttribute("", "", "isnull", "CDATA", "true");
                    value = "";
                }
                value = XMLUtils.escape(value);
                a.addAttribute("", "", "value", "CDATA", value);
                handler.startElement("", "", "entry", a);
                handler.endElement("", "", "entry");
            }
        }
        handler.endElement("", "", ConfigEntries.config.name());
    }
}
