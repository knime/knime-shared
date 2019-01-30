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
 *   Sep 12, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.knime.core.util.Version;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO containing information about a KNIME node and bundle information.
 *
 * <p>Adapted from NodeAndBundleInformation class in knime-core.</p>
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class NodeAndBundleInformation {

    @JsonProperty("featureSymbolicName")
    private final Optional<String> m_featureSymbolicName;

    @JsonProperty("featureName")
    private final Optional<String> m_featureName;

    @JsonProperty("featureVendor")
    private final Optional<String> m_featureVendor;

    @JsonProperty("featureVersion")
    private final Optional<Version> m_featureVersion;

    @JsonProperty("bundleSymbolicName")
    private final Optional<String> m_bundleSymbolicName;

    @JsonProperty("bundleName")
    private final Optional<String> m_bundleName;

    @JsonProperty("bundleVendor")
    private final Optional<String> m_bundleVendor;

    @JsonProperty("nodeName")
    private final Optional<String> m_nodeName;

    @JsonProperty("factoryClass")
    private final Optional<String> m_factoryClass;

    @JsonProperty("bundleVersion")
    private final Optional<Version> m_bundleVersion;

    /**
     *
     * @param factoryClass
     * @param bundleSymbolicName
     * @param bundleName
     * @param bundleVendor
     * @param nodeName
     * @param bundleVersion
     * @param featureSymbolicName
     * @param featureName
     * @param featureVendor
     * @param featureVersion
     */
    public NodeAndBundleInformation(final Optional<String> factoryClass, final Optional<String> bundleSymbolicName,
        final Optional<String> bundleName, final Optional<String> bundleVendor, final Optional<String> nodeName,
        final Optional<Version> bundleVersion, final Optional<String> featureSymbolicName,
        final Optional<String> featureName, final Optional<String> featureVendor,
        final Optional<Version> featureVersion) {
        m_bundleSymbolicName = bundleSymbolicName;
        m_bundleName = bundleName;
        m_bundleVendor = bundleVendor;
        m_nodeName = nodeName;
        m_bundleVersion = bundleVersion;
        m_featureName = featureName;
        m_featureSymbolicName = featureSymbolicName;
        m_featureVendor = featureVendor;
        m_featureVersion = featureVersion;
        m_factoryClass = factoryClass;
    }

    /**
     * Returns the bundle's name in which the node is contained.
     *
     * @return the bundle's name
     * @since 3.0
     */
    public Optional<String> getBundleName() {
        if (m_bundleName == null) {
            throw new UnsupportedOperationException("getBundleName() is not supported, field was not read");
        }
        return m_bundleName;
    }

    /**
     * Returns the bundle's symbolic name in which the component is contained.
     *
     * @return the bundle's symbolic name
     */
    public Optional<String> getBundleSymbolicName() {
        if (m_bundleSymbolicName == null) {
            throw new UnsupportedOperationException("getBundleSymbolicName() is not supported, field was not read");
        }
        return m_bundleSymbolicName;
    }

    /**
     * Returns the bundle's vendor in which the node is contained.
     *
     * @return the bundle's vendor
     * @since 3.0
     */
    public Optional<String> getBundleVendor() {
        if (m_bundleVendor == null) {
            throw new UnsupportedOperationException("getBundleVendor() is not supported, field was not read");
        }
        return m_bundleVendor;
    }

    /**
     * Returns the bundle's version in which the node is contained.
     *
     * @return the bundle's version
     * @since 3.0
     */
    public Optional<Version> getBundleVersion() {
        if (m_bundleVersion == null) {
            throw new UnsupportedOperationException("getBundleVersion() is not supported, field was not read");
        }
        return m_bundleVersion;
    }

    /**
     * Returns the node's name.
     *
     * @return the node's name
     * @since 3.0
     */
    public Optional<String> getNodeName() {
        if (m_nodeName == null) {
            throw new UnsupportedOperationException("getNodeName() is not supported, field was not read");
        }
        return m_nodeName;
    }

    /**
     * Returns the node's factory class name.
     *
     * @return the factory class, never <code>null</code>
     */
    public Optional<String> getFactoryClass() {
        if (m_factoryClass == null) {
            throw new UnsupportedOperationException("getFactoryClass() is not supported, field was not read");
        }
        return m_factoryClass;
    }

    /**
     * Returns the features's name in which the node is contained.
     *
     * @return the feature's name
     * @since 3.0
     */
    public Optional<String> getFeatureName() {
        if (m_featureName == null) {
            throw new UnsupportedOperationException("getFeatureName() is not supported, field was not read");
        }
        return m_featureName;
    }

    /**
     * Returns the features's symbolic name in which the component is contained.
     *
     * @return the feature's symbolic name
     */
    public Optional<String> getFeatureSymbolicName() {
        if (m_featureSymbolicName == null) {
            throw new UnsupportedOperationException("getFeatureSymbolicName() is not supported, field was not read");
        }
        return m_featureSymbolicName;
    }

    /**
     * Returns the features' vendor in which the node is contained.
     *
     * @return the feature's vendor
     * @since 3.0
     */
    public Optional<String> getFeatureVendor() {
        if (m_featureVendor == null) {
            throw new UnsupportedOperationException("getFeatureVendor() is not supported, field was not read");
        }
        return m_featureVendor;
    }

    /**
     * Returns the features's version in which the node is contained.
     *
     * @return the feature's version
     * @since 3.0
     */
    public Optional<Version> getFeatureVersion() {
        if (m_featureVersion == null) {
            throw new UnsupportedOperationException("getFeatureVersion() is not supported, field was not read");
        }
        return m_featureVersion;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof NodeAndBundleInformation)) {
            return false;
        }
        final NodeAndBundleInformation other = (NodeAndBundleInformation)obj;
        return new EqualsBuilder()
                .append(m_featureSymbolicName, other.m_featureSymbolicName)
                .append(m_featureName, other.m_featureName)
                .append(m_featureVendor, other.m_featureVendor)
                .append(m_featureVersion, other.m_featureVersion)
                .append(m_bundleSymbolicName, other.m_bundleSymbolicName)
                .append(m_bundleName, other.m_bundleName)
                .append(m_bundleVendor, other.m_bundleVendor)
                .append(m_nodeName, other.m_nodeName)
                .append(m_factoryClass, other.m_factoryClass)
                .append(m_bundleVersion, other.m_bundleVersion)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_featureSymbolicName)
                .append(m_featureName)
                .append(m_featureVendor)
                .append(m_featureVersion)
                .append(m_bundleSymbolicName)
                .append(m_bundleName)
                .append(m_bundleVendor)
                .append(m_nodeName)
                .append(m_factoryClass)
                .append(m_bundleVersion)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "node_name: " + m_nodeName.orElse(null) +
        ", factory_class: " + m_factoryClass.orElse(null) +
        ", bundle_symbolic_name: " + m_bundleSymbolicName.orElse(null) +
        ", bundle_name: " + m_bundleName.orElse(null) +
        ", bundle_vendor: " + m_bundleVendor.orElse(null) +
        ", bundle_version: " + m_bundleVersion.orElse(null) +
        ", feature_symbolic_name: " + m_featureSymbolicName.orElse(null) +
        ", feature_name: " + m_featureName.orElse(null) +
        ", feature_vendor: " + m_featureVendor.orElse(null) +
        ", feature_version: " + m_featureVersion.orElse(null);
    }
}

