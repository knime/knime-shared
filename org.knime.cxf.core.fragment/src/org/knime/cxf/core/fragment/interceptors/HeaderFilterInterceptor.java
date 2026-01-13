/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 * History
 *   Dec 12, 2023 (Leon Wenzler, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.cxf.core.fragment.interceptors;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.URLConnectionHTTPConduit;

import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Filters client headers before sending the REST request.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
public class HeaderFilterInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final List<HeaderFilterCondition> FILTERS = List.of( //
        /*
         * When using the URLConnectionHTTPConduit to make an HTTPS connection, the connection is established
         * via the HTTP CONNECT method (i.e. HTTP tunneling). For this, the CXF library with its
         * org.apache.cxf.transport.http.CXFAuthenticator provides preemptive authentication and the
         * authenticated tunnel can be established. For the "main request", the HTTPS then no longer needs to
         * send the "Proxy-Authorization" header. Doing that anyway results in this header being treated as
         * regular data and being sent down the tunnel, unencrypted directly to the final foreign host. This
         * interceptor prevents this behavior by removing the unnecessary header preemptively. See AP-21902.
         */
        new HeaderFilterCondition("Proxy-Authorization", //
            m -> isUsingURLConnection(m) && "https".equals(m.get("http.scheme"))) //
    );

    /**
     * Default constructor.
     */
    public HeaderFilterInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    /**
     * Checks whether the given {@link Message} will be sent using a {@link URLConnection}. In Apache CXF,
     * this corresponds to using the {@link URLConnectionHTTPConduit} which is a configuration object
     * that is marshalled into a REST request.
     * <p>
     * Selecting the {@link URLConnectionHTTPConduit} implementation is either done by force using a system
     * property (see AP-21605) or when other implementations do not offer the configured functionality
     * (e.g. using a custom {@link SSLSocketFactory} or {@link TrustManager}s). In the latter case,
     * the message is labeled with {@code "USING_URLCONNECTION"}.
     * </p>
     *
     * @param message the message to check
     * @return {@code true} if the {@link URLConnection}-based implementation is used, otherwise {@link false}
     */
    static boolean isUsingURLConnection(final Message message) {
        return Boolean.TRUE.equals(message.getContextualProperty("force.urlconnection.http.conduit")) //
            || Boolean.TRUE.equals(message.get("USING_URLCONNECTION"));
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        FILTERS.forEach(condition -> condition.accept(message));
    }

    /**
     * Represents a header filter which is only applied on a specific condition.
     *
     * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
     */
    private static final class HeaderFilterCondition implements Consumer<Message> {
        private final String m_header;

        private final Predicate<Message> m_condition;

        private HeaderFilterCondition(final String h, final Predicate<Message> c) {
            this.m_header = h;
            this.m_condition = c;
        }

        @Override
        public void accept(final Message message) {
            if (this.m_condition.test(message)) {
                @SuppressWarnings("unchecked")
                MultivaluedMap<String, String> headers =
                    (MultivaluedMap<String, String>)message.get(Message.PROTOCOL_HEADERS);
                if (headers != null) {
                    headers.remove(this.m_header);
                }
            }
        }
    }
}
