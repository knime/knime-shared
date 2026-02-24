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
 *   Feb 18, 2026 (wiswedel): created
 */
package org.knime.core.checkpoint;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for managing phased initialization with CRaC (Coordinated Restore at Checkpoint) support. For an example
 * usage, refer to {@link PhasedInit}.
 *
 * @author Bernd Wiswedel, Manuel Hotz, KNIME GmbH, Konstanz, Germany
 * @since 6.11
 * @noreference This interface is not intended to be referenced by clients (pending API)
 */
public final class PhasedInitSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhasedInitSupport.class);

    /** Name of the environment variable to enable phased initialization support. Must be set to "true" to enable. */
    public static final String ENV_VARIABLE_PHASED_INIT = "KNIME_CHECKPOINT_SUPPORT";

    private static Boolean isCheckpointingSupported;

    /** A list to keep track of registered resources. (CRaC uses weak references and will ignore otherwise). */
    private static final List<Resource> resources = new ArrayList<>();

    private PhasedInitSupport() {
    }

    /**
     * Register a component for phased initialization with CRaC support.
     *
     * <p>
     * If CRaC checkpoint support is enabled (environment variable set, jdk.crac module available, and
     * -XX:CRaCCheckpointTo JVM argument present), the component's {@link PhasedInit#activate()} is deferred
     * until after checkpoint restore. Otherwise, it is called immediately.
     * </p>
     *
     * @param phasedInit the component to initialize
     * @param <E> the type of exception possibly thrown by the activation
     * @throws E if activation is called directly and PhasedInit.activate() throws
     */
    public static <E extends Exception> void registerOrActivate(final PhasedInit<E> phasedInit) throws E {
        if (isSupported()) {
            synchronized (resources) {
                addCleanupResourceIfEmpty();
                final ResourceImplementation resource = new ResourceImplementation(phasedInit);
                resources.add(resource);
                Core.getGlobalContext().register(resource);
            }
        } else {
            phasedInit.activate();
        }
    }

    private static void addCleanupResourceIfEmpty() {
        if (resources.isEmpty()) {
            final Resource cleanupResource = new Resource() {
                @Override
                public void beforeCheckpoint(final Context<? extends Resource> context) throws Exception {
                    // nothing to do here
                }

                @Override
                public void afterRestore(final Context<? extends Resource> context) throws Exception {
                    resources.clear();
                }
            };
            resources.add(cleanupResource);
            Core.getGlobalContext().register(cleanupResource);
        }
    }

    /**
     * Checks if phased initialization with CRaC support is enabled.
     *
     * @return true if environment variable is set, jdk.crac module is available, and -XX:CRaCCheckpointTo is present
     */
    public static synchronized boolean isSupported() {
        if (isCheckpointingSupported == null) {
            if (!Boolean.parseBoolean(System.getenv(ENV_VARIABLE_PHASED_INIT))) {
                LOGGER.atDebug().log("Phased initialization support is disabled; environment variable "
                    + "'{}' is false or not set", ENV_VARIABLE_PHASED_INIT);
                isCheckpointingSupported = Boolean.FALSE;
                return false;
            }
            // check if --add-modules jdk.crac is present
            boolean isCracModuleAvailable = Optional.ofNullable(ModuleLayer.boot())
                    .flatMap(ml -> ml.findModule("jdk.crac")).isPresent();
            if (!isCracModuleAvailable) {
                LOGGER.atDebug().log("Phased initialization support is disabled; jdk.crac module not found");
                isCheckpointingSupported = Boolean.FALSE;
                return false;
            }

            // checks for the presence of the -XX:CRaCCheckpointTo JVM argument
            final List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            boolean isRunningBeforeRestore = inputArguments.stream().anyMatch(arg -> arg.contains("CRaCCheckpointTo"));
            if (!isRunningBeforeRestore) {
                LOGGER.atDebug().log("Phased initialization support is disabled; JVM not running in CRaC checkpoint "
                    + "mode (missing -XX:CRaCCheckpointTo argument)");
                isCheckpointingSupported = Boolean.FALSE;
                return false;
            }

            LOGGER.atDebug().log("Phased initialization support is enabled");
            isCheckpointingSupported = Boolean.TRUE;
        }
        return isCheckpointingSupported.booleanValue();
    }

    static final class ResourceImplementation implements Resource {
        private final PhasedInit<?> m_phasedInit;

        ResourceImplementation(final PhasedInit<?> phasedInit) {
            m_phasedInit = phasedInit;
        }

        @Override
        public void beforeCheckpoint(final Context<? extends Resource> context) throws Exception {
            // nothing to do here, we only need to react after restore
        }

        @Override
        public void afterRestore(final Context<? extends Resource> context) throws Exception {
            m_phasedInit.activate();
        }
    }

}
