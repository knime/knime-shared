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
 *   Oct 5, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Optional;

import org.knime.core.util.Version;

/**
 * Builder for {@link NativeNodeMetadata}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
class NativeNodeMetadataBuilder {

    private SingleNodeFields m_singleNodeFields;
    private Optional<String> m_nodeName;
    private Optional<String> m_factoryClass;
    private Optional<String> m_bundleName;
    private Optional<String> m_bundleSymbolicName;
    private Optional<String> m_bundleVendor;
    private Optional<Version> m_bundleVersion;
    private Optional<String> m_featureName;
    private Optional<String> m_featureSymbolicName;
    private Optional<String> m_featureVendor;
    private Optional<Version> m_featureVersion;

    private NodeAndBundleInformation m_nabi;
    private boolean m_NodeAndBundleInfoIsDirty;

    void setSingleNodeFields(final SingleNodeFields singleNodeFields) {
        m_singleNodeFields = singleNodeFields;
    }

    void setNodeName(final Optional<String> nodeName) {
        m_nodeName = nodeName;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setFactoryClass(final Optional<String> factoryClass) {
        m_factoryClass = factoryClass;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setBundleName(final Optional<String> bundleName) {
        m_bundleName = bundleName;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setBundleSymbolicName(final Optional<String> bundleSymbolicName) {
        m_bundleSymbolicName = bundleSymbolicName;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setBundleVendor(final Optional<String> bundleVendor) {
        m_bundleVendor = bundleVendor;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setBundleVersion(final Optional<Version> bundleVersion) {
        m_bundleVersion = bundleVersion;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setFeatureName(final Optional<String> featureName) {
        m_featureName = featureName;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setFeatureSymbolicName(final Optional<String> featureSymbolicName) {
        m_featureSymbolicName = featureSymbolicName;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setFeatureVendor(final Optional<String> featureVendor) {
        m_featureVendor = featureVendor;
        m_NodeAndBundleInfoIsDirty = true;
    }

    void setFeatureVersion(final Optional<Version> featureVersion) {
        m_featureVersion = featureVersion;
        m_NodeAndBundleInfoIsDirty = true;
    }

    SingleNodeFields getSingleNodeFields() {
        return m_singleNodeFields;
    }

    Optional<String> getNodeName() {
        return m_nodeName;
    }

    Optional<String> getFactoryClass() {
        return m_factoryClass;
    }

    Optional<String> getBundleName() {
        return m_bundleName;
    }

    Optional<String> getBundleSymbolicName() {
        return m_bundleSymbolicName;
    }

    Optional<String> getBundleVendor() {
        return m_bundleVendor;
    }

    Optional<Version> getBundleVersion() {
        return m_bundleVersion;
    }

    Optional<String> getFeatureName() {
        return m_featureName;
    }

    Optional<String> getFeatureSymbolicName() {
        return m_featureSymbolicName;
    }

    Optional<String> getFeatureVendor() {
        return m_featureVendor;
    }

    Optional<Version> getFeatureVersion() {
        return m_featureVersion;
    }

    NodeAndBundleInformation buildNodeAndBundleInformation() {
        if (m_NodeAndBundleInfoIsDirty) {
            m_nabi = new NodeAndBundleInformation(m_factoryClass, m_bundleSymbolicName, m_bundleName,
                m_bundleVendor, m_nodeName, m_bundleVersion, m_featureSymbolicName, m_featureName, m_featureVendor,
                m_featureVersion);
            m_NodeAndBundleInfoIsDirty = false;
        }
        return m_nabi;
    }

    NativeNodeMetadata build(final WorkflowalizerConfiguration wc) {
        m_singleNodeFields.validate();
        if (wc.parseNodeName()) {
            checkPopulated(m_nodeName, "node name");
        }
        if (wc.parseFactoryClass()) {
            checkPopulated(m_factoryClass, "factory class");
        }
        if (wc.parseBundleName()) {
            checkPopulated(m_bundleName, "bundle name");
        }
        if (wc.parseBundleSymbolicName()) {
            checkPopulated(m_bundleSymbolicName, "bundle symbolic name");
        }
        if (wc.parseBundleVendor()) {
            checkPopulated(m_bundleVendor, "bundle vendor");
        }
        if (wc.parseBundleVersion()) {
            checkPopulated(m_bundleVersion, "bundle version");
        }
        if (wc.parseFeatureName()) {
            checkPopulated(m_featureName, "feature name");
        }
        if (wc.parseFeatureSymbolicName()) {
            checkPopulated(m_featureSymbolicName, "feature symbolic name");
        }
        if (wc.parseFeatureVendor()) {
            checkPopulated(m_featureVendor, "feature vendor");
        }
        if (wc.parseFeatureVersion()) {
            checkPopulated(m_featureVersion, "feature version");
        }
        return new NativeNodeMetadata(this);
    }

    private static void checkPopulated(final Object field, final String name) {
        if (field == null) {
            throw new IllegalArgumentException("Requested field, " + name + ", should not be null");
        }
    }
}
