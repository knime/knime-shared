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

import org.knime.shared.workflow.def.BoundsDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.FilestoreDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.SingleNodeDef;
import org.knime.shared.workflow.def.VendorDef;
import java.util.Optional;

import org.knime.shared.workflow.def.impl.DefaultNativeNodeDef;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



/**
 * The basic executable building block of a workflow. 
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
@JsonDeserialize(as = DefaultNativeNodeDef.class)
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.interface-config.json"})
public interface NativeNodeDef extends SingleNodeDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /**  
          * Identifies the node within the scope of its containing workflow, e.g., for specifying the source or target of a connection. Standalone metanodes and components do not have an id since they have no containing workflow.
          *
          * The type of this data attribute is {@link Integer}.
          * Is is returned by {@link NativeNodeDef#getId} 
          */
         ID,
         /**  
          * states the most specific subtype, i.e., Metanode, Component, or Native Node
          *
          * This is a required field.
          * The type of this data attribute is {@link NodeTypeEnum}.
          * Is is returned by {@link NativeNodeDef#getNodeType} 
          */
         NODE_TYPE,
         /**  
          * A longer description, provided by the user
          *
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link NativeNodeDef#getCustomDescription} 
          */
         CUSTOM_DESCRIPTION,
         /** 
          * The type of this data attribute is {@link NodeAnnotationDef}.
          * Is is returned by {@link NativeNodeDef#getAnnotation} 
          */
         ANNOTATION,
         /** 
          * The type of this data attribute is {@link BoundsDef}.
          * Is is returned by {@link NativeNodeDef#getBounds} 
          */
         BOUNDS,
         /** 
          * The type of this data attribute is {@link NodeLocksDef}.
          * Is is returned by {@link NativeNodeDef#getLocks} 
          */
         LOCKS,
         /** 
          * The type of this data attribute is {@link JobManagerDef}.
          * Is is returned by {@link NativeNodeDef#getJobManager} 
          */
         JOB_MANAGER,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link NativeNodeDef#getModelSettings} 
          */
         MODEL_SETTINGS,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link NativeNodeDef#getInternalNodeSubSettings} 
          */
         INTERNAL_NODE_SUB_SETTINGS,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link NativeNodeDef#getVariableSettings} 
          */
         VARIABLE_SETTINGS,
         /**  
          * Describes and identifies the node in the node repository
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link NativeNodeDef#getNodeName} 
          */
         NODE_NAME,
         /**  
          * Qualified class name
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link NativeNodeDef#getFactory} 
          */
         FACTORY,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link NativeNodeDef#getFactorySettings} 
          */
         FACTORY_SETTINGS,
         /** 
          * This is a required field.
          * The type of this data attribute is {@link VendorDef}.
          * Is is returned by {@link NativeNodeDef#getFeature} 
          */
         FEATURE,
         /** 
          * This is a required field.
          * The type of this data attribute is {@link VendorDef}.
          * Is is returned by {@link NativeNodeDef#getBundle} 
          */
         BUNDLE,
         /** 
          * The type of this data attribute is {@link ConfigMapDef}.
          * Is is returned by {@link NativeNodeDef#getNodeCreationConfig} 
          */
         NODE_CREATION_CONFIG,
         /** 
          * The type of this data attribute is {@link FilestoreDef}.
          * Is is returned by {@link NativeNodeDef#getFilestore} 
          */
         FILESTORE,
;
    }
    

  /**
   * Example value: Table Creator
   * @return Describes and identifies the node in the node repository, never <code>null</code>
   **/
  public String getNodeName();

  /**
   * Example value: org.knime.base.node.io.tablecreator.TableCreator2NodeFactory
   * @return Qualified class name, never <code>null</code>
   **/
  public String getFactory();

  /**
   * @return 
   **/
  public Optional<ConfigMapDef> getFactorySettings();

  /**
   * @return , never <code>null</code>
   **/
  public VendorDef getFeature();

  /**
   * @return , never <code>null</code>
   **/
  public VendorDef getBundle();

  /**
   * @return 
   **/
  public Optional<ConfigMapDef> getNodeCreationConfig();

  /**
   * @return 
   **/
  public Optional<FilestoreDef> getFilestore();


}
