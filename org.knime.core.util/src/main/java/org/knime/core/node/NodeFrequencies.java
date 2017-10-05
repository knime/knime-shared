/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
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
 * ---------------------------------------------------------------------
 *
 * History
 *   18.03.2016 (thor): created
 */
package org.knime.core.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.knime.core.util.Version;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * This class stores a complete list of node triples and their frequencies.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @since 5.4
 */
public class NodeFrequencies {
    private final String m_name;

    private final Version m_version;

    private final List<NodeTriple> m_frequencies;

    /**
     * Creates a new node frequencies object.
     *
     * @param name a name for this object, must not be <code>null</code>
     * @param triples a (possibly empty) collection of node triples
     */
    public NodeFrequencies(@JsonProperty("name") final String name,
        @JsonProperty("frequencies") final Collection<NodeTriple> triples) {
        m_name = name;
        m_version = new Version(3, 2, 0);
        m_frequencies = Collections.unmodifiableList(new ArrayList<>(triples));
    }

    @JsonCreator
    private NodeFrequencies(@JsonProperty("name") final String name, @JsonProperty("version") final Version version,
        @JsonProperty("frequencies") final List<NodeTriple> triples) {
        m_name = name;
        m_version = version;
        m_frequencies = Collections.unmodifiableList(triples);
    }

    /**
     * Returns the custom name of this object.
     *
     * @return the name, never <code>null</code>
     */
    @JsonProperty("name")
    public String getName() {
        return m_name;
    }

    /**
     * Returns the version of this node frequencies object.
     *
     * @return the version, never <code>null</code>
     */
    @JsonProperty("version")
    public Version getVersion() {
        return m_version;
    }

    /**
     * Returns a (unmodifieable) list of node triples.
     *
     * @return a (possibly empty) list of triples; never <code>null</code>
     */
    @JsonProperty("frequencies")
    public List<NodeTriple> getFrequencies() {
        return m_frequencies;
    }

    /**
     * Writes this instance in JSON format to the output stream.
     *
     * @param out the destination stream, must not be <code>null</code>
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void write(final OutputStream out) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(Include.NON_NULL);

        mapper.writeValue(out, this);
    }

    /**
     * Creates a new object by reading JSON data from the given input stream. The format must be the same as written by
     * {@link #write(OutputStream)}.
     *
     * @param in an input stream
     * @return a new node frequencies object
     * @throws IOException if an I/O error occurs while reading from the stream
     */
    public static NodeFrequencies from(final InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        return mapper.readValue(in, NodeFrequencies.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_frequencies == null) ? 0 : m_frequencies.hashCode());
        result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
        result = prime * result + ((m_version == null) ? 0 : m_version.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NodeFrequencies other = (NodeFrequencies)obj;
        if (!Objects.equals(m_name, other.m_name) || !Objects.equals(m_version, other.m_version)) {
            return false;
        }
        if (m_frequencies.size() != other.m_frequencies.size()) {
            return false;
        }

        Iterator<NodeTriple> it1 = m_frequencies.iterator();
        Iterator<NodeTriple> it2 = other.m_frequencies.iterator();

        while (it1.hasNext()) {
            NodeTriple nt1 = it1.next();
            NodeTriple nt2 = it2.next();

            if (!Objects.equals(nt1, nt2) || (nt1.getCount() != nt2.getCount())) {
                return false;
            }
        }

        return true;
    }
}
