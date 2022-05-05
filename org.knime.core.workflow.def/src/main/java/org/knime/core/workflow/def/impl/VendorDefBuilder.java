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
package org.knime.core.workflow.def.impl;

import java.util.Map;


// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.VendorDef;
// for types that define enums
import org.knime.core.workflow.def.VendorDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * The author/provider of a native node. Describes bundles and features that contain the code to execute  a native node.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class VendorDefBuilder {

    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree data
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Contains the load exception or load exception tree for each attribute with load exceptions. Def-type objects
     * provide both the data and the exceptions, so the instances referenced in this map are identical to the ones
     * returned by the getters. Non-Def-type objects (that includes lists and maps of Def types) do not provide the
     * {@link LoadException}s associated to their loading. Instead, separate {@link LoadExceptionTree} instances are
     * referenced in this map.
     */
    Map<VendorDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(VendorDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_name;
    

    String m_symbolicName;
    

    String m_vendor;
    

    String m_version;
    

    /**
     * Create a new builder.
     */
    public VendorDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public VendorDefBuilder(final VendorDef toCopy) {
        m_name = toCopy.getName();
        m_symbolicName = toCopy.getSymbolicName();
        m_vendor = toCopy.getVendor();
        m_version = toCopy.getVersion();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for name
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param name For a bundle, see the example value; for a feature it can be an empty string
     * @return this builder for fluent API.
     */ 
    public VendorDefBuilder setName(final String name) {
        setName(() -> name, name);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(VendorDef.Attribute.NAME)} will return true and and
     * {@code getExceptionalChildren().get(VendorDef.Attribute.NAME)} will return the exception.
     * 
     * @param name see {@link VendorDef#getName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setName(String)
     */
    public VendorDefBuilder setName(final FallibleSupplier<String> name, String defaultValue) {
        java.util.Objects.requireNonNull(name, () -> "No supplier for name provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(VendorDef.Attribute.NAME);
        try {
            m_name = name.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_name = defaultValue;
            m_exceptionalChildren.put(VendorDef.Attribute.NAME, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for symbolicName
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param symbolicName For a bundle, see the example value; for a feature it can be an empty string
     * @return this builder for fluent API.
     */ 
    public VendorDefBuilder setSymbolicName(final String symbolicName) {
        setSymbolicName(() -> symbolicName, symbolicName);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(VendorDef.Attribute.SYMBOLIC_NAME)} will return true and and
     * {@code getExceptionalChildren().get(VendorDef.Attribute.SYMBOLIC_NAME)} will return the exception.
     * 
     * @param symbolicName see {@link VendorDef#getSymbolicName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setSymbolicName(String)
     */
    public VendorDefBuilder setSymbolicName(final FallibleSupplier<String> symbolicName, String defaultValue) {
        java.util.Objects.requireNonNull(symbolicName, () -> "No supplier for symbolicName provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(VendorDef.Attribute.SYMBOLIC_NAME);
        try {
            m_symbolicName = symbolicName.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_symbolicName = defaultValue;
            m_exceptionalChildren.put(VendorDef.Attribute.SYMBOLIC_NAME, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for vendor
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param vendor For a bundle, see the example value; for a feature it can be an empty string
     * @return this builder for fluent API.
     */ 
    public VendorDefBuilder setVendor(final String vendor) {
        setVendor(() -> vendor, vendor);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(VendorDef.Attribute.VENDOR)} will return true and and
     * {@code getExceptionalChildren().get(VendorDef.Attribute.VENDOR)} will return the exception.
     * 
     * @param vendor see {@link VendorDef#getVendor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVendor(String)
     */
    public VendorDefBuilder setVendor(final FallibleSupplier<String> vendor, String defaultValue) {
        java.util.Objects.requireNonNull(vendor, () -> "No supplier for vendor provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(VendorDef.Attribute.VENDOR);
        try {
            m_vendor = vendor.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_vendor = defaultValue;
            m_exceptionalChildren.put(VendorDef.Attribute.VENDOR, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for version
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param version For a bundle, see the example value; for a feature, it can be, e.g., \&quot;0.0.0\&quot;
     * @return this builder for fluent API.
     */ 
    public VendorDefBuilder setVersion(final String version) {
        setVersion(() -> version, version);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(VendorDef.Attribute.VERSION)} will return true and and
     * {@code getExceptionalChildren().get(VendorDef.Attribute.VERSION)} will return the exception.
     * 
     * @param version see {@link VendorDef#getVersion}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setVersion(String)
     */
    public VendorDefBuilder setVersion(final FallibleSupplier<String> version, String defaultValue) {
        java.util.Objects.requireNonNull(version, () -> "No supplier for version provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(VendorDef.Attribute.VERSION);
        try {
            m_version = version.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_version = defaultValue;
            m_exceptionalChildren.put(VendorDef.Attribute.VERSION, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link VendorDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultVendorDef build() {
        
    	
        return new DefaultVendorDef(this);
    }    

}
