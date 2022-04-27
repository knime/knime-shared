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
import java.util.Optional;
import java.util.Objects; 

import org.apache.commons.lang3.builder.HashCodeBuilder;


import org.knime.core.workflow.def.ComponentDialogSettingsDef;



// for types that define enums
import org.knime.core.workflow.def.ComponentDialogSettingsDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * Properties relating to the display of a Component&#39;s node dialog.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class FallibleComponentDialogSettingsDef implements ComponentDialogSettingsDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<ComponentDialogSettingsDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    @JsonProperty("layoutJSON")
    protected String m_layoutJSON;

    @JsonProperty("configurationLayoutJSON")
    protected String m_configurationLayoutJSON;

    @JsonProperty("hideInWizard")
    protected Boolean m_hideInWizard;

    @JsonProperty("cssStyles")
    protected String m_cssStyles;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    FallibleComponentDialogSettingsDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link ComponentDialogSettingsDefBuilder}.
     * @param builder source
     */
    FallibleComponentDialogSettingsDef(ComponentDialogSettingsDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_layoutJSON = builder.m_layoutJSON;
        m_configurationLayoutJSON = builder.m_configurationLayoutJSON;
        m_hideInWizard = builder.m_hideInWizard;
        m_cssStyles = builder.m_cssStyles;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link ComponentDialogSettingsDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    FallibleComponentDialogSettingsDef(ComponentDialogSettingsDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new ComponentDialogSettingsDefBuilder().build());
        
        m_layoutJSON = toCopy.getLayoutJSON();
        m_configurationLayoutJSON = toCopy.getConfigurationLayoutJSON();
        m_hideInWizard = toCopy.isHideInWizard();
        m_cssStyles = toCopy.getCssStyles();
        if(toCopy instanceof FallibleComponentDialogSettingsDef){
            var childTree = ((FallibleComponentDialogSettingsDef)toCopy).getLoadExceptionTree();                
            // if present, merge child tree with supply exception
            var merged = childTree.isEmpty() ? supplyException : SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            m_exceptionTree = Optional.of(merged);
        } else {
            m_exceptionTree = Optional.of(supplyException);
        }
    }



    /**
     * Copy constructor.
     * @param toCopy source
     */
    public FallibleComponentDialogSettingsDef(ComponentDialogSettingsDef toCopy) {
        m_layoutJSON = toCopy.getLayoutJSON();
        m_configurationLayoutJSON = toCopy.getConfigurationLayoutJSON();
        m_hideInWizard = toCopy.isHideInWizard();
        m_cssStyles = toCopy.getCssStyles();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing ComponentDialogSettings
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static FallibleComponentDialogSettingsDef withException(ComponentDialogSettingsDef toCopy, final LoadException exception) {
        Objects.requireNonNull(exception);
        throw new IllegalArgumentException();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree implementation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @return the load exceptions for this instance and its descendants
     */
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(ComponentDialogSettingsDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to ComponentDialogSettingsDef.Attribute
            return ((LoadExceptionTree<ComponentDialogSettingsDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getLayoutJSON() {
        return m_layoutJSON;
    }
    @Override
    public String getConfigurationLayoutJSON() {
        return m_configurationLayoutJSON;
    }
    @Override
    public Boolean isHideInWizard() {
        return m_hideInWizard;
    }
    @Override
    public String getCssStyles() {
        return m_cssStyles;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to layoutJSON.
     */
    @JsonIgnore
    public Optional<LoadException> getLayoutJSONSupplyException(){
    	return getLoadExceptionTree(ComponentDialogSettingsDef.Attribute.LAYOUT_JSON).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to configurationLayoutJSON.
     */
    @JsonIgnore
    public Optional<LoadException> getConfigurationLayoutJSONSupplyException(){
    	return getLoadExceptionTree(ComponentDialogSettingsDef.Attribute.CONFIGURATION_LAYOUT_JSON).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to hideInWizard.
     */
    @JsonIgnore
    public Optional<LoadException> getHideInWizardSupplyException(){
    	return getLoadExceptionTree(ComponentDialogSettingsDef.Attribute.HIDE_IN_WIZARD).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to cssStyles.
     */
    @JsonIgnore
    public Optional<LoadException> getCssStylesSupplyException(){
    	return getLoadExceptionTree(ComponentDialogSettingsDef.Attribute.CSS_STYLES).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    

    // -----------------------------------------------------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FallibleComponentDialogSettingsDef)) {
            return false;
        }
        FallibleComponentDialogSettingsDef other = (FallibleComponentDialogSettingsDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.appendSuper(super.equals(other));
        equalsBuilder.append(m_layoutJSON, other.m_layoutJSON);
        equalsBuilder.append(m_configurationLayoutJSON, other.m_configurationLayoutJSON);
        equalsBuilder.append(m_hideInWizard, other.m_hideInWizard);
        equalsBuilder.append(m_cssStyles, other.m_cssStyles);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_layoutJSON)
                .append(m_configurationLayoutJSON)
                .append(m_hideInWizard)
                .append(m_cssStyles)
                .toHashCode();
    }

} 
