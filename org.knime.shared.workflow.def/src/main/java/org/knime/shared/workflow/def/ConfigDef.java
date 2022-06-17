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

import java.util.Optional;

import org.knime.shared.workflow.def.impl.DefaultConfigDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.knime.shared.workflow.def.impl.DefaultConfigDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueCharArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueIntDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueDoubleArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueBooleanArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueStringArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValuePasswordDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueLongArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueStringDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueDoubleDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueLongDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueShortDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueByteArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueBooleanDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueShortArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueByteDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueFloatDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueIntArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueArrayDef;
import org.knime.shared.workflow.def.impl.DefaultConfigMapDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueCharDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueTransientStringDef;
import org.knime.shared.workflow.def.impl.DefaultConfigValueFloatArrayDef;


/**
 * A tree of configuration values as formed by org.knime.core.node.NodeSettings. Can be either a subtree (ConfigMap) or a leaf (ConfigValueInt, ConfigValueIntArray, etc.). This is a marker interface.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "configType",
    visible = true,
    defaultImpl = DefaultConfigDef.class)
@JsonSubTypes({
    @Type(value = DefaultConfigDef.class, name="Config")
, @Type(value = DefaultConfigValueCharArrayDef.class, name = "ConfigValueCharArray")
, @Type(value = DefaultConfigValueIntDef.class, name = "ConfigValueInt")
, @Type(value = DefaultConfigValueDoubleArrayDef.class, name = "ConfigValueDoubleArray")
, @Type(value = DefaultConfigValueBooleanArrayDef.class, name = "ConfigValueBooleanArray")
, @Type(value = DefaultConfigValueStringArrayDef.class, name = "ConfigValueStringArray")
, @Type(value = DefaultConfigValueDef.class, name = "ConfigValue")
, @Type(value = DefaultConfigValuePasswordDef.class, name = "ConfigValuePassword")
, @Type(value = DefaultConfigValueLongArrayDef.class, name = "ConfigValueLongArray")
, @Type(value = DefaultConfigValueStringDef.class, name = "ConfigValueString")
, @Type(value = DefaultConfigValueDoubleDef.class, name = "ConfigValueDouble")
, @Type(value = DefaultConfigValueLongDef.class, name = "ConfigValueLong")
, @Type(value = DefaultConfigValueShortDef.class, name = "ConfigValueShort")
, @Type(value = DefaultConfigValueByteArrayDef.class, name = "ConfigValueByteArray")
, @Type(value = DefaultConfigValueBooleanDef.class, name = "ConfigValueBoolean")
, @Type(value = DefaultConfigValueShortArrayDef.class, name = "ConfigValueShortArray")
, @Type(value = DefaultConfigValueByteDef.class, name = "ConfigValueByte")
, @Type(value = DefaultConfigValueFloatDef.class, name = "ConfigValueFloat")
, @Type(value = DefaultConfigValueIntArrayDef.class, name = "ConfigValueIntArray")
, @Type(value = DefaultConfigValueArrayDef.class, name = "ConfigValueArray")
, @Type(value = DefaultConfigMapDef.class, name = "ConfigMap")
, @Type(value = DefaultConfigValueCharDef.class, name = "ConfigValueChar")
, @Type(value = DefaultConfigValueTransientStringDef.class, name = "ConfigValueTransientString")
, @Type(value = DefaultConfigValueFloatArrayDef.class, name = "ConfigValueFloatArray")
})
@JsonDeserialize(as = DefaultConfigDef.class)
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.interface-config.json"})
public interface ConfigDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /**  
          * Discriminator for inheritance. Must be the base name of this type/schema.
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link ConfigDef#getConfigType} 
          */
         CONFIG_TYPE,
;
    }
    

  /**
   * Example value: ConfigValueCharArray
   * @return Discriminator for inheritance. Must be the base name of this type/schema., never <code>null</code>
   **/
  public String getConfigType();


}
