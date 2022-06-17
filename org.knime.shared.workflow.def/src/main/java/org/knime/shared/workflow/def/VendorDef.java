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

import org.knime.shared.workflow.def.impl.DefaultVendorDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



/**
 * The author/provider of a native node. Describes bundles and features that contain the code to execute  a native node.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonDeserialize(as = DefaultVendorDef.class)
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.interface-config.json"})
public interface VendorDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /**  
          * For a bundle, see the example value; for a feature it can be an empty string
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link VendorDef#getName} 
          */
         NAME,
         /**  
          * For a bundle, see the example value; for a feature it can be an empty string
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link VendorDef#getSymbolicName} 
          */
         SYMBOLIC_NAME,
         /**  
          * For a bundle, see the example value; for a feature it can be an empty string
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link VendorDef#getVendor} 
          */
         VENDOR,
         /**  
          * For a bundle, see the example value; for a feature, it can be, e.g., \&quot;0.0.0\&quot;
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link VendorDef#getVersion} 
          */
         VERSION,
;
    }
    

  /**
   * Example value: KNIME Base Nodes
   * @return For a bundle, see the example value; for a feature it can be an empty string, never <code>null</code>
   **/
  public String getName();

  /**
   * Example value: org.knime.base
   * @return For a bundle, see the example value; for a feature it can be an empty string, never <code>null</code>
   **/
  public String getSymbolicName();

  /**
   * Example value: KNIME AG, Zurich, Switzerland
   * @return For a bundle, see the example value; for a feature it can be an empty string, never <code>null</code>
   **/
  public String getVendor();

  /**
   * Example value: 4.6.0.v202201041551
   * @return For a bundle, see the example value; for a feature, it can be, e.g., \&quot;0.0.0\&quot;, never <code>null</code>
   **/
  public String getVersion();


}
