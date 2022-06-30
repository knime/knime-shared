/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME AG, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 */
package org.knime.shared.codegen;

/**
 * Enumerates and documents all spec-extensions ("x-...") used in particular for the gateway code generation. All
 * extensions are used in the api-specification (.yaml) and will take effect either through additional logic in
 * {@link GatewayCodegen} or the templates (.mustache).
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 */
public final class Extensions {

    private Extensions() {
        // util
    }

    /**
     * @deprecated use {@link Extensions#EXECUTOR_EXCEPTIONS} or {@link Extensions#SERVER_EXCEPTIONS} instead
     */
    @Deprecated
    public static final String EXCEPTIONS = "x-knimegateway-exceptions";

    /**
     * Used in operation-definitions to enumerate all the exceptions that are expected in the executor for a certain
     * endpoints. Those exceptions will be added to the generated method's throws-clause (but only for
     * executor/AP-generated code).
     */
    public static final String EXECUTOR_EXCEPTIONS = "x-knime-gateway-executor-exceptions";

    /**
     * Used in response-definitions which allows to map a http-response (code generated for the server) to an exception
     * by specifying the exceptoins full classname. Those exceptions, e.g., will be added as thrown exceptions to the
     * method signature of the server side endpoint.
     */
    public static final String SERVER_EXCEPTIONS = "x-knime-gateway-server-exceptions";

    /**
     * Used in operations. Allows one to map executor exceptions to server responses (http status code). By default they
     * are otherwise all mapped to on single response (400, GatewayExecutorException).
     */
    public static final String EXECUTOR_EXCEPTION_TO_SERVER_RESPONSE_MAP =
        "x-knime-gateway-executor-exception-to-server-response-map";

    /**
     * Used in schemas to mark an object (e.g. node-id) to be turned into a string-parameter if it's server-generated
     * code. This avoids the need to reference, e.g., the NodeIDEnt-class, which is not available on the server.
     */
    public static final String SERVER_PARAM_AS_STRING = "x-knime-gateway-server-param-as-string";

    /**
     * Used in schemas to mark an object being used as a parameter of a method (and not only as a (indirectly) returned
     * object).
     */
    public static final String IS_USED_AS_PARAMETER = "x-knimegateway-isUsedAsParameter";

    /**
     * Used with the 'oneOf'-property. Tells the code generator that another schema, e.g. 'Node', is the super-type of
     * the schemas listed as 'oneOf' (e.g. MetaNode, Component, NativeNode).
     */
    public static final String CODEGEN_PARENT = "x-knime-gateway-codegen-parent";

    /**
     * Used in properties. It tells the code-generator to narrow the return-type of a method by 'overwriting' the same
     * property in an derived schema ('allOf') with a narrowed property-type.
     */
    public static final String ALLOW_SUBTYPES = "x-knime-gateway-allowsubtypes";

    /**
     * Used in operations for server-code generation. Allows one to manually intercept the request on the server in
     * order, e.g., to customize the response. In a 'methodToCall' object the full name of a static function is defined.
     */
    public static final String CUSTOM_SERVER_IMPLEMENTATION = "x-knime-gateway-custom-server-implementation";

    /**
     * Used in a response-schema (for server-code generation). It's a response object that won't change and, e.g., gets
     * a Last-Modified header set.
     */
    public static final String STATIC_RESPONSE = "x-knime-gateway-static-response";

    /**
     * Used in a response schema (for server-code generation). Those responses will be transfered as binary json between
     * the server and executor.
     */
    public static final String IS_BINARY_RESPONSE = "x-knime-gateway-binary-response";

    /**
     * Used in an operation schema for server-code generation. Operations marked with this extension won't trigger a
     * job-swap back to the executor. If a job is swapped, the operation won't be carried out and an
     * 'executor-exception' (provided by the extension) returned instead.
     */
    public static final String EXCEPTION_IF_JOB_IS_SWAPPED = "x-knime-gateway-executor-exception-if-job-is-swapped";

    /**
     * Most def types implement LoadExceptionTree<K> to provide access to LoadException associated to their members.
     * However, for union types (e.g., a Node is either a NativeNode, a Component, or a Metanode) the superclass is only
     * a marker and should not implement LoadExceptionTree. The reason is that the type parameter of LoadExceptionTree
     * should be the enum that lists the attributes of that particular class, e.g., for NativeNodeDef.Attribute. If the
     * superclass would also implement LoadExceptionTree (e.g., with type parameter NodeDef.Attribute) we would get an
     * interface implementation clash - cannot implement the same interface with different type parameters.
     */
    public static final String DEF_MARKER_INTERFACE = "x-knime-marker-interface";
}
