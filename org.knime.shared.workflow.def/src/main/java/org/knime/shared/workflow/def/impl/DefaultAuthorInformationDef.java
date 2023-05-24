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
import java.util.Objects; 

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.OffsetDateTime;

import org.knime.shared.workflow.def.AuthorInformationDef;



// for types that define enums
import org.knime.shared.workflow.def.AuthorInformationDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * DefaultAuthorInformationDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @jakarta.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class DefaultAuthorInformationDef implements AuthorInformationDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<AuthorInformationDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    @JsonProperty("authoredBy")
    protected String m_authoredBy;

    @JsonProperty("authoredWhen")
    protected OffsetDateTime m_authoredWhen;

    @JsonProperty("lastEditedBy")
    protected String m_lastEditedBy;

    @JsonProperty("lastEditedWhen")
    protected OffsetDateTime m_lastEditedWhen;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    DefaultAuthorInformationDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link AuthorInformationDefBuilder}.
     * @param builder source
     */
    DefaultAuthorInformationDef(AuthorInformationDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_authoredBy = builder.m_authoredBy;
        m_authoredWhen = builder.m_authoredWhen;
        m_lastEditedBy = builder.m_lastEditedBy;
        m_lastEditedWhen = builder.m_lastEditedWhen;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link AuthorInformationDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    DefaultAuthorInformationDef(AuthorInformationDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new AuthorInformationDefBuilder().build());
        
        m_authoredBy = toCopy.getAuthoredBy();
        m_authoredWhen = toCopy.getAuthoredWhen();
        m_lastEditedBy = toCopy.getLastEditedBy();
        m_lastEditedWhen = toCopy.getLastEditedWhen();
        if(toCopy instanceof DefaultAuthorInformationDef){
            var childTree = ((DefaultAuthorInformationDef)toCopy).getLoadExceptionTree();                
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
    public DefaultAuthorInformationDef(AuthorInformationDef toCopy) {
        m_authoredBy = toCopy.getAuthoredBy();
        m_authoredWhen = toCopy.getAuthoredWhen();
        m_lastEditedBy = toCopy.getLastEditedBy();
        m_lastEditedWhen = toCopy.getLastEditedWhen();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing AuthorInformation
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static DefaultAuthorInformationDef withException(AuthorInformationDef toCopy, final LoadException exception) {
        Objects.requireNonNull(exception);
        throw new IllegalArgumentException();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // LoadExceptionTree implementation
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @return the load exceptions for this instance and its descendants
     */
    @JsonIgnore
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(){
        return m_exceptionTree;
    }

    /**
     * @param attribute identifies the child
     * @return the load exceptions for the requested child instance and its descendants
     */
    @SuppressWarnings("unchecked")
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(AuthorInformationDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to AuthorInformationDef.Attribute
            return ((LoadExceptionTree<AuthorInformationDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String getAuthoredBy() {
        return m_authoredBy;
    }
    @Override
    public OffsetDateTime getAuthoredWhen() {
        return m_authoredWhen;
    }
    @Override
    public String getLastEditedBy() {
        return m_lastEditedBy;
    }
    @Override
    public OffsetDateTime getLastEditedWhen() {
        return m_lastEditedWhen;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to authoredBy.
     */
    @JsonIgnore
    public Optional<LoadException> getAuthoredBySupplyException(){
    	return getLoadExceptionTree(AuthorInformationDef.Attribute.AUTHORED_BY).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to authoredWhen.
     */
    @JsonIgnore
    public Optional<LoadException> getAuthoredWhenSupplyException(){
    	return getLoadExceptionTree(AuthorInformationDef.Attribute.AUTHORED_WHEN).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to lastEditedBy.
     */
    @JsonIgnore
    public Optional<LoadException> getLastEditedBySupplyException(){
    	return getLoadExceptionTree(AuthorInformationDef.Attribute.LAST_EDITED_BY).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to lastEditedWhen.
     */
    @JsonIgnore
    public Optional<LoadException> getLastEditedWhenSupplyException(){
    	return getLoadExceptionTree(AuthorInformationDef.Attribute.LAST_EDITED_WHEN).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    

    // -----------------------------------------------------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o.getClass().equals(this.getClass()))) {
            return false;
        }
        DefaultAuthorInformationDef other = (DefaultAuthorInformationDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_authoredBy, other.m_authoredBy);
        equalsBuilder.append(m_authoredWhen, other.m_authoredWhen);
        equalsBuilder.append(m_lastEditedBy, other.m_lastEditedBy);
        equalsBuilder.append(m_lastEditedWhen, other.m_lastEditedWhen);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_authoredBy)
                .append(m_authoredWhen)
                .append(m_lastEditedBy)
                .append(m_lastEditedWhen)
                .toHashCode();
    }

} 
