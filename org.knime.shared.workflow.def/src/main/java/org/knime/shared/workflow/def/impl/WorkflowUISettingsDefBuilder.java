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
import org.knime.shared.workflow.def.WorkflowUISettingsDef;
// for types that define enums
import org.knime.shared.workflow.def.WorkflowUISettingsDef.*;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.LoadExceptionTreeProvider;

/**
 * WorkflowUISettingsDefBuilder
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.shared.workflow.def.impl.def-builder-config.json"})
public class WorkflowUISettingsDefBuilder {

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
    public WorkflowUISettingsDefBuilder strict(){
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
    Map<WorkflowUISettingsDef.Attribute, LoadExceptionTree<?>> m_exceptionalChildren = new java.util.EnumMap<>(WorkflowUISettingsDef.Attribute.class);

    // -----------------------------------------------------------------------------------------------------------------
    // Def attributes
    // -----------------------------------------------------------------------------------------------------------------
    Boolean m_snapToGrid = true;
    

    Boolean m_showGrid = false;
    

    Integer m_gridX = 20;
    

    Integer m_gridY = 20;
    

    Double m_zoomLevel = 1.25d;
    

    Boolean m_curvedConnections = true;
    

    Integer m_connectionLineWidth = 2;
    

    /**
     * Create a new builder.
     */
    public WorkflowUISettingsDefBuilder() {
    }

    /**
     * Create a new builder from an existing instance.
     */
    public WorkflowUISettingsDefBuilder(final WorkflowUISettingsDef toCopy) {
        setSnapToGrid(toCopy.isSnapToGrid());
        setShowGrid(toCopy.isShowGrid());
        setGridX(toCopy.getGridX());
        setGridY(toCopy.getGridY());
        setZoomLevel(toCopy.getZoomLevel());
        setCurvedConnections(toCopy.isCurvedConnections());
        setConnectionLineWidth(toCopy.getConnectionLineWidth());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setters for snapToGrid
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param snapToGrid Whether nodes moved in the editor are aligned to the nearest grid point. 
     * @return this builder for fluent API.
     */ 
    public WorkflowUISettingsDefBuilder setSnapToGrid(final Boolean snapToGrid) {
        setSnapToGrid(() -> snapToGrid, snapToGrid);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowUISettingsDef.Attribute.SNAP_TO_GRID)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowUISettingsDef.Attribute.SNAP_TO_GRID)} will return the exception.
     * 
     * @param snapToGrid see {@link WorkflowUISettingsDef#isSnapToGrid}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setSnapToGrid(Boolean)
     */
    public WorkflowUISettingsDefBuilder setSnapToGrid(final FallibleSupplier<Boolean> snapToGrid, Boolean defaultValue) {
        java.util.Objects.requireNonNull(snapToGrid, () -> "No supplier for snapToGrid provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowUISettingsDef.Attribute.SNAP_TO_GRID);
        try {
            var supplied = snapToGrid.get();
            m_snapToGrid = supplied;

            if(m_snapToGrid == null) {
                throw new IllegalArgumentException("snapToGrid is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_snapToGrid = defaultValue;
            m_exceptionalChildren.put(WorkflowUISettingsDef.Attribute.SNAP_TO_GRID, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for showGrid
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param showGrid Whether to show a grid in the workflow editor background. 
     * @return this builder for fluent API.
     */ 
    public WorkflowUISettingsDefBuilder setShowGrid(final Boolean showGrid) {
        setShowGrid(() -> showGrid, showGrid);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowUISettingsDef.Attribute.SHOW_GRID)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowUISettingsDef.Attribute.SHOW_GRID)} will return the exception.
     * 
     * @param showGrid see {@link WorkflowUISettingsDef#isShowGrid}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setShowGrid(Boolean)
     */
    public WorkflowUISettingsDefBuilder setShowGrid(final FallibleSupplier<Boolean> showGrid, Boolean defaultValue) {
        java.util.Objects.requireNonNull(showGrid, () -> "No supplier for showGrid provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowUISettingsDef.Attribute.SHOW_GRID);
        try {
            var supplied = showGrid.get();
            m_showGrid = supplied;

            if(m_showGrid == null) {
                throw new IllegalArgumentException("showGrid is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_showGrid = defaultValue;
            m_exceptionalChildren.put(WorkflowUISettingsDef.Attribute.SHOW_GRID, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for gridX
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param gridX Horizontal spacing of grid lines. 
     * @return this builder for fluent API.
     */ 
    public WorkflowUISettingsDefBuilder setGridX(final Integer gridX) {
        setGridX(() -> gridX, gridX);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowUISettingsDef.Attribute.GRID_X)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowUISettingsDef.Attribute.GRID_X)} will return the exception.
     * 
     * @param gridX see {@link WorkflowUISettingsDef#getGridX}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setGridX(Integer)
     */
    public WorkflowUISettingsDefBuilder setGridX(final FallibleSupplier<Integer> gridX, Integer defaultValue) {
        java.util.Objects.requireNonNull(gridX, () -> "No supplier for gridX provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowUISettingsDef.Attribute.GRID_X);
        try {
            var supplied = gridX.get();
            m_gridX = supplied;

            if(m_gridX == null) {
                throw new IllegalArgumentException("gridX is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_gridX = defaultValue;
            m_exceptionalChildren.put(WorkflowUISettingsDef.Attribute.GRID_X, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for gridY
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param gridY Vertical spacing of grid lines. 
     * @return this builder for fluent API.
     */ 
    public WorkflowUISettingsDefBuilder setGridY(final Integer gridY) {
        setGridY(() -> gridY, gridY);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowUISettingsDef.Attribute.GRID_Y)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowUISettingsDef.Attribute.GRID_Y)} will return the exception.
     * 
     * @param gridY see {@link WorkflowUISettingsDef#getGridY}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setGridY(Integer)
     */
    public WorkflowUISettingsDefBuilder setGridY(final FallibleSupplier<Integer> gridY, Integer defaultValue) {
        java.util.Objects.requireNonNull(gridY, () -> "No supplier for gridY provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowUISettingsDef.Attribute.GRID_Y);
        try {
            var supplied = gridY.get();
            m_gridY = supplied;

            if(m_gridY == null) {
                throw new IllegalArgumentException("gridY is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_gridY = defaultValue;
            m_exceptionalChildren.put(WorkflowUISettingsDef.Attribute.GRID_Y, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for zoomLevel
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param zoomLevel The current magnification of the workflow in the workflow editor. 
     * @return this builder for fluent API.
     */ 
    public WorkflowUISettingsDefBuilder setZoomLevel(final Double zoomLevel) {
        setZoomLevel(() -> zoomLevel, zoomLevel);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowUISettingsDef.Attribute.ZOOM_LEVEL)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowUISettingsDef.Attribute.ZOOM_LEVEL)} will return the exception.
     * 
     * @param zoomLevel see {@link WorkflowUISettingsDef#getZoomLevel}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setZoomLevel(Double)
     */
    public WorkflowUISettingsDefBuilder setZoomLevel(final FallibleSupplier<Double> zoomLevel, Double defaultValue) {
        java.util.Objects.requireNonNull(zoomLevel, () -> "No supplier for zoomLevel provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowUISettingsDef.Attribute.ZOOM_LEVEL);
        try {
            var supplied = zoomLevel.get();
            m_zoomLevel = supplied;

            if(m_zoomLevel == null) {
                throw new IllegalArgumentException("zoomLevel is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_zoomLevel = defaultValue;
            m_exceptionalChildren.put(WorkflowUISettingsDef.Attribute.ZOOM_LEVEL, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for curvedConnections
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param curvedConnections Whether to use straight or curved segments to connect a connection&#39;s bend points. 
     * @return this builder for fluent API.
     */ 
    public WorkflowUISettingsDefBuilder setCurvedConnections(final Boolean curvedConnections) {
        setCurvedConnections(() -> curvedConnections, curvedConnections);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowUISettingsDef.Attribute.CURVED_CONNECTIONS)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowUISettingsDef.Attribute.CURVED_CONNECTIONS)} will return the exception.
     * 
     * @param curvedConnections see {@link WorkflowUISettingsDef#isCurvedConnections}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setCurvedConnections(Boolean)
     */
    public WorkflowUISettingsDefBuilder setCurvedConnections(final FallibleSupplier<Boolean> curvedConnections, Boolean defaultValue) {
        java.util.Objects.requireNonNull(curvedConnections, () -> "No supplier for curvedConnections provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowUISettingsDef.Attribute.CURVED_CONNECTIONS);
        try {
            var supplied = curvedConnections.get();
            m_curvedConnections = supplied;

            if(m_curvedConnections == null) {
                throw new IllegalArgumentException("curvedConnections is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_curvedConnections = defaultValue;
            m_exceptionalChildren.put(WorkflowUISettingsDef.Attribute.CURVED_CONNECTIONS, supplyException);
            if(m__failFast){
                throw new IllegalStateException(e);
            }
	    }   
        return this;
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Setters for connectionLineWidth
    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param connectionLineWidth The width of the lines connecting a workflow&#39;s nodes in the workflow editor. 
     * @return this builder for fluent API.
     */ 
    public WorkflowUISettingsDefBuilder setConnectionLineWidth(final Integer connectionLineWidth) {
        setConnectionLineWidth(() -> connectionLineWidth, connectionLineWidth);
        return this;
    }
 
    
    /**
     * Sets the field using a supplier that may throw an exception. If an exception is thrown, it is recorded and can
     * be accessed through {@link LoadExceptionTree} interface of the instance build by this builder.
     * {@code hasExceptions(WorkflowUISettingsDef.Attribute.CONNECTION_LINE_WIDTH)} will return true and and
     * {@code getExceptionalChildren().get(WorkflowUISettingsDef.Attribute.CONNECTION_LINE_WIDTH)} will return the exception.
     * 
     * @param connectionLineWidth see {@link WorkflowUISettingsDef#getConnectionLineWidth}
     * @param defaultValue is set in case the supplier throws an exception.
     * @return this builder for fluent API.
     * @see #setConnectionLineWidth(Integer)
     */
    public WorkflowUISettingsDefBuilder setConnectionLineWidth(final FallibleSupplier<Integer> connectionLineWidth, Integer defaultValue) {
        java.util.Objects.requireNonNull(connectionLineWidth, () -> "No supplier for connectionLineWidth provided.");
        // in case the setter was called before with an exception and this time there is no exception, remove the old exception
        m_exceptionalChildren.remove(WorkflowUISettingsDef.Attribute.CONNECTION_LINE_WIDTH);
        try {
            var supplied = connectionLineWidth.get();
            m_connectionLineWidth = supplied;
            if(m_connectionLineWidth != null && m_connectionLineWidth < 1) {
                throw new IllegalArgumentException("connectionLineWidth must not be smaller than 1, but was given: " + m_connectionLineWidth);
            }
            if(m_connectionLineWidth != null && m_connectionLineWidth > 3) {
                throw new IllegalArgumentException("connectionLineWidth must not be larger than 3, but was given: " + m_connectionLineWidth);
            }
            
            if(m_connectionLineWidth == null) {
                throw new IllegalArgumentException("connectionLineWidth is required and must not be null.");
            }
	    } catch (Exception e) {
            var supplyException = new LoadException(e);
                                     
            m_connectionLineWidth = defaultValue;
            m_exceptionalChildren.put(WorkflowUISettingsDef.Attribute.CONNECTION_LINE_WIDTH, supplyException);
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
	 * @return the {@link WorkflowUISettingsDef} created from the data passed to the setters. Implements 
     *      {@link LoadExceptionTree} to provide access to any load exceptions that have occurred during evaluation
     *      of the suppliers passed to the setters.
	 */
    public DefaultWorkflowUISettingsDef build() {
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_snapToGrid == null) setSnapToGrid( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_showGrid == null) setShowGrid( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_gridX == null) setGridX( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_gridY == null) setGridY( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_zoomLevel == null) setZoomLevel( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_curvedConnections == null) setCurvedConnections( null);
        
         
        // in case the setter has never been called, the required field is still null, but no load exception was recorded. Do that now.
        if(m_connectionLineWidth == null) setConnectionLineWidth( null);
        
    	
        return new DefaultWorkflowUISettingsDef(this);
    }    

}
