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

import org.knime.core.workflow.def.CoordinateDef;

// for the Attribute enum and javadoc references
import org.knime.core.workflow.def.ConnectionUISettingsDef;
// for types that define enums
import org.knime.core.workflow.def.ConnectionUISettingsDef.*;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
/**
 * ConnectionUISettingsDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.def-builder-config.json"})
public class ConnectionUISettingsDefBuilder {

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
    Map<ConnectionUISettingsDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(ConnectionUISettingsDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Holds the final result of merging the bulk and individual elements in #build().
     * Elements added individually go directly into this list so they are inserted at positions 0, 1, ... this is important for non-Def types since the accompanying {@code Map<Integer, LoadException>} uses the element's offset to correlate it to its LoadException.
     */
    java.util.List<CoordinateDef> m_bendPoints = new java.util.ArrayList<>();
    /** Temporarily holds onto elements set as a whole with setBendPoints these are added to m_bendPoints in build */
    private java.util.List<CoordinateDef> m_bendPointsBulkElements = new java.util.ArrayList<>();
    /** This exception is merged with the exceptions of the elements of this list into a single {@link LoadExceptionTree} during {@link #build()}. The LES is then put into {@link #m_m_exceptionalChildren}. */
    private LoadException m_bendPointsContainerSupplyException; 
    
    /**
     * Create a new builder.
     */
    public ConnectionUISettingsDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public ConnectionUISettingsDefBuilder(final ConnectionUISettingsDef toCopy) {
        m_bendPoints = toCopy.getBendPoints();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for bendPoints
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * Adds elements in bulk to the bendPoints list. 
     * Calling this method again will undo the previous call (it is not additive).
     * Elements previously or subsequently added with {@link #addToBendPoints} will be inserted at the end of the list.
     * @param bendPoints 
     * @return this for fluent API
     */
    public ConnectionUISettingsDefBuilder setBendPoints(final java.util.List<CoordinateDef> bendPoints) {
        setBendPoints(() -> bendPoints);
        return this;
    }
 
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * A thrown {@link LoadException} is associated to the bendPoints list,
     * whereas exceptions thrown in addTo allows to register a {@link LoadException} 
     * for an individual element of the bendPoints list). 
     * {@code hasExceptions(ConnectionUISettingsDef.Attribute.BEND_POINTS)} will return true and and
     * {@code getExceptionalChildren().get(ConnectionUISettingsDef.Attribute.BEND_POINTS)} will return the exception.
     * 
     * @param bendPoints see {@link ConnectionUISettingsDef#getBendPoints}
     * 
     * @return this builder for fluent API.
     * @see #setBendPoints(java.util.List<CoordinateDef>)
     */
    public ConnectionUISettingsDefBuilder setBendPoints(final FallibleSupplier<java.util.List<CoordinateDef>> bendPoints) {
        java.util.Objects.requireNonNull(bendPoints, () -> "No supplier for bendPoints provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(ConnectionUISettingsDef.Attribute.BEND_POINTS);
        try {
            m_bendPointsBulkElements = bendPoints.get();
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
             
            m_bendPointsBulkElements = java.util.List.of();
            // merged together with list element exceptions into a single LoadExceptionTree in #build()
            m_bendPointsContainerSupplyException = supplyException;
	    }   
        return this;
    }
    /**
     * @param value the value to add to the bendPoints list
     * @return this builder for fluent API
     */
    public ConnectionUISettingsDefBuilder addToBendPoints(CoordinateDef value){
    	addToBendPoints(() -> value, /* default value will not be used */ value);
        return this;
    }
    
    /**
     * Adds the return value of the fallible supplier to the list returned by {@link ConnectionUISettingsDef#getBendPoints}. If the 
     * fallible supplier fails, adds the default value instead and registers a {@link LoadException} for the added element's index in the list.
     *
     * @param value the value of the entry to add to the bendPoints list
     * @param defaultValue is added to the list as value for the key if an exception occurs during {@link FallibleSupplier#get}
     * @return this builder for fluent API.
     */
    public ConnectionUISettingsDefBuilder addToBendPoints(FallibleSupplier<CoordinateDef> value, CoordinateDef defaultValue) {
        CoordinateDef toAdd = null;
        try {
            toAdd = value.get();
        } catch (Exception e) {
            var supplyException = new LoadException(e);
            toAdd = new FallibleCoordinateDef(defaultValue, supplyException);
        }
        m_bendPoints.add(toAdd);
        return this;
    } 
    // -----------------------------------------------------------------------------------------------------------------
    // Build method
    // -----------------------------------------------------------------------------------------------------------------
    /**
	 * @return the {@link ConnectionUISettingsDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public FallibleConnectionUISettingsDef build() {
        
    	
        // contains the elements set with #setBendPoints (those added with #addToBendPoints have already been inserted into m_bendPoints)
        m_bendPointsBulkElements = java.util.Objects.requireNonNullElse(m_bendPointsBulkElements, java.util.List.of());
        m_bendPoints.addAll(0, m_bendPointsBulkElements);
                
        var bendPointsLoadExceptionTree = org.knime.core.util.workflow.def.SimpleLoadExceptionTree
            .list(m_bendPoints, m_bendPointsContainerSupplyException);
        if(bendPointsLoadExceptionTree.hasExceptions()){
            m_exceptionalChildren.put(ConnectionUISettingsDef.Attribute.BEND_POINTS, bendPointsLoadExceptionTree);
        }
        
        return new FallibleConnectionUISettingsDef(this);
    }    

}
