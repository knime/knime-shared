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
package org.knime.shared.workflow.def.impl;

import java.util.Map;
import java.util.Optional;


// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.ComponentDialogSettingsDef;
// for types that define enums
import org.knime.shared.workflow.def.ComponentDialogSettingsDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * Properties relating to the display of a Component&#39;s node dialog.
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class ComponentDialogSettingsDefBuilder {

    /**
     * @see #strict()
     */
    boolean m__failFast = false;

    /**
     * Enable fail-fast mode.
     * In fail-fast mode, all load exceptions will be immediately thrown.
     * This can be when invoking a setter with an illegal argument (e.g., null or out of range) or 
     * when invoking {@link #build()} without previously having called the setter for a required field.
     * By default, fail-fast mode is off and all exceptions will be caught instead of thrown and collected for later reference into a LoadExceptionTree.
     * @return this builder for fluent API.
     */
    public ComponentDialogSettingsDefBuilder strict(){
        m__failFast = true;
        return this;
    }

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
    Map<ComponentDialogSettingsDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ComponentDialogSettingsDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Optional<String> m_layoutJSON = Optional.empty();
    

    Optional<String> m_configurationLayoutJSON = Optional.empty();
    

    Boolean m_hideInWizard = false;
    

    Optional<String> m_cssStyles = Optional.empty();
    

    /**
     * Create a new builder.
     */
    public ComponentDialogSettingsDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ComponentDialogSettingsDefBuilder(final ComponentDialogSettingsDef toCopy) {
        setLayoutJSON(toCopy.getLayoutJSON().orElse(null));
        setConfigurationLayoutJSON(toCopy.getConfigurationLayoutJSON().orElse(null));
        setHideInWizard(toCopy.isHideInWizard());
        setCssStyles(toCopy.getCssStyles().orElse(null));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for layoutJSON
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param layoutJSON  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentDialogSettingsDefBuilder setLayoutJSON(final String layoutJSON) {
        setLayoutJSON(() -> layoutJSON, layoutJSON);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDialogSettingsDef.Attribute.LAYOUT_JSON)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDialogSettingsDef.Attribute.LAYOUT_JSON)} will return the exception.
     * 
     * @param layoutJSON see {@link ComponentDialogSettingsDef#getLayoutJSON}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLayoutJSON(String)
     */
    public ComponentDialogSettingsDefBuilder setLayoutJSON(final FallibleSupplier<String> layoutJSON) {
        setLayoutJSON(layoutJSON, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDialogSettingsDef.Attribute.LAYOUT_JSON)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDialogSettingsDef.Attribute.LAYOUT_JSON)} will return the exception.
     * 
     * @param layoutJSON see {@link ComponentDialogSettingsDef#getLayoutJSON}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setLayoutJSON(String)
     */
    public ComponentDialogSettingsDefBuilder setLayoutJSON(final FallibleSupplier<String> layoutJSON, String defaultValue) {
        java.util.Objects.requireNonNull(layoutJSON, () -> "No supplier for layoutJSON provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDialogSettingsDef.Attribute.LAYOUT_JSON);
        try {
            var supplied = layoutJSON.get();
            m_layoutJSON = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_layoutJSON = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentDialogSettingsDef.Attribute.LAYOUT_JSON, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for configurationLayoutJSON
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param configurationLayoutJSON  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentDialogSettingsDefBuilder setConfigurationLayoutJSON(final String configurationLayoutJSON) {
        setConfigurationLayoutJSON(() -> configurationLayoutJSON, configurationLayoutJSON);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDialogSettingsDef.Attribute.CONFIGURATION_LAYOUT_JSON)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDialogSettingsDef.Attribute.CONFIGURATION_LAYOUT_JSON)} will return the exception.
     * 
     * @param configurationLayoutJSON see {@link ComponentDialogSettingsDef#getConfigurationLayoutJSON}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setConfigurationLayoutJSON(String)
     */
    public ComponentDialogSettingsDefBuilder setConfigurationLayoutJSON(final FallibleSupplier<String> configurationLayoutJSON) {
        setConfigurationLayoutJSON(configurationLayoutJSON, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDialogSettingsDef.Attribute.CONFIGURATION_LAYOUT_JSON)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDialogSettingsDef.Attribute.CONFIGURATION_LAYOUT_JSON)} will return the exception.
     * 
     * @param configurationLayoutJSON see {@link ComponentDialogSettingsDef#getConfigurationLayoutJSON}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setConfigurationLayoutJSON(String)
     */
    public ComponentDialogSettingsDefBuilder setConfigurationLayoutJSON(final FallibleSupplier<String> configurationLayoutJSON, String defaultValue) {
        java.util.Objects.requireNonNull(configurationLayoutJSON, () -> "No supplier for configurationLayoutJSON provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDialogSettingsDef.Attribute.CONFIGURATION_LAYOUT_JSON);
        try {
            var supplied = configurationLayoutJSON.get();
            m_configurationLayoutJSON = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_configurationLayoutJSON = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentDialogSettingsDef.Attribute.CONFIGURATION_LAYOUT_JSON, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for hideInWizard
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param hideInWizard  
     * @return this builder for fluent API.
     */ 
    public ComponentDialogSettingsDefBuilder setHideInWizard(final Boolean hideInWizard) {
        setHideInWizard(() -> hideInWizard, hideInWizard);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDialogSettingsDef.Attribute.HIDE_IN_WIZARD)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDialogSettingsDef.Attribute.HIDE_IN_WIZARD)} will return the exception.
     * 
     * @param hideInWizard see {@link ComponentDialogSettingsDef#isHideInWizard}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setHideInWizard(Boolean)
     */
    public ComponentDialogSettingsDefBuilder setHideInWizard(final FallibleSupplier<Boolean> hideInWizard, Boolean defaultValue) {
        java.util.Objects.requireNonNull(hideInWizard, () -> "No supplier for hideInWizard provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDialogSettingsDef.Attribute.HIDE_IN_WIZARD);
        try {
            var supplied = hideInWizard.get();
            m_hideInWizard = supplied;

            if(m_hideInWizard == null) {
                throw new IllegalArgumentException("hideInWizard is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_hideInWizard = defaultValue;
            m_exceptionalChildren.put(ComponentDialogSettingsDef.Attribute.HIDE_IN_WIZARD, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for cssStyles
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param cssStyles  This is an optional field. Passing <code>null</code> will leave the field empty. 
     * @return this builder for fluent API.
     */ 
    public ComponentDialogSettingsDefBuilder setCssStyles(final String cssStyles) {
        setCssStyles(() -> cssStyles, cssStyles);
        return this;
    }
 
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDialogSettingsDef.Attribute.CSS_STYLES)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDialogSettingsDef.Attribute.CSS_STYLES)} will return the exception.
     * 
     * @param cssStyles see {@link ComponentDialogSettingsDef#getCssStyles}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCssStyles(String)
     */
    public ComponentDialogSettingsDefBuilder setCssStyles(final FallibleSupplier<String> cssStyles) {
        setCssStyles(cssStyles, null);
        return this;
    }
    
    /**
     * Sets the optional field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(ComponentDialogSettingsDef.Attribute.CSS_STYLES)} will return true and and
     * {@code getExceptionalChildren().get(ComponentDialogSettingsDef.Attribute.CSS_STYLES)} will return the exception.
     * 
     * @param cssStyles see {@link ComponentDialogSettingsDef#getCssStyles}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCssStyles(String)
     */
    public ComponentDialogSettingsDefBuilder setCssStyles(final FallibleSupplier<String> cssStyles, String defaultValue) {
        java.util.Objects.requireNonNull(cssStyles, () -> "No supplier for cssStyles provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ComponentDialogSettingsDef.Attribute.CSS_STYLES);
        try {
            var supplied = cssStyles.get();
            m_cssStyles = Optional.ofNullable(supplied);
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_cssStyles = Optional.ofNullable(defaultValue);
            m_exceptionalChildren.put(ComponentDialogSettingsDef.Attribute.CSS_STYLES, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link ComponentDialogSettingsDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultComponentDialogSettingsDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_hideInWizard == null) setHideInWizard( null);
        
    	
        return new DefaultComponentDialogSettingsDef(this);
    }    

}
