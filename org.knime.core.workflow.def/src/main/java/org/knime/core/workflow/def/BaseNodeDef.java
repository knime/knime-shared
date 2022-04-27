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
package org.knime.core.workflow.def;

import org.knime.core.workflow.def.JobManagerDef;
import org.knime.core.workflow.def.NodeAnnotationDef;
import org.knime.core.workflow.def.NodeLocksDef;
import org.knime.core.workflow.def.NodeUIInfoDef;

import org.knime.core.workflow.def.impl.FallibleBaseNodeDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.knime.core.workflow.def.impl.FallibleBaseNodeDef;
import org.knime.core.workflow.def.impl.FallibleComponentDef;
import org.knime.core.workflow.def.impl.FallibleMetaNodeDef;
import org.knime.core.workflow.def.impl.FallibleConfigurableNodeDef;
import org.knime.core.workflow.def.impl.FallibleNativeNodeDef;


/**
 * Basic information about an executable block (node) in a workflow.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "nodeType",
    visible = true,
    defaultImpl = FallibleBaseNodeDef.class)
@JsonSubTypes({
    @Type(value = FallibleBaseNodeDef.class, name="BaseNode")
, @Type(value = FallibleComponentDef.class, name = "Component")
, @Type(value = FallibleMetaNodeDef.class, name = "MetaNode")
, @Type(value = FallibleConfigurableNodeDef.class, name = "ConfigurableNode")
, @Type(value = FallibleNativeNodeDef.class, name = "NativeNode")
})
@JsonDeserialize(as = FallibleBaseNodeDef.class)
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.interface-config.json"})
public interface BaseNodeDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /**  
          * Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. 
          *
          * The type of this data attribute is {@link Integer}.
          * Is is returned by {@link BaseNodeDef#getId} 
          */
         ID,
         /**  
          * states the most specific subtype, i.e., Metanode, Component, or Native Node
          *
          * The type of this data attribute is {@link NodeTypeEnum}.
          * Is is returned by {@link BaseNodeDef#getNodeType} 
          */
         NODE_TYPE,
         /**  
          * A longer description, provided by the user
          *
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link BaseNodeDef#getCustomDescription} 
          */
         CUSTOM_DESCRIPTION,
         /** 
          * The type of this data attribute is {@link NodeAnnotationDef}.
          * Is is returned by {@link BaseNodeDef#getAnnotation} 
          */
         ANNOTATION,
         /** 
          * The type of this data attribute is {@link NodeUIInfoDef}.
          * Is is returned by {@link BaseNodeDef#getUiInfo} 
          */
         UI_INFO,
         /** 
          * The type of this data attribute is {@link NodeLocksDef}.
          * Is is returned by {@link BaseNodeDef#getLocks} 
          */
         LOCKS,
         /** 
          * The type of this data attribute is {@link JobManagerDef}.
          * Is is returned by {@link BaseNodeDef#getJobManager} 
          */
         JOB_MANAGER,
;
    }
    
  /**
   * states the most specific subtype, i.e., Metanode, Component, or Native Node
   */
  public enum NodeTypeEnum {
    NATIVE_NODE("NATIVE_NODE"),
    
    COMPONENT("COMPONENT"),
    
    METANODE("METANODE");

    private String value;

    NodeTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

  }


  /**
   * @return Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. 
   **/
  public Integer getId();

  /**
   * @return states the most specific subtype, i.e., Metanode, Component, or Native Node
   **/
  public NodeTypeEnum getNodeType();

  /**
   * @return A longer description, provided by the user
   **/
  public String getCustomDescription();

  /**
   * @return 
   **/
  public NodeAnnotationDef getAnnotation();

  /**
   * @return 
   **/
  public NodeUIInfoDef getUiInfo();

  /**
   * @return 
   **/
  public NodeLocksDef getLocks();

  /**
   * @return 
   **/
  public JobManagerDef getJobManager();


}
