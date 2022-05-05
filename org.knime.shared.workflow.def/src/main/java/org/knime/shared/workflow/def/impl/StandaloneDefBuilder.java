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

import org.knime.shared.workflow.def.CreatorDef;

// for the Attribute enum and javadoc references
import org.knime.shared.workflow.def.StandaloneDef;
// for types that define enums
import org.knime.shared.workflow.def.StandaloneDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * Represents a standalone, loadable unit for editing in the workflow editor. In case this is a workflow, it is also executable. Standalone components and metanodes cannot be executed but referenced into a workflow and updated when the referenced component/metanode changed. 
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class StandaloneDefBuilder {

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
    Map<StandaloneDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(StandaloneDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    CreatorDef m_creator;
    
    Object m_contents = null;
    

    ContentTypeEnum m_contentType;
    

    /**
     * Create a new builder.
     */
    public StandaloneDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public StandaloneDefBuilder(final StandaloneDef toCopy) {
        m_creator = toCopy.getCreator();
        m_contents = toCopy.getContents();
        m_contentType = toCopy.getContentType();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for creator
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param creator 
     * @return this builder for fluent API.
     */ 
    public StandaloneDefBuilder setCreator(final CreatorDef creator) {
        setCreator(() -> creator, creator);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StandaloneDef.Attribute.CREATOR)} will return true and and
     * {@code getExceptionalChildren().get(StandaloneDef.Attribute.CREATOR)} will return the exception.
     * 
     * @param creator see {@link StandaloneDef#getCreator}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCreator(CreatorDef)
     */
    public StandaloneDefBuilder setCreator(final FallibleSupplier<CreatorDef> creator, CreatorDef defaultValue) {
        java.util.Objects.requireNonNull(creator, () -> "No supplier for creator provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StandaloneDef.Attribute.CREATOR);
        try {
            m_creator = creator.get();
            if (m_creator instanceof LoadExceptionTree<?> && ((LoadExceptionTree<?>)m_creator).hasExceptions()) {
                m_exceptionalChildren.put(StandaloneDef.Attribute.CREATOR, (LoadExceptionTree<?>)m_creator);
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                         
            LoadExceptionTree<?> exceptionTree;
            if(defaultValue instanceof DefaultCreatorDef){
                var childTree = ((DefaultCreatorDef)defaultValue).getLoadExceptionTree();                
                // if present, merge child tree with supply exception
                exceptionTree = childTree.isEmpty() ? supplyException : org.knime.core.util.workflow.def.SimpleLoadExceptionTree.tree(childTree.get(), supplyException);
            } else {
                exceptionTree = supplyException;
            }
            m_creator = defaultValue;
            m_exceptionalChildren.put(StandaloneDef.Attribute.CREATOR, exceptionTree);
            	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for contents
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param contents This is either a RootWorkflow or a Component or a Metanode, which one is indicated by contentType. Having a workflow is useful when adding version information to copied content. 
     * @return this builder for fluent API.
     */ 
    public StandaloneDefBuilder setContents(final Object contents) {
        setContents(() -> contents, contents);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StandaloneDef.Attribute.CONTENTS)} will return true and and
     * {@code getExceptionalChildren().get(StandaloneDef.Attribute.CONTENTS)} will return the exception.
     * 
     * @param contents see {@link StandaloneDef#getContents}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setContents(Object)
     */
    public StandaloneDefBuilder setContents(final FallibleSupplier<Object> contents, Object defaultValue) {
        java.util.Objects.requireNonNull(contents, () -> "No supplier for contents provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StandaloneDef.Attribute.CONTENTS);
        try {
            m_contents = contents.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_contents = defaultValue;
            m_exceptionalChildren.put(StandaloneDef.Attribute.CONTENTS, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for contentType
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param contentType Describes the type of the contents field.
     * @return this builder for fluent API.
     */ 
    public StandaloneDefBuilder setContentType(final ContentTypeEnum contentType) {
        setContentType(() -> contentType, contentType);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(StandaloneDef.Attribute.CONTENT_TYPE)} will return true and and
     * {@code getExceptionalChildren().get(StandaloneDef.Attribute.CONTENT_TYPE)} will return the exception.
     * 
     * @param contentType see {@link StandaloneDef#getContentType}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setContentType(ContentTypeEnum)
     */
    public StandaloneDefBuilder setContentType(final FallibleSupplier<ContentTypeEnum> contentType, ContentTypeEnum defaultValue) {
        java.util.Objects.requireNonNull(contentType, () -> "No supplier for contentType provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(StandaloneDef.Attribute.CONTENT_TYPE);
        try {
            m_contentType = contentType.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_contentType = defaultValue;
            m_exceptionalChildren.put(StandaloneDef.Attribute.CONTENT_TYPE, supplyException);
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link StandaloneDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultStandaloneDef build() {
        
    	
        return new DefaultStandaloneDef(this);
    }    

}
