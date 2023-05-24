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

import org.knime.shared.workflow.def.CreatorDef;

import org.knime.shared.workflow.def.impl.DefaultStandaloneDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



/**
 * Represents a standalone, loadable unit for editing in the workflow editor. In case this is a workflow, it is also executable. Standalone components and metanodes cannot be executed but referenced into a workflow and updated when the referenced component/metanode changed. 
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonDeserialize(as = DefaultStandaloneDef.class)
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.interface-config.json"})
public interface StandaloneDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /** 
          * The type of this data attribute is {@link CreatorDef}.
          * Is is returned by {@link StandaloneDef#getCreator} 
          */
         CREATOR,
         /**  
          * This is either a RootWorkflow or a Component or a Metanode, which one is indicated by contentType. Having a workflow is useful when adding version information to copied content. 
          *
          * The type of this data attribute is {@link Object}.
          * Is is returned by {@link StandaloneDef#getContents} 
          */
         CONTENTS,
         /**  
          * Describes the type of the contents field.
          *
          * The type of this data attribute is {@link ContentTypeEnum}.
          * Is is returned by {@link StandaloneDef#getContentType} 
          */
         CONTENT_TYPE,
;
    }
    
  /**
   * Describes the type of the contents field.
   */
  public enum ContentTypeEnum {
    WORKFLOW("Workflow"),
    
    ROOT_WORKFLOW("Root Workflow"),
    
    METANODE("Metanode"),
    
    COMPONENT("Component");

    private String value;

    ContentTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

  }


  /**
   * @return 
   **/
  public CreatorDef getCreator();

  /**
   * @return This is either a RootWorkflow or a Component or a Metanode, which one is indicated by contentType. Having a workflow is useful when adding version information to copied content. 
   **/
  public Object getContents();

  /**
   * @return Describes the type of the contents field.
   **/
  public ContentTypeEnum getContentType();


}
