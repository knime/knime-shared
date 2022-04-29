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


import org.knime.core.workflow.def.WorkflowUISettingsDef;



// for types that define enums
import org.knime.core.workflow.def.WorkflowUISettingsDef.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.knime.core.util.workflow.def.LoadException;
import org.knime.core.util.workflow.def.LoadExceptionTree;
import org.knime.core.util.workflow.def.SimpleLoadExceptionTree;


/**
 * FallibleWorkflowUISettingsDef
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
// @javax.annotation.Generated(value = {"com.knime.gateway.codegen.CoreCodegen", "src-gen/api/core/configs/org.knime.core.workflow.def.impl.fallible-config.json"})
@JsonPropertyOrder(alphabetic = true)
public class FallibleWorkflowUISettingsDef implements WorkflowUISettingsDef {

    /** this either points to a LoadException (which implements LoadExceptionTree<Void>) or to
     * a LoadExceptionTree<WorkflowUISettingsDef.Attribute> instance. */
    final private Optional<LoadExceptionTree<?>> m_exceptionTree;

    /** 
     * Whether nodes moved in the editor are aligned to the nearest grid point. 
     */
    @JsonProperty("snapToGrid")
    protected Boolean m_snapToGrid;

    /** 
     * Whether to show a grid in the workflow editor background. 
     */
    @JsonProperty("showGrid")
    protected Boolean m_showGrid;

    @JsonProperty("gridX")
    protected Integer m_gridX;

    @JsonProperty("gridY")
    protected Integer m_gridY;

    /** 
     * The current magnification of the workflow in the workflow editor. 
     */
    @JsonProperty("zoomLevel")
    protected Double m_zoomLevel;

    /** 
     * Whether to use straight or curved segments to connect a connection&#39;s bend points. 
     */
    @JsonProperty("curvedConnections")
    protected Boolean m_curvedConnections;

    /** 
     * The width of the lines connecting a workflow&#39;s nodes in the workflow editor. 
     */
    @JsonProperty("connectionLineWidth")
    protected Integer m_connectionLineWidth;

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Internal constructor for subclasses.
     */
    FallibleWorkflowUISettingsDef() {
        m_exceptionTree = Optional.empty();
    }

    /**
     * Builder constructor. Only for use in {@link WorkflowUISettingsDefBuilder}.
     * @param builder source
     */
    FallibleWorkflowUISettingsDef(WorkflowUISettingsDefBuilder builder) {
        // TODO make immutable copies!!
        
            
        m_snapToGrid = builder.m_snapToGrid;
        m_showGrid = builder.m_showGrid;
        m_gridX = builder.m_gridX;
        m_gridY = builder.m_gridY;
        m_zoomLevel = builder.m_zoomLevel;
        m_curvedConnections = builder.m_curvedConnections;
        m_connectionLineWidth = builder.m_connectionLineWidth;

        m_exceptionTree = Optional.empty();
    }

    /**
     * LoadExceptionTree constructor. Copies the given instance and sets the {@link LoadException} as supply
     * exception, marking it as a default value that had to be used due to the given {@link LoadException}.
     * Only for use in {@link WorkflowUISettingsDefBuilder}.
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    FallibleWorkflowUISettingsDef(WorkflowUISettingsDef toCopy, final LoadException supplyException) {
        Objects.requireNonNull(supplyException);
        toCopy = Objects.requireNonNullElse(toCopy, new WorkflowUISettingsDefBuilder().build());
        
        m_snapToGrid = toCopy.isSnapToGrid();
        m_showGrid = toCopy.isShowGrid();
        m_gridX = toCopy.getGridX();
        m_gridY = toCopy.getGridY();
        m_zoomLevel = toCopy.getZoomLevel();
        m_curvedConnections = toCopy.isCurvedConnections();
        m_connectionLineWidth = toCopy.getConnectionLineWidth();
        if(toCopy instanceof FallibleWorkflowUISettingsDef){
            var childTree = ((FallibleWorkflowUISettingsDef)toCopy).getLoadExceptionTree();                
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
    public FallibleWorkflowUISettingsDef(WorkflowUISettingsDef toCopy) {
        m_snapToGrid = toCopy.isSnapToGrid();
        m_showGrid = toCopy.isShowGrid();
        m_gridX = toCopy.getGridX();
        m_gridY = toCopy.getGridY();
        m_zoomLevel = toCopy.getZoomLevel();
        m_curvedConnections = toCopy.isCurvedConnections();
        m_connectionLineWidth = toCopy.getConnectionLineWidth();
        
        m_exceptionTree = Optional.empty();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Factory method to attach a LoadException to an existing WorkflowUISettings
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Factory method that copies all subtype information but returns the more general type, e.g., for inclusion in a container.
     * 
     * @param toCopy default value to annotate with the supply exception
     * @param exception supply exception, not null (use the copy constructor otherwise)
     */
    static FallibleWorkflowUISettingsDef withException(WorkflowUISettingsDef toCopy, final LoadException exception) {
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
    public Optional<LoadExceptionTree<?>> getLoadExceptionTree(WorkflowUISettingsDef.Attribute attribute){
        return m_exceptionTree.flatMap(t -> {
            if(t instanceof LoadException) return Optional.empty();
            // if the tree is not a leaf, it is typed to WorkflowUISettingsDef.Attribute
            return ((LoadExceptionTree<WorkflowUISettingsDef.Attribute>)t).getExceptionTree(attribute);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Boolean isSnapToGrid() {
        return m_snapToGrid;
    }
    @Override
    public Boolean isShowGrid() {
        return m_showGrid;
    }
    @Override
    public Integer getGridX() {
        return m_gridX;
    }
    @Override
    public Integer getGridY() {
        return m_gridY;
    }
    @Override
    public Double getZoomLevel() {
        return m_zoomLevel;
    }
    @Override
    public Boolean isCurvedConnections() {
        return m_curvedConnections;
    }
    @Override
    public Integer getConnectionLineWidth() {
        return m_connectionLineWidth;
    }
    
    // -------------------------------------------------------------------------------------------------------------------
    // Load Exception Convenience Getters: Cast LoadExceptionTree<?> to more specific type where possible
    // -------------------------------------------------------------------------------------------------------------------  
    
    /**
     * @return The supply exception associated to snapToGrid.
     */
    @JsonIgnore
    public Optional<LoadException> getSnapToGridSupplyException(){
    	return getLoadExceptionTree(WorkflowUISettingsDef.Attribute.SNAP_TO_GRID).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to showGrid.
     */
    @JsonIgnore
    public Optional<LoadException> getShowGridSupplyException(){
    	return getLoadExceptionTree(WorkflowUISettingsDef.Attribute.SHOW_GRID).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to gridX.
     */
    @JsonIgnore
    public Optional<LoadException> getGridXSupplyException(){
    	return getLoadExceptionTree(WorkflowUISettingsDef.Attribute.GRID_X).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to gridY.
     */
    @JsonIgnore
    public Optional<LoadException> getGridYSupplyException(){
    	return getLoadExceptionTree(WorkflowUISettingsDef.Attribute.GRID_Y).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to zoomLevel.
     */
    @JsonIgnore
    public Optional<LoadException> getZoomLevelSupplyException(){
    	return getLoadExceptionTree(WorkflowUISettingsDef.Attribute.ZOOM_LEVEL).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to curvedConnections.
     */
    @JsonIgnore
    public Optional<LoadException> getCurvedConnectionsSupplyException(){
    	return getLoadExceptionTree(WorkflowUISettingsDef.Attribute.CURVED_CONNECTIONS).flatMap(LoadExceptionTree::getSupplyException);
    }
    
 
     
    /**
     * @return The supply exception associated to connectionLineWidth.
     */
    @JsonIgnore
    public Optional<LoadException> getConnectionLineWidthSupplyException(){
    	return getLoadExceptionTree(WorkflowUISettingsDef.Attribute.CONNECTION_LINE_WIDTH).flatMap(LoadExceptionTree::getSupplyException);
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
        FallibleWorkflowUISettingsDef other = (FallibleWorkflowUISettingsDef)o;
        var equalsBuilder = new org.apache.commons.lang3.builder.EqualsBuilder();
        equalsBuilder.append(m_snapToGrid, other.m_snapToGrid);
        equalsBuilder.append(m_showGrid, other.m_showGrid);
        equalsBuilder.append(m_gridX, other.m_gridX);
        equalsBuilder.append(m_gridY, other.m_gridY);
        equalsBuilder.append(m_zoomLevel, other.m_zoomLevel);
        equalsBuilder.append(m_curvedConnections, other.m_curvedConnections);
        equalsBuilder.append(m_connectionLineWidth, other.m_connectionLineWidth);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_snapToGrid)
                .append(m_showGrid)
                .append(m_gridX)
                .append(m_gridY)
                .append(m_zoomLevel)
                .append(m_curvedConnections)
                .append(m_connectionLineWidth)
                .toHashCode();
    }

} 
