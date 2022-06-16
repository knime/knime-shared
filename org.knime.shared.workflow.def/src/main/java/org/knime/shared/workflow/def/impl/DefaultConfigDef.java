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
 * ------------------------------------------------------------------------
 */
package org.knime.shared.workflow.def.impl;

import java.util.Map;
import java.util.Optional;
import java.util.Objects; 

import org.apache.commons.lang3.builder.HashCodeBuilder;


import org.knime.shared.workflow.def.ConfigDef;

import org.knime.shared.workflow.def.ConfigValueFloatArrayDef;
import org.knime.shared.workflow.def.ConfigValueStringArrayDef;
import org.knime.shared.workflow.def.ConfigValueCharArrayDef;
import org.knime.shared.workflow.def.ConfigValueStringDef;
import org.knime.shared.workflow.def.ConfigValueCharDef;
import org.knime.shared.workflow.def.ConfigValueDoubleDef;
import org.knime.shared.workflow.def.ConfigValueBooleanArrayDef;
import org.knime.shared.workflow.def.ConfigValueIntArrayDef;
import org.knime.shared.workflow.def.ConfigValuePasswordDef;
import org.knime.shared.workflow.def.ConfigValueDef;
import org.knime.shared.workflow.def.ConfigValueByteDef;
import org.knime.shared.workflow.def.ConfigValueDoubleArrayDef;
import org.knime.shared.workflow.def.ConfigValueLongDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.ConfigValueByteArrayDef;
import org.knime.shared.workflow.def.ConfigValueShortDef;
import org.knime.shared.workflow.def.ConfigValueLongArrayDef;
import org.knime.shared.workflow.def.ConfigValueShortArrayDef;
import org.knime.shared.workflow.def.ConfigValueBooleanDef;
import org.knime.shared.workflow.def.ConfigValueFloatDef;
import org.knime.shared.workflow.def.ConfigValueArrayDef;
import org.knime.shared.workflow.def.ConfigValueIntDef;


// for types that define enums
import org.knime.shared.workflow.def.ConfigDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * A tree of configuration values as formed by org.knime.core.node.NodeSettings. Can be either a subtree (ConfigMap) or a leaf (ConfigValueInt, ConfigValueIntArray, etc.). This is a marker interface.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public abstract class DefaultConfigDef implements ConfigDef, LoadExceptionTreeProvider {


    /** 
     * Discriminator for inheritance. Must be the base name of this type/schema. 
     * 
     * Example value: ConfigValueCharArray
     */
    @JsonProperty("configType")
    protected String m_configType;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultConfigDef() {

    }


    /**
     * Copy constructor.
     * @param toCopy source
     */
    public DefaultConfigDef(ConfigDef toCopy) {
        m_configType = toCopy.getConfigType();
        

    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing Config
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultConfigDef withException(ConfigDef toCopy, final LoadException exception) {
        Objects.requireNonNull(exception);
        if (toCopy instanceof ConfigValueFloatArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueFloatArrayDefBuilder().build());
            return new DefaultConfigValueFloatArrayDef((ConfigValueFloatArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueStringArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueStringArrayDefBuilder().build());
            return new DefaultConfigValueStringArrayDef((ConfigValueStringArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueCharArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueCharArrayDefBuilder().build());
            return new DefaultConfigValueCharArrayDef((ConfigValueCharArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueStringDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueStringDefBuilder().build());
            return new DefaultConfigValueStringDef((ConfigValueStringDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueCharDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueCharDefBuilder().build());
            return new DefaultConfigValueCharDef((ConfigValueCharDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueDoubleDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueDoubleDefBuilder().build());
            return new DefaultConfigValueDoubleDef((ConfigValueDoubleDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueBooleanArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueBooleanArrayDefBuilder().build());
            return new DefaultConfigValueBooleanArrayDef((ConfigValueBooleanArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueIntArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueIntArrayDefBuilder().build());
            return new DefaultConfigValueIntArrayDef((ConfigValueIntArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValuePasswordDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValuePasswordDefBuilder().build());
            return new DefaultConfigValuePasswordDef((ConfigValuePasswordDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueByteDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueByteDefBuilder().build());
            return new DefaultConfigValueByteDef((ConfigValueByteDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueDoubleArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueDoubleArrayDefBuilder().build());
            return new DefaultConfigValueDoubleArrayDef((ConfigValueDoubleArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueLongDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueLongDefBuilder().build());
            return new DefaultConfigValueLongDef((ConfigValueLongDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigMapDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigMapDefBuilder().build());
            return new DefaultConfigMapDef((ConfigMapDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueByteArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueByteArrayDefBuilder().build());
            return new DefaultConfigValueByteArrayDef((ConfigValueByteArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueShortDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueShortDefBuilder().build());
            return new DefaultConfigValueShortDef((ConfigValueShortDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueLongArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueLongArrayDefBuilder().build());
            return new DefaultConfigValueLongArrayDef((ConfigValueLongArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueShortArrayDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueShortArrayDefBuilder().build());
            return new DefaultConfigValueShortArrayDef((ConfigValueShortArrayDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueBooleanDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueBooleanDefBuilder().build());
            return new DefaultConfigValueBooleanDef((ConfigValueBooleanDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueFloatDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueFloatDefBuilder().build());
            return new DefaultConfigValueFloatDef((ConfigValueFloatDef)toCopy, exception);
        }
        if (toCopy instanceof ConfigValueIntDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ConfigValueIntDefBuilder().build());
            return new DefaultConfigValueIntDef((ConfigValueIntDef)toCopy, exception);
        }
        throw new IllegalArgumentException();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree implementation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @return the load exceptions for this instance and its descendants
     */
    @JsonIgnore
    @Override
    public abstract LoadExceptionTree<?> getLoadExceptionTree();
    

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getConfigType() {
        return m_configType;
    }
    

    // -----------------------------------------------------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o.getClass().equals(this.getClass()))) {
            return false;
        }
        DefaultConfigDef other = (DefaultConfigDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_configType, other.m_configType);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_configType)
                .toHashCode();
    }

} 
