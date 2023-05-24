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
package org.knime.shared.workflow.def;

import org.knime.shared.workflow.def.AnnotationDataDef;
import org.knime.shared.workflow.def.AuthorInformationDef;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.ConnectionDef;
import org.knime.shared.workflow.def.WorkflowUISettingsDef;

import org.knime.shared.workflow.def.impl.DefaultWorkflowDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



/**
 * Defines a data processing pipeline.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonDeserialize(as = DefaultWorkflowDef.class)
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.interface-config.json"})
public interface WorkflowDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /**  
          * A user-chosen identifier for the workflow.
          *
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link WorkflowDef#getName} 
          */
         NAME,
         /** 
          * The type of this data attribute is {@link AuthorInformationDef}.
          * Is is returned by {@link WorkflowDef#getAuthorInformation} 
          */
         AUTHOR_INFORMATION,
         /**  
          * The executable blocks in this workflow.
          *
          * The type of this data attribute is java.util.Map&lt;String, BaseNodeDef&gt;.
          * Is is returned by {@link WorkflowDef#getNodes} 
          */
         NODES,
         /**  
          * Define the data flow between nodes.
          *
          * The type of this data attribute is java.util.List&lt;ConnectionDef&gt;.
          * Is is returned by {@link WorkflowDef#getConnections} 
          */
         CONNECTIONS,
         /**  
          * Explanatory text boxes that are shown in the workflow editor.
          *
          * The type of this data attribute is java.util.Map&lt;String, AnnotationDataDef&gt;.
          * Is is returned by {@link WorkflowDef#getAnnotations} 
          */
         ANNOTATIONS,
         /** 
          * The type of this data attribute is {@link WorkflowUISettingsDef}.
          * Is is returned by {@link WorkflowDef#getWorkflowEditorSettings} 
          */
         WORKFLOW_EDITOR_SETTINGS,
;
    }
    

  /**
   * @return A user-chosen identifier for the workflow.
   **/
  public String getName();

  /**
   * @return 
   **/
  public AuthorInformationDef getAuthorInformation();

  /**
   * @return The executable blocks in this workflow.
   **/
  public java.util.Map<String, BaseNodeDef> getNodes();

  /**
   * @return Define the data flow between nodes.
   **/
  public java.util.List<ConnectionDef> getConnections();

  /**
   * @return Explanatory text boxes that are shown in the workflow editor.
   **/
  public java.util.Map<String, AnnotationDataDef> getAnnotations();

  /**
   * @return 
   **/
  public WorkflowUISettingsDef getWorkflowEditorSettings();


}
