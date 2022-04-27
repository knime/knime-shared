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

import org.knime.core.workflow.def.CipherDef;
import org.knime.core.workflow.def.ComponentDialogSettingsDef;
import org.knime.core.workflow.def.ComponentMetadataDef;
import org.knime.core.workflow.def.ConfigMapDef;
import org.knime.core.workflow.def.ConfigurableNodeDef;
import org.knime.core.workflow.def.JobManagerDef;
import org.knime.core.workflow.def.NodeAnnotationDef;
import org.knime.core.workflow.def.NodeLocksDef;
import org.knime.core.workflow.def.NodeUIInfoDef;
import org.knime.core.workflow.def.PortDef;
import org.knime.core.workflow.def.TemplateInfoDef;
import org.knime.core.workflow.def.WorkflowDef;

import org.knime.core.workflow.def.impl.FallibleComponentDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



/**
 * A node that contains a workflow. Similar to a metanode, except it has more flexibility, e.g., filtering the incoming and outgoing flow variables.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonDeserialize(as = FallibleComponentDef.class)
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.interface-config.json"})
public interface ComponentDef extends ConfigurableNodeDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /**  
          * Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. 
          *
          * The type of this data attribute is {@link Integer}.
          * Is is returned by {@link ComponentDef#getId} 
          */
         ID,
         /**  
          * states the most specific subtype, i.e., Metanode, Component, or Native Node
          *
          * The type of this data attribute is {@link NodeTypeEnum}.
          * Is is returned by {@link ComponentDef#getNodeType} 
          */
         NODE_TYPE,
         /**  
          * A longer description, provided by the user
          *
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link ComponentDef#getCustomDescription} 
          */
         CUSTOM_DESCRIPTION,
         /** 
          * The type of this data attribute is {@link NodeAnnotationDef}.
          * Is is returned by {@link ComponentDef#getAnnotation} 
          */
         ANNOTATION,
         /** 
          * The type of this data attribute is {@link NodeUIInfoDef}.
          * Is is returned by {@link ComponentDef#getUiInfo} 
          */
         UI_INFO,
         /** 
          * The type of this data attribute is {@link NodeLocksDef}.
          * Is is returned by {@link ComponentDef#getLocks} 
          */
         LOCKS,
         /** 
          * The type of this data attribute is {@link JobManagerDef}.
          * Is is returned by {@link ComponentDef#getJobManager} 
          */
         JOB_MANAGER,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link ComponentDef#getModelSettings} 
          */
         MODEL_SETTINGS,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link ComponentDef#getInternalNodeSubSettings} 
          */
         INTERNAL_NODE_SUB_SETTINGS,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link ComponentDef#getVariableSettings} 
          */
         VARIABLE_SETTINGS,
         /** 
          * The type of this data attribute is {@link WorkflowDef}.
          * Is is returned by {@link ComponentDef#getWorkflow} 
          */
         WORKFLOW,
         /** 
          * The type of this data attribute is java.util.List&lt;PortDef&gt;.
          * Is is returned by {@link ComponentDef#getInPorts} 
          */
         IN_PORTS,
         /** 
          * The type of this data attribute is java.util.List&lt;PortDef&gt;.
          * Is is returned by {@link ComponentDef#getOutPorts} 
          */
         OUT_PORTS,
         /** 
          * The type of this data attribute is {@link CipherDef}.
          * Is is returned by {@link ComponentDef#getCipher} 
          */
         CIPHER,
         /**  
          * The virtual in node provides the input ports of the component as its output ports (replaces the input bar of the metanode)
          *
          * The type of this data attribute is {@link Integer}.
          * Is is returned by {@link ComponentDef#getVirtualInNodeId} 
          */
         VIRTUAL_IN_NODE_ID,
         /** 
          * The type of this data attribute is {@link Integer}.
          * Is is returned by {@link ComponentDef#getVirtualOutNodeId} 
          */
         VIRTUAL_OUT_NODE_ID,
         /** 
          * The type of this data attribute is {@link ComponentMetadataDef}.
          * Is is returned by {@link ComponentDef#getMetadata} 
          */
         METADATA,
         /** 
          * The type of this data attribute is {@link TemplateInfoDef}.
          * Is is returned by {@link ComponentDef#getTemplateInfo} 
          */
         TEMPLATE_INFO,
         /** 
          * The type of this data attribute is {@link ComponentDialogSettingsDef}.
          * Is is returned by {@link ComponentDef#getDialogSettings} 
          */
         DIALOG_SETTINGS,
;
    }
    

  /**
   * @return 
   **/
  public WorkflowDef getWorkflow();

  /**
   * @return 
   **/
  public java.util.List<PortDef> getInPorts();

  /**
   * @return 
   **/
  public java.util.List<PortDef> getOutPorts();

  /**
   * @return 
   **/
  public CipherDef getCipher();

  /**
   * @return The virtual in node provides the input ports of the component as its output ports (replaces the input bar of the metanode)
   **/
  public Integer getVirtualInNodeId();

  /**
   * @return 
   **/
  public Integer getVirtualOutNodeId();

  /**
   * @return 
   **/
  public ComponentMetadataDef getMetadata();

  /**
   * @return 
   **/
  public TemplateInfoDef getTemplateInfo();

  /**
   * @return 
   **/
  public ComponentDialogSettingsDef getDialogSettings();


}
