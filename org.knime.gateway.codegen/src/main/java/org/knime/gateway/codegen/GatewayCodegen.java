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
 */
package org.knime.gateway.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.codegen.CodegenModelFactory;
import io.swagger.codegen.CodegenModelType;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.AbstractJavaCodegen;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

/**
 * Service and entity-class/interface generation for the KNIME gateway based on
 * a swagger/openapi spec.
 *
 * @author Martin Horn, University of Konstanz
 */
public class GatewayCodegen extends AbstractJavaCodegen {

	static Logger LOGGER = LoggerFactory.getLogger(GatewayCodegen.class);

	private final Map<String, String> m_tagDescriptions = new HashMap<String, String>();

	private String m_apiTemplateFile;

	private String m_modelNamePattern;

	private String m_apiNamePattern;

	private String m_modelPropertyNamePattern;

	private String m_modelPropertyPackage;

	@Override
	public void processOpts() {
		super.processOpts();
		apiTemplateFiles.clear();
		m_apiTemplateFile = getPropertyAsString("apiTemplateFile").orElse(null);
		if (m_apiTemplateFile != null) {
			apiTemplateFiles.put(m_apiTemplateFile, ".java");
		}
		modelTemplateFiles.clear();
		if (additionalProperties().get("modelTemplateFile") != null) {
			modelTemplateFiles.put(additionalProperties().get("modelTemplateFile").toString(), ".java");
		}
		apiTestTemplateFiles.clear();
		modelTestTemplateFiles.clear();
		apiDocTemplateFiles.clear();
		modelDocTemplateFiles.clear();

		// property options
		m_modelNamePattern = getPropertyAsString("modelNamePattern").orElse(null);
		m_apiNamePattern = getPropertyAsString("apiNamePattern").orElse(null);
		m_modelPropertyNamePattern = getPropertyAsString("modelPropertyNamePattern").orElse(null);
		m_modelPropertyPackage = getPropertyAsString("modelPropertyPackage").orElse(null);

		// supporting files
		getPropertyAsList("supportingFiles").ifPresent(l -> l.stream().forEach(sf -> {
			Map<String, Object> sfmap = (Map<String, Object>) sf;
			final String folder = (sourceFolder + '/' + sfmap.get("package").toString()).replace(".", "/");
			supportingFiles.add(new SupportingFile(sfmap.get("templateFile").toString(), folder,
					sfmap.get("destinationFileName").toString()));
			sfmap.keySet().forEach(k -> {
				additionalProperties.put(sfmap.get("templateFile").toString() + "#package",
						sfmap.get("package").toString());
			});

		}));
	}

	private Optional<String> getPropertyAsString(final String propName) {
		return Optional.ofNullable(additionalProperties().get(propName)).map(o -> o.toString());
	}

	private Optional<List<Object>> getPropertyAsList(final String propName) {
		return Optional.ofNullable(additionalProperties.get(propName)).map(o -> List.class.cast(o));
	}

	@Override
	public String getName() {
		return additionalProperties().get("codegenName").toString();
	}

	@Override
	public CodegenType getTag() {
		return CodegenType.OTHER;
	}

	@Override
	public String getHelp() {
		return "TODO";
	}

	@Override
	public Map<String, Object> postProcessModels(final Map<String, Object> objs) {
		// Remove imports ApiModel and ApiModelProperty
		List<Map<String, String>> imports = (List<Map<String, String>>) objs.get("imports");
		Pattern pattern = Pattern.compile(".*(ApiModel|ApiModelProperty)");
		for (Iterator<Map<String, String>> itr = imports.iterator(); itr.hasNext();) {
			String _import = itr.next().get("import");
			if (pattern.matcher(_import).matches()) {
				itr.remove();
			}
		}
		return super.postProcessModels(objs);
	}

	@Override
	public void addOperationToGroup(final String tag, final String resourcePath, final Operation operation,
			final CodegenOperation co, final Map<String, List<CodegenOperation>> operations) {
		// makes the tag descriptions available to the mustache templates
		// (e.g. to be used in the javadoc of the service interfaces)
		// TODO is there a better way?
		String tagDesc = null;
		for (int i = 0; i < co.tags.size(); i++) {
			if (co.tags.get(i).getName().equals(tag)) {
				tagDesc = co.tags.get(i).getDescription();
				break;
			}
		}
		if (tagDesc != null) {
			m_tagDescriptions.put(tag.toLowerCase(), tagDesc);
		}
		super.addOperationToGroup(tag, resourcePath, operation, co, operations);
	}

	@Override
	public Map<String, Object> postProcessOperations(final Map<String, Object> objs) {
		// provide tag (i.e. service) descriptions
		// TODO is there a better way
		String tagDesc = m_tagDescriptions.get(((Map<String, Object>) objs.get("operations")).get("pathPrefix"));
		if ((tagDesc != null)) {
			objs.put("tagDescription", tagDesc);
		}

		// collect and provide the exceptions for each operation

		return super.postProcessOperations(objs);
	}

	@Override
	public String toApiName(final String name) {
		// make original name available to templates
		// these form the basis of the service names, e.g. WorkflowService,
		// NodeService
		if (additionalProperties().get("tags") == null) {
			additionalProperties().put("tags", new HashSet<String>());
		} else {
			Set<String> tags = (Set<String>) additionalProperties().get("tags");
			tags.add(name);
		}
		if (m_apiNamePattern != null) {
			return m_apiNamePattern.replace("##name##", camelize(name));
		} else {
			return name;
		}
	}

	@Override
	public String toModelName(final String name) {
		if (m_modelNamePattern != null) {
			return m_modelNamePattern.replace("##name##", name);
		} else {
			return name;
		}
	}

	@Override
	public CodegenProperty fromProperty(final String name, final Property p) {
		// enables properties to have another name then the property they are
		// part of
		// e.g. DefaultNodeEnt.getNodeMessage() returns NodeMessageEnt instead
		// of DefaultNodeMessageEnt
		if (m_modelPropertyNamePattern != null && p instanceof RefProperty) {
			CodegenProperty property = CodegenModelFactory.newInstance(CodegenModelType.PROPERTY);
			property.name = toVarName(name);
			property.baseName = name;
			property.nameInCamelCase = camelize(property.name, false);
			property.description = escapeText(p.getDescription());
			property.unescapedDescription = p.getDescription();
			property.title = p.getTitle();
			property.getter = toGetter(name);
			property.setter = toSetter(name);
			property.baseType = property.datatype = property.datatypeWithEnum = m_modelPropertyNamePattern
					.replace("##name##", ((RefProperty) p).getSimpleRef());
			importMapping.put(property.datatype, m_modelPropertyPackage + "." + property.datatype);
			return property;
		} else {
			return super.fromProperty(name, p);
		}
	}

	@Override
	protected void updatePropertyForMap(final CodegenProperty property, final CodegenProperty innerProperty) {
		if (m_modelPropertyNamePattern != null) {
			// TODO hacky but works!
			super.updatePropertyForMap(property, innerProperty);
			property.datatype = property.datatypeWithEnum = "java.util.Map<String, " + innerProperty.datatype + ">";
		} else {
			super.updatePropertyForMap(property, innerProperty);
		}
	}

	@Override
	protected void updatePropertyForArray(final CodegenProperty property, final CodegenProperty innerProperty) {
		if (m_modelPropertyNamePattern != null) {
			// TODO hacky but works!
			super.updatePropertyForArray(property, innerProperty);
			property.datatype = property.datatypeWithEnum = "java.util.List<" + innerProperty.datatype + ">";
		} else {
			super.updatePropertyForArray(property, innerProperty);
		}
	}

	@Override
	public void preprocessSwagger(final Swagger swagger) {
		super.preprocessSwagger(swagger);

		// Collect all pre-defined exceptions (x-knimegateway-exceptions section
		// in the swagger
		// file) - a 'vendor extension'
		Map<String, Object> executorExceptions = (Map<String, Object>) ((Map<String, Object>) swagger
				.getVendorExtensions().get("x-knimegateway-exceptions")).get("executor");
		Map<String, Object> serverExceptions = (Map<String, Object>) ((Map<String, Object>) swagger
				.getVendorExtensions().get("x-knimegateway-exceptions")).get("server");

		// TODO null-check for required properties
		additionalProperties().put("executorExceptions",
				executorExceptions.values().stream().collect(Collectors.toList()));
		additionalProperties().put("serverExceptions", serverExceptions.values().stream().collect(Collectors.toList()));

		// 'manually' map x-knimegateway-exceptions cross-references since
		// swagger doesn't support cross-references for vendor extensions
		// there might be a more elegant way ...
		swagger.getPaths().values().stream().flatMap(p -> p.getOperations().stream()).forEach(op -> {
			resolveExceptionReference("executor", op, executorExceptions);
			resolveExceptionReference("server", op, serverExceptions);
		});
	}

	private void resolveExceptionReference(final String location, final Operation operation, final Map<String, Object> exceptions) {
		Map<String, Object> locations = (Map<String, Object>) operation.getVendorExtensions()
				.get("x-knimegateway-exceptions");
		if (locations != null) {
			List<Object> opExceptions = (List<Object>) locations.get(location);
			if (opExceptions != null) {
				// replace reference by predefined objects (in case it's a
				// reference)
				for (int i = 0; i < opExceptions.size(); i++) {
					Map<String, Object> ref = (Map<String, Object>) opExceptions.get(i);
					Object opException = ref.get("$ref");
					if (opException != null && opException instanceof String) {
						String key = (String) opException;
						String prefix = "#/x-knimegateway-exceptions/" + location + "/";
						if (key.startsWith(prefix)) {
							Object o2 = exceptions.get(key.substring(prefix.length()));
							if (o2 != null) {
								opExceptions.set(i, o2);
							} else {
								throw new RuntimeException("Reference '" + key + "' cannot be resolved.");
							}

						}
					}
				}
			}
		}
	}
}
