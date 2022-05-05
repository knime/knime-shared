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
import org.knime.core.workflow.def.PortTypeDef;
// for types that define enums
import org.knime.core.workflow.def.PortTypeDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * PortTypeDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class PortTypeDefBuilder {

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
    Map<PortTypeDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(PortTypeDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    String m_portObjectClass;
    

    String m_portObjectSpecClass;
    

    Integer m_color;
    

    Boolean m_hidden;
    

    Boolean m_optional;
    

    String m_name;
    

    /**
     * Create a new builder.
     */
    public PortTypeDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public PortTypeDefBuilder(final PortTypeDef toCopy) {
        m_portObjectClass = toCopy.getPortObjectClass();
        m_portObjectSpecClass = toCopy.getPortObjectSpecClass();
        m_color = toCopy.getColor();
        m_hidden = toCopy.isHidden();
        m_optional = toCopy.isOptional();
        m_name = toCopy.getName();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for portObjectClass
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param portObjectClass the class of the port object this port type is associated with
     * @return this builder for fluent API.
     */ 
    public PortTypeDefBuilder setPortObjectClass(final String portObjectClass) {
        setPortObjectClass(() -> portObjectClass, portObjectClass);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortTypeDef.Attribute.PORT_OBJECT_CLASS)} will return true and and
     * {@code getExceptionalChildren().get(PortTypeDef.Attribute.PORT_OBJECT_CLASS)} will return the exception.
     * 
     * @param portObjectClass see {@link PortTypeDef#getPortObjectClass}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setPortObjectClass(String)
     */
    public PortTypeDefBuilder setPortObjectClass(final FallibleSupplier<String> portObjectClass, String defaultValue) {
        java.util.Objects.requireNonNull(portObjectClass, () -> "No supplier for portObjectClass provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortTypeDef.Attribute.PORT_OBJECT_CLASS);
        try {
            m_portObjectClass = portObjectClass.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_portObjectClass = defaultValue;
            m_exceptionalChildren.put(PortTypeDef.Attribute.PORT_OBJECT_CLASS, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for portObjectSpecClass
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param portObjectSpecClass Returns the class of the port object spec.
     * @return this builder for fluent API.
     */ 
    public PortTypeDefBuilder setPortObjectSpecClass(final String portObjectSpecClass) {
        setPortObjectSpecClass(() -> portObjectSpecClass, portObjectSpecClass);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortTypeDef.Attribute.PORT_OBJECT_SPEC_CLASS)} will return true and and
     * {@code getExceptionalChildren().get(PortTypeDef.Attribute.PORT_OBJECT_SPEC_CLASS)} will return the exception.
     * 
     * @param portObjectSpecClass see {@link PortTypeDef#getPortObjectSpecClass}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setPortObjectSpecClass(String)
     */
    public PortTypeDefBuilder setPortObjectSpecClass(final FallibleSupplier<String> portObjectSpecClass, String defaultValue) {
        java.util.Objects.requireNonNull(portObjectSpecClass, () -> "No supplier for portObjectSpecClass provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortTypeDef.Attribute.PORT_OBJECT_SPEC_CLASS);
        try {
            m_portObjectSpecClass = portObjectSpecClass.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_portObjectSpecClass = defaultValue;
            m_exceptionalChildren.put(PortTypeDef.Attribute.PORT_OBJECT_SPEC_CLASS, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for color
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param color 
     * @return this builder for fluent API.
     */ 
    public PortTypeDefBuilder setColor(final Integer color) {
        setColor(() -> color, color);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortTypeDef.Attribute.COLOR)} will return true and and
     * {@code getExceptionalChildren().get(PortTypeDef.Attribute.COLOR)} will return the exception.
     * 
     * @param color see {@link PortTypeDef#getColor}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setColor(Integer)
     */
    public PortTypeDefBuilder setColor(final FallibleSupplier<Integer> color, Integer defaultValue) {
        java.util.Objects.requireNonNull(color, () -> "No supplier for color provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortTypeDef.Attribute.COLOR);
        try {
            m_color = color.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_color = defaultValue;
            m_exceptionalChildren.put(PortTypeDef.Attribute.COLOR, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for hidden
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param hidden whether to short this port to users, e.g., in dialogs
     * @return this builder for fluent API.
     */ 
    public PortTypeDefBuilder setHidden(final Boolean hidden) {
        setHidden(() -> hidden, hidden);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortTypeDef.Attribute.HIDDEN)} will return true and and
     * {@code getExceptionalChildren().get(PortTypeDef.Attribute.HIDDEN)} will return the exception.
     * 
     * @param hidden see {@link PortTypeDef#isHidden}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHidden(Boolean)
     */
    public PortTypeDefBuilder setHidden(final FallibleSupplier<Boolean> hidden, Boolean defaultValue) {
        java.util.Objects.requireNonNull(hidden, () -> "No supplier for hidden provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortTypeDef.Attribute.HIDDEN);
        try {
            m_hidden = hidden.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_hidden = defaultValue;
            m_exceptionalChildren.put(PortTypeDef.Attribute.HIDDEN, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for optional
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param optional whether this port needs to be connected
     * @return this builder for fluent API.
     */ 
    public PortTypeDefBuilder setOptional(final Boolean optional) {
        setOptional(() -> optional, optional);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortTypeDef.Attribute.OPTIONAL)} will return true and and
     * {@code getExceptionalChildren().get(PortTypeDef.Attribute.OPTIONAL)} will return the exception.
     * 
     * @param optional see {@link PortTypeDef#isOptional}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setOptional(Boolean)
     */
    public PortTypeDefBuilder setOptional(final FallibleSupplier<Boolean> optional, Boolean defaultValue) {
        java.util.Objects.requireNonNull(optional, () -> "No supplier for optional provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortTypeDef.Attribute.OPTIONAL);
        try {
            m_optional = optional.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_optional = defaultValue;
            m_exceptionalChildren.put(PortTypeDef.Attribute.OPTIONAL, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for name
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param name human-readable name. In case the port type is not registered at the extension point, the port object&#39;s class name is returned.
     * @return this builder for fluent API.
     */ 
    public PortTypeDefBuilder setName(final String name) {
        setName(() -> name, name);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(PortTypeDef.Attribute.NAME)} will return true and and
     * {@code getExceptionalChildren().get(PortTypeDef.Attribute.NAME)} will return the exception.
     * 
     * @param name see {@link PortTypeDef#getName}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setName(String)
     */
    public PortTypeDefBuilder setName(final FallibleSupplier<String> name, String defaultValue) {
        java.util.Objects.requireNonNull(name, () -> "No supplier for name provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(PortTypeDef.Attribute.NAME);
        try {
            m_name = name.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_name = defaultValue;
            m_exceptionalChildren.put(PortTypeDef.Attribute.NAME, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link PortTypeDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultPortTypeDef build() {
        
    	
        return new DefaultPortTypeDef(this);
    }    

}
