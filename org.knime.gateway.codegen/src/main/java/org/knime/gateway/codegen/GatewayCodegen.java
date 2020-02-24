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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.openapitools.codegen.CodegenModelFactory;
import org.openapitools.codegen.CodegenModelType;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.AbstractJavaCodegen;
import org.openapitools.codegen.utils.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;

/**
 * Service and entity-class/interface generation for the KNIME gateway based on
 * a swagger/openapi spec.
 *
 * @author Martin Horn, University of Konstanz
 */
public class GatewayCodegen extends AbstractJavaCodegen {

	private static final String NAME_PLACEHOLDER = "##name##";

    static Logger LOGGER = LoggerFactory.getLogger(GatewayCodegen.class);

	private final Map<String, String> m_tagDescriptions = new HashMap<String, String>();

    private final Map<String, List<Map<String, String>>> m_operationExecutorExceptions =
        new HashMap<String, List<Map<String, String>>>();

    private final Map<String, List<Map<String, String>>> m_operationServerExceptions =
        new HashMap<String, List<Map<String, String>>>();

	private String m_apiTemplateFile;

	private String m_modelNamePattern;

	private String m_apiNamePattern;

	private String m_modelPropertyNamePattern;

	private String m_modelPropertyPackage;

    private Map<String, Schema> m_schemas;

	@Override
	public void processOpts() {
		super.processOpts();

		setBooleanGetterPrefix("is");

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

        //TODO set to, e.g., 'InputStream' when streaming is supported throughout the whole stack
        //(i.e. in client, server and executor)
        typeMapping.put("file", "byte[]");
    }

	@Override
    public String getTypeDeclaration(final Schema p) {
        if ("node-id".equals(p.getFormat())) {
            return "com.knime.gateway.entity.NodeIDEnt";
        } else if ("connection-id".equals(p.getFormat())) {
            return "com.knime.gateway.entity.ConnectionIDEnt";
        } else if ("annotation-id".equals(p.getFormat())) {
            return "com.knime.gateway.entity.AnnotationIDEnt";
        } else {
            return super.getTypeDeclaration(p);
        }
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

		// add exceptions thrown by the operation
        co.vendorExtensions.put("x-knime-gateway-codegen-executor-exceptions",
            m_operationExecutorExceptions.get(operation.getOperationId()));
        co.vendorExtensions.put("x-knime-gateway-codegen-server-exceptions",
            m_operationServerExceptions.get(operation.getOperationId()));

        //set 'isResponseBinary' flag if a json schema returned at 200
        //has the 'x-knime-gateway-binary-response' extension
        //-> those responses will be transfered as binary json between
        //the server and the executor
        ApiResponse apiResponse = operation.getResponses().get("200");
        if (apiResponse != null) {
            Content content = apiResponse.getContent();
            if (content != null) {
                MediaType mediaType = content.get("application/json");
                if (mediaType != null) {
                    String ref = mediaType.getSchema().get$ref();
                    if (ref != null) {
                        Map extensions = m_schemas.get(extractNameFromRef(ref)).getExtensions();
                        if (extensions != null) {
                            Object extension = extensions.get("x-knime-gateway-binary-response");
                            if (Boolean.TRUE.equals(extension)) {
                                co.isResponseBinary = true;
                            }
                        }
                    }
                }
                mediaType = content.get("*/*");
                if (mediaType != null) {
                    Schema schema = mediaType.getSchema();
                    if (schema != null) {
                        Map extensions = schema.getExtensions();
                        if (extensions != null) {
                            Object extension = extensions.get("x-knime-gateway-static-response");
                            if (Boolean.TRUE.equals(extension)) {
                                co.vendorExtensions.put("x-knime-gateway-static-response", true);
                            }
                        }
                    }
                }

            }
        }
    	super.addOperationToGroup(tag, resourcePath, operation, co, operations);
	}

	@Override
	public Map<String, Object> postProcessOperations(final Map<String, Object> objs) {
		// provide tag (i.e. service) descriptions
		// TODO is there a better way
		String tagDesc = m_tagDescriptions.get(((Map<String, Object>) objs.get("operations")).get("pathPrefix"));
		if ((tagDesc != null)) {
			objs.put("x-knime-gateway-codegen-tagDescription", tagDesc);
		}

		return super.postProcessOperations(objs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean needToImport(final String type) {
        if (type.contains("GatewayException")) {
            return false;
        } else {
            return super.needToImport(type);
        }
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
			return m_apiNamePattern.replace(NAME_PLACEHOLDER, camelize(name));
		} else {
			return name;
		}
	}

	@Override
	public String toModelName(final String name) {
		if (m_modelNamePattern != null) {
			return m_modelNamePattern.replace(NAME_PLACEHOLDER, name);
		} else {
			return name;
		}
	}

    @Override
    public String toBooleanGetter(final String name) {
        if (name.startsWith("has")) {
            return "has" + getterAndSetterCapitalize(name.substring(3));
        } else {
            return super.toBooleanGetter(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toDefaultValue(final Schema p) {
        if (ModelUtils.isStringSchema(p)) {
            if (p.getDefault() != null) {
                return (String)p.getDefault();
            }
        }
        return super.toDefaultValue(p);
    }

	@Override
	public CodegenProperty fromProperty(final String name, final Schema p) {
		// enables properties to have another name then the property they are
		// part of
		// e.g. DefaultNodeEnt.getNodeMessage() returns NodeMessageEnt instead
		// of DefaultNodeMessageEnt
		if (m_modelPropertyNamePattern != null && p.get$ref() != null) {
			CodegenProperty property = CodegenModelFactory.newInstance(CodegenModelType.PROPERTY);
			property.name = toVarName(name);
			property.baseName = name;
			property.nameInCamelCase = camelize(property.name, false);
			property.description = escapeText(p.getDescription());
			property.unescapedDescription = p.getDescription();
			property.title = p.getTitle();
			property.getter = toGetter(name);
			property.setter = toSetter(name);
			property.baseType = property.dataType = property.datatypeWithEnum = m_modelPropertyNamePattern
					.replace(NAME_PLACEHOLDER, extractNameFromRef(p.get$ref()));
			importMapping.put(property.dataType, m_modelPropertyPackage + "." + property.dataType);
			return property;
		} else {
			return super.fromProperty(name, p);
		}
	}

    private String extractNameFromRef(final String ref) {
        return ref.substring(ref.lastIndexOf("/") + 1);
    }

	@Override
	protected void updatePropertyForMap(final CodegenProperty property, final CodegenProperty innerProperty) {
		if (m_modelPropertyNamePattern != null) {
			// TODO hacky but works!
			super.updatePropertyForMap(property, innerProperty);
			property.dataType = property.datatypeWithEnum = "java.util.Map<String, " + innerProperty.dataType + ">";
		} else {
			super.updatePropertyForMap(property, innerProperty);
		}
	}

	@Override
	protected void updatePropertyForArray(final CodegenProperty property, final CodegenProperty innerProperty) {
		if (m_modelPropertyNamePattern != null) {
			// TODO hacky but works!
			super.updatePropertyForArray(property, innerProperty);
			property.dataType = property.datatypeWithEnum = "java.util.List<" + innerProperty.dataType + ">";
		} else {
			super.updatePropertyForArray(property, innerProperty);
		}
	}

    @Override
    public void preprocessOpenAPI(final OpenAPI openAPI) {
        super.preprocessOpenAPI(openAPI);

        Map<String, Map<String, String>> executorExceptions = collectExecutorExceptions(openAPI);

        List<Operation> operations = openAPI.getPaths().values().stream()
            .flatMap(pi -> Arrays.asList(pi.getGet(), pi.getPut(), pi.getPost(), pi.getDelete(), pi.getHead()).stream())
            .filter(o -> o != null).collect(Collectors.toList());
        operations.forEach(o -> {
            resolveAndCollectExecutorExceptionsForOperation(o, executorExceptions);
            collectServerExceptionsForOperation(o);
        });
        m_schemas = openAPI.getComponents().getSchemas();
    }

    /**
     * Collect all 'x-knime-gateway-executor-exceptions' extensions of possible responses and make them globally
     * available.
     */
    private Map<String, Map<String, String>> collectExecutorExceptions(final OpenAPI openAPI) {
        final Map<String, Map<String, String>> executorExceptions = (Map<String, Map<String, String>>)openAPI
            .getComponents().getExtensions().get("x-knime-gateway-executor-exceptions");
        additionalProperties().put("x-knime-gateway-codegen-executor-exceptions", executorExceptions.values());
        return executorExceptions;
    }

    /**
     * Resolve and collect the executor exceptions from the respective extension (x-...) for an operation. Required for
     * easier access the in the code templates later on.
     *
     * Directly manipulates the passed operation in order to set the resolved references!!
     */
    private void resolveAndCollectExecutorExceptionsForOperation(final Operation op,
        final Map<String, Map<String, String>> executorExceptions) {
        //collect the executor exception from the operation's 'x-knime-gateway-executor-exceptions'-extension
        List<Map<String, String>> executorExceptionsRefs;
        if (op.getExtensions() != null && (executorExceptionsRefs =
            (List<Map<String, String>>)op.getExtensions().get("x-knime-gateway-executor-exceptions")) != null) {
            //replace references with referees
            List<Map<String, String>> replaced = new ArrayList<Map<String, String>>();
            executorExceptionsRefs.forEach(ref -> {
                String refName = extractNameFromRef(ref.get("$ref"));
                Map<String, String> ee = executorExceptions.get(refName);
                if (ee == null) {
                    throw new IllegalStateException("Broken gateway-executor-exception reference: " + refName);
                }
                replaced.add(ee);
                m_operationExecutorExceptions.computeIfAbsent(op.getOperationId(), k -> new ArrayList<>()).add(ee);
            });
            op.getExtensions().put("x-knime-gateway-executor-exceptions", replaced);
        }

        //make exception to response mapping available globally
        List<Map<String, String>> exceptionToResponseMapExt;
        if (op.getExtensions() != null && (exceptionToResponseMapExt = (List<Map<String, String>>)op.getExtensions()
            .get("x-knime-gateway-executor-exception-to-server-response-map")) != null) {
            List list = (List)additionalProperties()
                .computeIfAbsent("x-knime-gateway-executor-exception-to-server-response-map", k -> new ArrayList());
            exceptionToResponseMapExt.forEach(m -> {
                Map<String, String> item = new HashMap<>();
                item.put("executor-exception", m.get("executor-exception"));
                item.put("server-response", m.get("server-response"));
                item.put("service", op.getTags().get(0));
                item.put("method", op.getOperationId());
                list.add(item);
            });
        }
    }

    /**
     * Extract and collect the server exceptions from the respective response for an operation. Required for easier
     * access the in the code templates later on.
     */
    private void collectServerExceptionsForOperation(final Operation op) {
        //collect the server exceptions from the responses
        for (Entry<String, ApiResponse> res : op.getResponses().entrySet()) {
            String code = res.getKey();
            Map<String, Object> extensions = res.getValue().getExtensions();
            if (extensions != null) {
                List<Map<String, String>> serverExceptions =
                    (List<Map<String, String>>)extensions.get("x-knime-gateway-server-exceptions");
                if (serverExceptions != null) {
                    serverExceptions.forEach(e -> {
                        if (!e.containsKey("description")) {
                            e.put("description", res.getValue().getDescription());
                        }
                        Map<String, String> copy = new HashMap<>(e);
                        copy.put("code", code);
                        m_operationServerExceptions.computeIfAbsent(op.getOperationId(), k -> new ArrayList<>())
                            .add(copy);
                    });
                }
            }
        }
    }
}
