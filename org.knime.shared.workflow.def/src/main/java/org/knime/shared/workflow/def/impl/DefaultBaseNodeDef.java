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

import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.NodeUIInfoDef;

import org.knime.shared.workflow.def.BaseNodeDef;

import org.knime.shared.workflow.def.ComponentNodeDef;
import org.knime.shared.workflow.def.ConfigurableNodeDef;
import org.knime.shared.workflow.def.NativeNodeDef;
import org.knime.shared.workflow.def.MetaNodeDef;


// for types that define enums
import org.knime.shared.workflow.def.BaseNodeDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * Basic information about an executable block (node) in a workflow.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultBaseNodeDef implements BaseNodeDef {


    /** 
     * Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection.  
     */
    @JsonProperty("id")
    protected Integer m_id;

    /** 
     * states the most specific subtype, i.e., Metanode, Component, or Native Node 
     */
    @JsonProperty("nodeType")
    protected NodeTypeEnum m_nodeType;

    /** 
     * A longer description, provided by the user 
     */
    @JsonProperty("customDescription")
    protected String m_customDescription;

    @JsonProperty("annotation")
    protected NodeAnnotationDef m_annotation;

    @JsonProperty("uiInfo")
    protected NodeUIInfoDef m_uiInfo;

    @JsonProperty("locks")
    protected NodeLocksDef m_locks;

    @JsonProperty("jobManager")
    protected JobManagerDef m_jobManager;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultBaseNodeDef() {

    }


    /**
     * Copy constructor.
     * @param toCopy source
     */
    public DefaultBaseNodeDef(BaseNodeDef toCopy) {
        m_id = toCopy.getId();
        m_nodeType = toCopy.getNodeType();
        m_customDescription = toCopy.getCustomDescription();
        m_annotation = toCopy.getAnnotation();
        m_uiInfo = toCopy.getUiInfo();
        m_locks = toCopy.getLocks();
        m_jobManager = toCopy.getJobManager();
        

    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing BaseNode
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultBaseNodeDef withException(BaseNodeDef toCopy, final LoadException exception) {
        Objects.requireNonNull(exception);
        if (toCopy instanceof ComponentNodeDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new ComponentNodeDefBuilder().build());
            return new DefaultComponentNodeDef((ComponentNodeDef)toCopy, exception);
        }
        if (toCopy instanceof NativeNodeDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new NativeNodeDefBuilder().build());
            return new DefaultNativeNodeDef((NativeNodeDef)toCopy, exception);
        }
        if (toCopy instanceof MetaNodeDef) {
            toCopy = Objects.requireNonNullElse(toCopy, new MetaNodeDefBuilder().build());
            return new DefaultMetaNodeDef((MetaNodeDef)toCopy, exception);
        }
        throw new IllegalArgumentException();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree implementation
    // -----------------------------------------------------------------------------------------------------------------
    

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Integer getId() {
        return m_id;
    }
    @Override
    public NodeTypeEnum getNodeType() {
        return m_nodeType;
    }
    @Override
    public String getCustomDescription() {
        return m_customDescription;
    }
    @Override
    public NodeAnnotationDef getAnnotation() {
        return m_annotation;
    }
    @Override
    public NodeUIInfoDef getUiInfo() {
        return m_uiInfo;
    }
    @Override
    public NodeLocksDef getLocks() {
        return m_locks;
    }
    @Override
    public JobManagerDef getJobManager() {
        return m_jobManager;
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
        DefaultBaseNodeDef other = (DefaultBaseNodeDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_id, other.m_id);
        equalsBuilder.append(m_nodeType, other.m_nodeType);
        equalsBuilder.append(m_customDescription, other.m_customDescription);
        equalsBuilder.append(m_annotation, other.m_annotation);
        equalsBuilder.append(m_uiInfo, other.m_uiInfo);
        equalsBuilder.append(m_locks, other.m_locks);
        equalsBuilder.append(m_jobManager, other.m_jobManager);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_id)
                .append(m_nodeType)
                .append(m_customDescription)
                .append(m_annotation)
                .append(m_uiInfo)
                .append(m_locks)
                .append(m_jobManager)
                .toHashCode();
    }

} 
