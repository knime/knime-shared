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

import org.knime.shared.workflow.def.PortMetadataDef;
import java.util.Optional;

import org.knime.shared.workflow.def.impl.DefaultComponentMetadataDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



/**
 * ComponentMetadataDef
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonDeserialize(as = DefaultComponentMetadataDef.class)
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.interface-config.json"})
public interface ComponentMetadataDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /** 
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link ComponentMetadataDef#getDescription} 
          */
         DESCRIPTION,
         /** 
          * The type of this data attribute is java.util.List&lt;PortMetadataDef&gt;.
          * Is is returned by {@link ComponentMetadataDef#getInPortMetadata} 
          */
         IN_PORT_METADATA,
         /** 
          * The type of this data attribute is java.util.List&lt;PortMetadataDef&gt;.
          * Is is returned by {@link ComponentMetadataDef#getOutPortMetadata} 
          */
         OUT_PORT_METADATA,
         /** 
          * The type of this data attribute is {@link byte[]}.
          * Is is returned by {@link ComponentMetadataDef#getIcon} 
          */
         ICON,
         /**  
          * Summarizes the kind of functionality of the component.
          *
          * The type of this data attribute is {@link ComponentTypeEnum}.
          * Is is returned by {@link ComponentMetadataDef#getComponentType} 
          */
         COMPONENT_TYPE,
;
    }
    
  /**
   * Summarizes the kind of functionality of the component.
   */
  public enum ComponentTypeEnum {
    LEARNER("LEARNER"),
    
    MANIPULATOR("MANIPULATOR"),
    
    PREDICTOR("PREDICTOR"),
    
    SINK("SINK"),
    
    SOURCE("SOURCE"),
    
    VISUALIZER("VISUALIZER");

    private String value;

    ComponentTypeEnum(String value) {
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
  public Optional<String> getDescription();

  /**
   * @return 
   **/
  public Optional<java.util.List<PortMetadataDef>> getInPortMetadata();

  /**
   * @return 
   **/
  public Optional<java.util.List<PortMetadataDef>> getOutPortMetadata();

  /**
   * @return 
   **/
  public Optional<byte[]> getIcon();

  /**
   * @return Summarizes the kind of functionality of the component.
   **/
  public Optional<ComponentTypeEnum> getComponentType();


}
