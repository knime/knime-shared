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
 *   Dec 3, 2025 (lw): created
 */
package org.knime.cxf.core.fragment.interceptors;

import static org.knime.cxf.core.fragment.interceptors.HeaderFilterInterceptor.isUsingURLConnection;

import java.io.IOException;
import java.util.Set;

import org.apache.cxf.BusFactory;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.Headers;

import jakarta.ws.rs.core.HttpHeaders;

/**
 * Forcefully sets the {@value Headers#EMPTY_REQUEST_PROPERTY} property in CXF requests
 * to {@code false}, such that the sending phase always performs body handling
 * (that property turns it off in CXF - probably for efficiency reasons).
 * <p>
 * If the body handling is active, the {@link HttpHeaders#CONTENT_LENGTH} is always
 * sent with the request, even if it's zero. While this is technically not required by
 * the HTTP standard, some load balancers require that header to *always* be set.
 * </p>
 * In order to stay compatible with these load balancers, we expose the Java system
 * property {@value #IS_ENABLED_KEY} to enable this non-standard behavior.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
public class EmptyRequestInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final String IS_ENABLED_KEY = "knime.http.body.forceContentLength";

    private static final boolean IS_ENABLED_VALUE = Boolean.getBoolean(IS_ENABLED_KEY);

    private static final Set<String> HTTP_VERBS_WITHOUT_BODY = DummyConduit.getVerbsWithoutContent();

    /**
     * Default constructor.
     */
    public EmptyRequestInterceptor() {
        // interceptor phase before the body handling
        super(Phase.PREPARE_SEND);
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        if (!IS_ENABLED_VALUE) {
            return;
        }
        final var verb = (String)message.get(Message.HTTP_REQUEST_METHOD);
        if (isUsingURLConnection(message) /* (1) using the `URLConnectionHTTPConduit` */
            && !HTTP_VERBS_WITHOUT_BODY.contains(verb) /* (2) should contain a request body */
            && Boolean.TRUE.equals(message.get(Headers.EMPTY_REQUEST_PROPERTY)) /* (3) but does not */
        ) {
            message.put(Headers.EMPTY_REQUEST_PROPERTY, Boolean.FALSE);
        }
    }

    // -- HELPER CLASSES --

    /** Non-functional dummy subclass to access the protected static field
     * {@link HTTPConduit#KNOWN_HTTP_VERBS_WITH_NO_CONTENT}. */
    private abstract static class DummyConduit extends HTTPConduit {
        private DummyConduit() throws IOException {
            super(BusFactory.getThreadDefaultBus(), new EndpointInfo());
            throw new IllegalStateException("Do not instantiate this class: " + getClass().getName());
        }

        private static Set<String> getVerbsWithoutContent() {
            return KNOWN_HTTP_VERBS_WITH_NO_CONTENT;
        }
    }

}
