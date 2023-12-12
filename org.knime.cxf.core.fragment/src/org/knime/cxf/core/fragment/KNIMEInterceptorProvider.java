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
package org.knime.cxf.core.fragment;

import java.util.Collections;
import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;
import org.knime.cxf.core.fragment.interceptors.HeaderFilterInterceptor;

/**
 * Provides custom CXF interceptors that all CXF clients which are instantiated in the AP should use.
 * Very general implementation to provide easy extensibility for further interceptors.
 * <p>
 * NB: for example, the {@link sun.net.www.protocol.http.HttpURLConnection} always caches proxy credentials
 * (see also <a href="https://github.com/frohoff/jdk8u-jdk/blob/ebf994e653bf56b6772ec621aacc6f48ee08d9a5/
 *src/share/classes/sun/net/www/protocol/http/HttpURLConnection.java#L2146-L2148">here</a>).
 * An idea for another interceptor may be one invalidating these caches for allowing a state-less
 * execution of REST requests using CXF. Not implemented since <tt>sun.*</tt> classes are not public API.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
public class KNIMEInterceptorProvider implements InterceptorProvider, CXFBusExtension<Void> {

    @Override
    public Void getExtension() {
        return null;
    }

    @Override
    public Class<Void> getRegistrationType() {
        return null;
    }

    @Override
    public void setOnBus(final Bus bus) {
        // could not get the extension architecture to work with the
        // InterceptorProvider.class as bus extension, but this works as well
        bus.getInInterceptors().addAll(this.getInInterceptors());
        bus.getOutInterceptors().addAll(this.getOutInterceptors());
        bus.getInFaultInterceptors().addAll(this.getInFaultInterceptors());
        bus.getOutFaultInterceptors().addAll(this.getOutFaultInterceptors());
    }

    // -- INTERCEPTORS --

    @Override
    public List<Interceptor<? extends Message>> getInInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public List<Interceptor<? extends Message>> getOutInterceptors() {
        return List.of(new HeaderFilterInterceptor());
    }

    @Override
    public List<Interceptor<? extends Message>> getInFaultInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public List<Interceptor<? extends Message>> getOutFaultInterceptors() {
        return Collections.emptyList();
    }
}
