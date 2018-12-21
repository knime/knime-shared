# KNIME Gateway Code Generation

This projects contains the definitions and logic to auto-generate source code using [swagger-codegen](https://github.com/swagger-api/swagger-codegen).

### Structure

The definitions, templates and configurations for the code-generation are contained in the **/src-gen** folder.

* definitions (**/src-gen/api**): yaml-swagger files mainly defining entities (aka messages, structs, etc.) and services (aka operations, endpoints, etc.). Currently using [Swagger 2.0](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md).
* templates (**/src-gen/templates**): [mustache-templates](https://mustache.github.io/) used for the actual code-generation and referenced from the configuration-files (see below) or other template-files
* configurations (**/src-gen/*-config.json**): json-configuration files to config the actual code-generation process that are essentially deserialized into a Swagger-[CodegenConfigurator](https://github.com/swagger-api/swagger-codegen/blob/master/modules/swagger-codegen/src/main/java/io/swagger/codegen/config/CodegenConfigurator.java)-object. Each config-file defines a new set of source-files to be generated.

The logic (java) to perform the actual code generation can be found in **/src/main/java**.

### Code Generation

In order to trigger the code-generation the **org.knime.gateway.codegen.Generate**-class needs to be run as a java-app. It reads all _/src-gen/*-config.json_-files and generates (and potentially overrides) the respective source files.
The source files will only be generated if the defined output-directory (outputDir-property) relative to this project exists. Otherwise the code-generation for the particular config-file will be skipped (see log output).