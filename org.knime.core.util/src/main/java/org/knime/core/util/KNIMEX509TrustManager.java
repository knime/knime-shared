/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME AG, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 *
 * History
 *   Created on 30.06.2023 by thor
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
