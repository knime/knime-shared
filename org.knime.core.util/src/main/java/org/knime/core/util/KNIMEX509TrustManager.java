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
 */
package org.knime.core.util;

import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;

/**
 * Custom trust manager that allows connection with the KNIME Server default certificate without checking the hostname.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 6.2
 */
public final class KNIMEX509TrustManager extends X509ExtendedTrustManager {

    private static final KNIMEX509TrustManager INSTANCE = new KNIMEX509TrustManager();

    private static final X509ExtendedTrustManager DEFAULT_X509_TRUST_MANAGER;

    static {
        try {
            var factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);

            X509ExtendedTrustManager defaultTrustManager = null;
            for (TrustManager each : factory.getTrustManagers()) {
                if (each instanceof X509ExtendedTrustManager manager) {
                    defaultTrustManager = manager;
                    break;
                }
            }
            if (defaultTrustManager == null) {
                throw new IllegalStateException("Could not find the default X509 trust manager");
            }
            DEFAULT_X509_TRUST_MANAGER = defaultTrustManager;
        } catch (NoSuchAlgorithmException | KeyStoreException ex) {
            throw new IllegalStateException("Could not find the default X509 trust manager: " + ex.getMessage(), ex);
        }
    }

    /**
     * Returns the singleton instance.
     *
     * @return the instance
     */
    public static KNIMEX509TrustManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        DEFAULT_X509_TRUST_MANAGER.checkClientTrusted(chain, authType);

    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        if (isDefaultServerInstallationCertificate(chain)) {
            return;
        }
        DEFAULT_X509_TRUST_MANAGER.checkServerTrusted(chain, authType);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return DEFAULT_X509_TRUST_MANAGER.getAcceptedIssuers();
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType, final Socket socket)
        throws CertificateException {
        DEFAULT_X509_TRUST_MANAGER.checkClientTrusted(chain, authType, socket);
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType, final Socket socket)
        throws CertificateException {
        if (isDefaultServerInstallationCertificate(chain)) {
            return;
        }
        DEFAULT_X509_TRUST_MANAGER.checkServerTrusted(chain, authType, socket);
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine)
        throws CertificateException {
        DEFAULT_X509_TRUST_MANAGER.checkServerTrusted(chain, authType, engine);
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType, final SSLEngine engine)
        throws CertificateException {
        if (isDefaultServerInstallationCertificate(chain)) {
            return;
        }
        DEFAULT_X509_TRUST_MANAGER.checkServerTrusted(chain, authType, engine);
    }

    private static boolean isDefaultServerInstallationCertificate(final X509Certificate[] chain) {
        return Arrays.stream(chain)
                .map(c -> c.getSubjectX500Principal().getName())
                .anyMatch(
                    "CN=default-server-installation.knime.local,O=KNIME.com AG,L=Atlantis,ST=Utopia,C=AA"::equals);
    }

    /**
     * Only singleton instance.
     */
    private KNIMEX509TrustManager() {
    }
}
