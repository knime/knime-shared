/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 * History
 *   31 Jan 2022 (Dionysios Stolis): created
 */
package org.knime.shared.workflow.storage.multidir.loader;

import java.util.Optional;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.BoundsDef;
import org.knime.shared.workflow.def.JobManagerDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.NodeLocksDef;
import org.knime.shared.workflow.def.impl.AnnotationDataDefBuilder;
import org.knime.shared.workflow.def.impl.BoundsDefBuilder;
import org.knime.shared.workflow.def.impl.JobManagerDefBuilder;
import org.knime.shared.workflow.def.impl.NodeAnnotationDefBuilder;
import org.knime.shared.workflow.def.impl.NodeLocksDefBuilder;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;

/**
 * Loads the description of the Node into a {@link BaseNodeDef}. The BaseNodeDef is the bootstrap node of the
 * NativeNode, ComponentNode and MetaNode.
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 * @author Carl Witt, KNIME GmbH, Berlin, Germany
 */
final class BaseNodeLoader {

    private BaseNodeLoader() {
    }

    static final JobManagerDef DEFAULT_JOB_MANAGER = new JobManagerDefBuilder() //
        .setFactory("") //
        .build();

    static final NodeAnnotationDef DEFAULT_NODE_ANNOTATION = new NodeAnnotationDefBuilder().build();

    /**
     * Loads the node's id from the {@code settings}. If the node is a standalone metanode or component, it is expected
     * that no ID is present, thus returning an Optional and not throwing an exception.
     *
     * @param settings a read only representation of part of the workflow.knime file that describes the node. For instance
     * {@code
     * <config key="node_2">
            <entry key="id" type="xint" value="2"/>
            <entry key="node_settings_file" type="xstring" value="CSV Reader (#2)/settings.xml"/>
            // etc.
        </config>
        }
     *
     * @return an {@link Integer}
     * @throws InvalidSettingsException
     */
    static Integer loadNodeId(final ConfigBaseRO settings) throws InvalidSettingsException {
        try {
            return settings.getInt(IOConst.ID_KEY.get());
        } catch (InvalidSettingsException e) { //NOSONAR
            var errorMessage =
                String.format("Unable to load node ID (internal id \"%s\"), trying random number", settings.getKey());
            throw new InvalidSettingsException(errorMessage, e);
        }
    }

    /**
     * Loads the node annotation from the {@code nodeSettings}.
     *
     * @param nodeSettings a read only representation of the settings.xml file.
     * @param workflowFormatVersion a {@link LoadVersion}
     * @return a {@link NodeAnnotationDef}
     * @throws InvalidSettingsException
     */
    static NodeAnnotationDef loadAnnotation(final ConfigBaseRO nodeSettings, final LoadVersion workflowFormatVersion)
        throws InvalidSettingsException {
        if (workflowFormatVersion.isOlderThan(LoadVersion.V250)) {
            var customName = nodeSettings.getString(IOConst.CUSTOM_NAME_KEY.get(), null);
            var isDefault = customName == null;
            return new NodeAnnotationDefBuilder() //
                .setAnnotationDefault(isDefault) //
                .setData(new AnnotationDataDefBuilder() //
                    .setText(customName) //
                    .build()) //
                .build();
        } else {
            var nodeAnnotationSettings = nodeSettings.getOptionalConfigBase(IOConst.NODE_ANNOTATION_KEY.get());
            if (nodeAnnotationSettings.isPresent()) {
                return new NodeAnnotationDefBuilder() //
                    .setAnnotationDefault(false) //
                    .setData(LoaderUtils.loadAnnotation(nodeAnnotationSettings.get(), workflowFormatVersion)) //
                    .build();
            } else {
                return new NodeAnnotationDefBuilder().setAnnotationDefault(true).build();
            }
        }
    }

    /**
     * Loads the job manager settings from the {@code settings}.
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return a {@link JobManagerDef}
     * @throws InvalidSettingsException
     */
    static Optional<JobManagerDef> loadJobManager(final ConfigBaseRO settings) throws InvalidSettingsException {
        try {
            var jobManagerSettings = settings.getOptionalConfigBase(IOConst.JOB_MANAGER_KEY.get());
            if (jobManagerSettings.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new JobManagerDefBuilder()//
                .setFactory(jobManagerSettings.get().getString(IOConst.JOB_MANAGER_FACTORY_ID_KEY.get()))//
                .setSettings(LoaderUtils
                    .toConfigMapDef(jobManagerSettings.get().getConfigBase(IOConst.JOB_MANAGER_SETTINGS_KEY.get())))
                .build());
        } catch (InvalidSettingsException e) {
            var errorMessage = "Can't restore node execution job manager: " + e.getMessage();
            throw new InvalidSettingsException(errorMessage, e);
        }
    }

    /**
     * Loads the node locks from the {@code settings} according to the {@code workflowFormatVersion}.
     *
     * @param settings a read only representation of the node's settings.xml.
     * @param workflowFormatVersion an {@link LoadVersion}.
     * @return
     */
    static NodeLocksDef loadLocks(final ConfigBaseRO settings, final LoadVersion workflowFormatVersion) {
        boolean hasDeleteLock;
        if (workflowFormatVersion.isOlderThan(LoadVersion.V200)) {
            hasDeleteLock = false;
        } else {
            hasDeleteLock = !settings.getBoolean(IOConst.IS_DELETABLE_KEY.get(), true);
        }
        boolean hasResetLock;
        if (workflowFormatVersion.isOlderThan(LoadVersion.V3010)) {
            hasResetLock = false;
        } else {
            hasResetLock = settings.getBoolean(IOConst.HAS_RESET_LOCK_KEY.get(), false);
        }
        boolean hasConfigureLock;
        if (workflowFormatVersion.isOlderThan(LoadVersion.V3010)) {
            hasConfigureLock = false;
        } else {
            hasConfigureLock = settings.getBoolean(IOConst.HAS_CONFIGURE_LOCK_KEY.get(), false);
        }

        return new NodeLocksDefBuilder() //
            .setHasConfigureLock(hasConfigureLock) //
            .setHasDeleteLock(hasDeleteLock) //
            .setHasResetLock(hasResetLock) //
            .build();
    }

    /**
     * Loads the custom description either from {@code workflowConfig} or {@code settings} according to the
     * {@code workflowFormatVersion}
     *
     * @param workflowConfig a read only representation of the workflow.knime.
     * @param settings a read only representation of the settings.xml.
     * @param workflowFormatVersion an {@link LoadVersion}.
     * @return a {@link String}
     * @throws InvalidSettingsException
     */
    static Optional<String> loadCustomDescription(final ConfigBaseRO workflowConfig, final ConfigBaseRO settings,
        final LoadVersion workflowFormatVersion) throws InvalidSettingsException {
        if (workflowFormatVersion.isOlderThan(LoadVersion.V200)) {
            if (!workflowConfig.containsKey(IOConst.CUSTOM_DESCRIPTION_KEY.get())) {
                return Optional.empty();
            }
            return Optional.ofNullable(workflowConfig.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get()));
        } else {
            // custom description was not saved in v2.5.0 (but again in v2.5.1)
            // see bug 3034
            if (!settings.containsKey(IOConst.CUSTOM_DESCRIPTION_KEY.get())) {
                return Optional.empty();
            }
            return Optional.ofNullable(settings.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get()));
        }
    }

    static Optional<BoundsDef> loadBoundsDef(final ConfigBaseRO settings, final LoadVersion workflowFormatVersion)
        throws InvalidSettingsException {
        // TODO for old workflows: var symbolRelative = workflowFormatVersion.ordinal() >= LoadVersion.V230.ordinal();
        // convert bounds such that symbolRelative is always true
        if (!settings.containsKey(IOConst.UI_SETTINGS_KEY.get())) {
            return Optional.empty();
        }
        var uiSettings = settings.getConfigBase(IOConst.UI_SETTINGS_KEY.get());
        if (!uiSettings.containsKey(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get())) {
            return Optional.empty();
        }
        try {
            var bounds = uiSettings.getIntArray(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get());
            if (bounds.length != 4) {
                throw new InvalidSettingsException("Invalid node bounds, must have four entries, has " + bounds.length);
            }
            return Optional.of(new BoundsDefBuilder() //
                .setLocation(LoaderUtils.loadCoordinate(bounds[0], bounds[1])) //
                .setHeight(bounds[2]) //
                .setWidth(bounds[3]) //
                .build());
        } catch (InvalidSettingsException e) {
            var errorMessage = String.format("Unable to load the UI Bounds for the node id %d: %s",
                settings.getInt("id"), e.getMessage());
            throw new InvalidSettingsException(errorMessage, e);
        }
    }

    /**
     * TODO Not used?
     *
     * @param settings
     * @param loadVersion
     * @return
     * @throws InvalidSettingsException
     */
    static String loadUIInfoClassName(final ConfigBaseRO settings, final LoadVersion loadVersion)
        throws InvalidSettingsException {
        try {
            if (loadVersion.isOlderThan(LoadVersion.V200)) {
                if (settings.containsKey(IOConst.EXTRA_INFO_CLASS_NAME_KEY.get())) {
                    return settings.getString(IOConst.EXTRA_INFO_CLASS_NAME_KEY.get());
                }
            } else {
                if (settings.containsKey("ui_classname")) {
                    return settings.getString("ui_classname");
                }
            }
        } catch (InvalidSettingsException e) {
            var errorMessage = String.format(
                "Unable to load UI information class name to node with ID suffix %s, no UI information available: %s",
                settings.getInt("id"), e.getMessage());
            throw new InvalidSettingsException(errorMessage, e);
        }
        return null;
    }
}
