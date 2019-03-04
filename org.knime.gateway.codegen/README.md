# KNIME Gateway Code Generation

This projects contains the definitions and logic to auto-generate source code using [swagger-codegen](https://github.com/swagger-api/swagger-codegen).

## Structure

The definitions, templates and configurations for the code-generation are contained in the **/src-gen** folder.

* definitions (**/src-gen/api**): yaml-swagger files mainly defining entities (aka messages, structs, etc.) and services (aka operations, endpoints, etc.). Currently using [OpenAPI 3.0](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md).
* templates (**/src-gen/templates**): [mustache-templates](https://mustache.github.io/) used for the actual code-generation and referenced from the configuration-files (see below) or other template-files
* configurations (**/src-gen/*-config.json**): json-configuration files to config the actual code-generation process that are essentially deserialized into a Swagger-[CodegenConfigurator](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/java/org/openapitools/codegen/config/CodegenConfigurator.java)-object. Each config-file defines a new set of source-files to be generated.

The logic (java) to perform the actual code generation can be found in **/src/main/java**.

## Code Generation

In order to trigger the code-generation the **org.knime.gateway.codegen.Generate**-class needs to be run as a java-app. It reads all _/src-gen/\*-config.json_-files and generates (and potentially overrides) the respective source files.  
The source files will only be generated if the defined output-directory (outputDir-property) relative to this project exists. Otherwise the code-generation for the particular config-file will be skipped (see log output).  
Code is generated for the three components:

* Analytics Platform (rest-client endpoints and entities)
* Server (rest-server endpoints)
* Executor (json-rpc server endpoints and entities)

## Gateway API Versioning

The Gateway API is not versioned individually.  
Justification:

* it is not public API
* future JavaScript-clients (e.g. wizard executor and workflow editor) will always be developed against the most recent API version
* Analytics Platform and its Remote Workflow Editor is the only component that can lag behind -> backward compatibility is achieved by either adding (and deprecating) endpoints or by modifying entities in the Server (see below) based on the *server version*.

## Backward compatibility of Gateway API in Server with Analytics Platform

### Backward compatibility in case of changes to gateway entities

One important assumption here:
Executor and Server are always of compatible versions (e.g. the most recent one) and don't need to be backward/forward compatible

Properties can be added to entities without further actions required. The AP as client just ignores those.

In order to make other changes to gateway entities (moved/removed properties, changed property types etc.) backward compatible, the changes need to be handled/compensated in the server which acts as the mediator between the AP (client) and the Executor. Consequently, changes to entities are represented in the server by JSON-transformations (e.g. by using a library such as [Jolt](https://github.com/bazaarvoice/jolt)) and encompass all changes made to gateway-entities between different released server versions. The JSON-transformations are applied to the JSON-RPC requests and responses that are exchanged between the server and the executor.
See, e.g., `com.knime.enterprise.gateway.rest.impl.AbstractGatewayRESTService` as starting point.

If changes are too intrusive, a new endpoint must be created instead! 

### Backward compatibility in case of changes to gateway endpoints

Few changes to endpoints (e.g. the removal/addition of an unimportant query parameter, renaming of an operation-id, etc.) might be able to be compensated with the modification of the json-rpc responses and requests (see above).

Otherwise, a new endpoint needs to be created.

If server or executor _exceptions_ are added, no further action is required. The exception is just never thrown anymore in the AP (and server won't no more return the respective response code).  
If server or executor exceptions are removed (i.e. including a new possible response code) a generic `com.knime.gateway.service.ServiceException` is thrown in the AP, which is fine (yet not ideal).

### If backward compatibility cannot be ensured anymore

If backward compatibility cannot be established anymore, compatibility of the of the AP-client need to be explicitly bounded to a range of server-versions in order to be able to give reasonable user feedback.  

The AP can be bounded by specifying maximum and minimum server versions in `com.knime.explorer.server.internal.view.actions.jobworkflow.OpenJobWorkflowUtil`.

Note: the compatibility-check of the AP with the server could also be done in the server. However, I refrained from doing it because it would require to add another error response (e.g. 410 and a IncompatibleServerException) to each endpoint which would unnecessarily pollute the API and generated code.