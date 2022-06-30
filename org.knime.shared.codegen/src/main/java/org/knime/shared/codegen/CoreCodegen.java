/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 * History
 *   11 Feb 2022 (carlwitt): created
 */
package org.knime.shared.codegen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.ModelUtils;

import io.swagger.v3.oas.models.media.Schema;

/**
 * Custom changes for generating source code for workflow format description classes.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public class CoreCodegen extends GatewayCodegen {

    /**
     * TODO the proper way would to to detect for x-knime-marker-interface
     */
    private static final Set<String> abstractModels = new HashSet<>();
    static {
        abstractModels.add("BaseNodeDef");
        abstractModels.add("ConfigDef");
    }

    /**
     * The default value is directly inserted into the code, e.g., variable = 5 or variable = John For numeric literals
     * this is fine, but Strings need quotes around them.
     */
    @Override
    public String toDefaultValue(final Schema p) {
        if (ModelUtils.isStringSchema(p)) {
            if (p.getDefault() != null) {
                return "\"" + (String)p.getDefault() + "\"";
            }
        }
        return super.toDefaultValue(p);
    }

    /**
     * Set the {@code isInherited} flag on all properties - it is always false by default. {@inheritDoc}
     * @param objs one type (e.g., Workflow, Connection, PortType, etc.) at a time
     */
    @Override
    public Map<String, Object> postProcessModels(final Map<String, Object> objs) {
        Map<String, Object> processedObjs = super.postProcessModels(objs);
        List<?> models = (List<?>)objs.get("models");

        CodegenModel model = (CodegenModel)((Map<?, ?>)models.get(0)).get("model");

        // the names of the properties that are declared directly by the model (not inherited)
        Set<String> varNames = model.vars.stream()//
            .map(CodegenProperty::getBaseName)//
            .collect(Collectors.toSet());

        for (CodegenProperty allVar : model.allVars) {
            // a property that is in allVars but not in vars is inherited
            if (!varNames.contains(allVar.getBaseName())) {
                // works because it refers to the same instance as the referenced in vars
                allVar.isInherited = true;
            }
        }
        return processedObjs;
    }

    /**
     * Fix snake case names of properties. {@inheritDoc}
     */
    @Override
    public CodegenProperty fromProperty(final String name, final Schema p) {
        CodegenProperty property = super.fromProperty(name, p);

        // we want to generate exception accessors only for the to Fallible*Def children of Fallible*Def
        // non-primitive types also include OffsetDateTime, for those attributes we don't generate an exception accessor,
        // e.g., getLastUpdatedException() whereas for def
        if (!property.isPrimitiveType) {
            final boolean baseTypeIsDef = property.baseType != null && property.baseType.endsWith("Def");
            // e.g., if the type is a List<PortDef>, the complex type is PortDef
            final boolean complexTypeIsDef = property.complexType != null && property.complexType.endsWith("Def");
            final boolean isDef = baseTypeIsDef || complexTypeIsDef;
            property.vendorExtensions.put("isDef", isDef);
            property.vendorExtensions.put("isSingleDef", isDef && !property.isContainer);
        }

        // usually, nameInSnakeCase contains the upper snake case name, e.g., TABLE_BACKEND_SETTINGS, but sometimes,
        // it is null and needs to be set. Also, the default value is sometimes wrong, e.g., inPortsBarUiInfo -> IN_PORTS_BAR_U_I_INFO
        property.nameInSnakeCase = nameToEnumName(name);

        final String nameInCamelCase;
        if (property.nameInCamelCase.length() > 0) {
            nameInCamelCase = property.nameInCamelCase;
        } else {
            nameInCamelCase = property.complexType;
            System.err.println("property.nameInCamelCase is zero-length string for property: " + property);
        }
        String allCamel =
            nameInCamelCase.substring(0, 1).toUpperCase() + nameInCamelCase.substring(1);
        property.vendorExtensions.put("allCamel", allCamel);

        // we'd prefer boolean (can't be null) over Boolean for required (non-null) properties that are not lists/maps
//        if("Boolean".equals(property.datatypeWithEnum)) {
            // somehow this is always false - even for required properties
//            if (property.required) {
//                if (!property.isContainer) {
//                    property.datatypeWithEnum = "boolean";
//                }
//            }
//        }

        return property;
    }

    /**
     * @param name a property name such as "workflow" or "tableBackendSettings"
     * @return name in upper snake case, e.g., "WORKFLOW" or "TABLE_BACKEND_SETTINGS"
     */
    private static String nameToEnumName(final String name) {
        return Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(name))//
            .collect(Collectors.joining("_"))//
            .toUpperCase();
    }

    /**
     * Create a putter method signature for putting an entry into the map. {@inheritDoc}
     */
    @Override
    protected void updatePropertyForMap(final CodegenProperty property, final CodegenProperty innerProperty) {
        super.updatePropertyForMap(property, innerProperty);
        if (m_modelPropertyNamePattern != null) {
            property.dataType = property.datatypeWithEnum = "java.util.Map<String, " + innerProperty.dataType + ">";
            // putter - similar to setter, just don't set the entire map but put an entry into it.
            // the type of the element that is to be added as value to the map
            property.vendorExtensions.put("valueType", innerProperty.baseType);
        }
        property.vendorExtensions.put("emptyContainer", "java.util.Map.of()");
        property.vendorExtensions.put("newContainer", "new java.util.HashMap<>()");
        property.vendorExtensions.put("containerType", "map");
        // maps are created via "additionalProperties" element in the schema and always have type Map<String, *>
        property.vendorExtensions.put("containerKeyType", "String");
        property.vendorExtensions.put("extend", "put");
        property.vendorExtensions.put("isAbstract", abstractModels.contains(property.getComplexType()));

    }

    /**
     * Create an adder method signature for adding an entry into a map. {@inheritDoc}
     */
    @Override
    protected void updatePropertyForArray(final CodegenProperty property, final CodegenProperty innerProperty) {
        super.updatePropertyForArray(property, innerProperty);
        if (property.getVendorExtensions().containsKey(Extensions.ALLOW_SUBTYPES)) {
            property.dataType = property.datatypeWithEnum = "java.util.List<? extends " + innerProperty.dataType + ">";
        } else if (m_modelPropertyNamePattern != null) {
            property.dataType = property.datatypeWithEnum = "java.util.List<" + innerProperty.dataType + ">";
            // adder - similar to setter, just don't set the entire list but put an element into it.
            // the type of the element that is to be added to the list
            property.vendorExtensions.put("valueType", innerProperty.baseType);
        } else {
            //
        }
        property.vendorExtensions.put("emptyContainer", "java.util.List.of()");
        property.vendorExtensions.put("newContainer", "new java.util.ArrayList<>()");
        property.vendorExtensions.put("containerType", "list");
        property.vendorExtensions.put("containerKeyType", "Integer");
        property.vendorExtensions.put("extend", "add");
        property.vendorExtensions.put("isAbstract", abstractModels.contains(property.getComplexType()));
    }

    @Override
    public String getName() {
        // TODO pull this from additional properties
//      System.err.println("additional properties: " + additionalProperties);
        return "sharedCodegen";
//      return additionalProperties().get("codegenName").toString();
    }
}
