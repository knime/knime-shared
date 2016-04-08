/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
 */
package org.knime.core.node.config.base;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A class used to load and save Config objects into an XML file.
 * <p>
 * This implementation uses a SAX Parser to create and save the xml files. This
 * got necessary since predictive params may get big and using a DOM parser
 * keeps the entire xml-tree in memory.
 *
 * @author Bernd Wiswedel, University of Konstanz
 */
public final class XMLConfig {
    private static final SAXParserFactory parserFactory;

    private static final SAXTransformerFactory transformerFactory;

    static {
        try {
            transformerFactory =
                    (SAXTransformerFactory)TransformerFactory.newInstance();
            parserFactory = SAXParserFactory.newInstance();
            parserFactory.setValidating(false);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /** dtd name from class name. */
    public static final String DTD_NAME =
            XMLConfig.class.getName().replace('.', '/') + ".dtd";

    /**
     * Entity resolver for the XMLConfig.dtd file used in some old config files.
     */
    public static final EntityResolver DTD_RESOLVER = new EntityResolver() {
        @Override
        public InputSource resolveEntity(final String publicId, final String systemId)
            throws SAXException, IOException {
            // XMLConfig.dtd was moved some time ago but old workflows still reference it
            if ((systemId != null) && (systemId.endsWith(XMLConfig.DTD_NAME)
                || systemId.replaceAll("(org/knime/core/node/config)/(?!base/)", "$1/base/")
                    .endsWith(XMLConfig.DTD_NAME)
                || systemId.replace("de/unikn/knime/core/node/config/", "org/knime/core/node/config/base/")
                    .endsWith(XMLConfig.DTD_NAME))) {
                // gets URL for systemId which specifies the dtd file+path
                ClassLoader classLoader = XMLConfig.class.getClassLoader();
                URL dtdURL = classLoader.getResource(XMLConfig.DTD_NAME);
                InputStream is = dtdURL.openStream();
                return new InputSource(is);
            } else {
                return null;
            }
        }
    };

    private XMLConfig() {
        // empty
    }

    /**
     * Reads from the given input stream into the given config object.
     *
     * @param c Where to put the results.
     * @param in Where to read from, stream will be closed when done.
     * @throws SAXException If stream can't be properly parsed.
     * @throws IOException If IO problem occur.
     * @throws ParserConfigurationException If not properly configured.
     * @throws NullPointerException If any argument is <code>null</code>.
     */
    public static void load(final ConfigBase c, final InputStream in)
            throws SAXException, IOException, ParserConfigurationException {
        SAXParser saxParser = parserFactory.newSAXParser();
        XMLReader reader = saxParser.getXMLReader();
        XMLContentHandler xmlContentHandler =
                new XMLContentHandler(c, in.toString());
        reader.setContentHandler(xmlContentHandler);
        reader.setEntityResolver(xmlContentHandler);
        reader.setErrorHandler(xmlContentHandler);

        BufferedReader buf = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));
        try {
            reader.parse(new InputSource(buf));
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Unable to parse xml: " + e.getMessage(), e);
        }
    }

    /**
     * Saves given Config into an XML stream. <em>Note that the stream is closed at the end by this method!</em>
     *
     * @param config the Config the save
     * @param output the stream to write Config as XML to. The stream does not need to be buffered.
     * @throws IOException if the Config could not be stored
     */
    public static void save(final ConfigBase config, final OutputStream output)
            throws IOException {
        TransformerHandler tfh = null;
        try {
            tfh = transformerFactory.newTransformerHandler();
        } catch (TransformerConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        Transformer t = tfh.getTransformer();

        final String encoding = "UTF-8";
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.ENCODING, encoding);

        // we write the XML header by hand because we need a linebreak after it for KNIME <= 2.6
        output.write(("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n").getBytes(Charset.forName(encoding)));

        final boolean originalOutputIsBuffered =
                ((output instanceof BufferedOutputStream) || (output instanceof ByteArrayOutputStream));
        OutputStream os = originalOutputIsBuffered ? output : new BufferedOutputStream(output);
        tfh.setResult(new StreamResult(os));

        try {
            XMLContentHandler.asXML(config, tfh);
        } catch (SAXException se) {
            throw new IOException("Saving xml to " + output.toString() + " failed: " + se.getMessage(), se);
        } finally {
            // Note: When using the GZIP stream, it is also required by the
            // ZLIB native library in order to support certain optimizations
            // to flush the stream.
            try {
                os.flush();
            } finally {
                os.close();
            }
        }
    }
}
