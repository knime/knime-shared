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

import java.time.OffsetDateTime;
import org.knime.shared.workflow.def.LinkDef;
import org.knime.shared.workflow.def.NodeContainerMetadataDef;

import org.knime.shared.workflow.def.impl.DefaultWorkflowMetadataDef;
import org.knime.core.util.workflow.def.DefAttribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



/**
 * WorkflowMetadataDef
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 * @since 5.1
 * @noimplement This interface is not intended to be implemented by clients.
 */
@JsonDeserialize(as = DefaultWorkflowMetadataDef.class)
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.interface-config.json"})
public interface WorkflowMetadataDef extends NodeContainerMetadataDef {

	/** Lists the data attributes this interface provides access to by providing a getter for each data attribute. */ 
    public enum Attribute implements DefAttribute {
         /**  
          * author of the workflow or component, user-provided
          *
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link WorkflowMetadataDef#getAuthor} 
          * @since 5.1
          */
         AUTHOR,
         /**  
          * creation date of the metadata
          *
          * The type of this data attribute is {@link OffsetDateTime}.
          * Is is returned by {@link WorkflowMetadataDef#getCreated} 
          * @since 5.1
          */
         CREATED,
         /**  
          * last-modified date of the metadata
          *
          * This is a required field.
          * The type of this data attribute is {@link OffsetDateTime}.
          * Is is returned by {@link WorkflowMetadataDef#getLastModified} 
          * @since 5.1
          */
         LAST_MODIFIED,
         /**  
          * description text of the workflow or component
          *
          * This is a required field.
          * The type of this data attribute is {@link String}.
          * Is is returned by {@link WorkflowMetadataDef#getDescription} 
          */
         DESCRIPTION,
         /**  
          * list of tags
          *
          * The type of this data attribute is java.util.List&lt;String&gt;.
          * Is is returned by {@link WorkflowMetadataDef#getTags} 
          * @since 5.1
          */
         TAGS,
         /**  
          * list of links to additional resources
          *
          * The type of this data attribute is java.util.List&lt;LinkDef&gt;.
          * Is is returned by {@link WorkflowMetadataDef#getLinks} 
          * @since 5.1
          */
         LINKS,
         /**  
          * The content type of the rich-text fields (currently &#x60;description&#x60;, &#x60;inPortDescriptions&#x60; and &#x60;outPortDescriptions&#x60;).
          *
          * This is a required field.
          * The type of this data attribute is {@link ContentTypeEnum}.
          * Is is returned by {@link WorkflowMetadataDef#getContentType} 
          * @since 5.1
          */
         CONTENT_TYPE,
;
    }
    


}
