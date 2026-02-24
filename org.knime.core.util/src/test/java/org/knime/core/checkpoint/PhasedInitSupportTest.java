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
 *   Feb 24, 2026 (wiswedel): created
 */
package org.knime.core.checkpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.junit.jupiter.api.Test;
import org.knime.core.checkpoint.PhasedInitSupport.ResourceImplementation;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 *
 * @author wiswedel
 */
final class PhasedInitSupportTest {

    @SuppressWarnings("static-method")
    @Test
    final void testRegisterOrActivate_NonPhased() {
        assertFalse(PhasedInitSupport.isSupported(), "Phased init should not be supported in test environment");
        AtomicBoolean isCalled = new AtomicBoolean(false);
        PhasedInit<RuntimeException> phasedInit = () -> {
            isCalled.set(true);
        };
        PhasedInitSupport.registerOrActivate(phasedInit);
        assertTrue(isCalled.get(), "activate should have been called immediately when phased init is not supported");
    }

    @SuppressWarnings("static-method")
    @Test
    final void testRegisterOrActivate_PhasedWithCheckpointRestore() throws Exception {
        try (var mockedPhasedInitSupport = Mockito.mockStatic(PhasedInitSupport.class, Mockito.CALLS_REAL_METHODS);
             var mockedCore = Mockito.mockStatic(Core.class)) {
            mockedPhasedInitSupport.when(PhasedInitSupport::isSupported).thenReturn(true);

            // Setup mock context and capture registered resources
            List<Resource> registeredResources = new ArrayList<>();
            @SuppressWarnings("unchecked")
            Context<Resource> mockContext = Mockito.mock(Context.class);

            Mockito.doAnswer(invocation -> {
                Resource resource = invocation.getArgument(0);
                registeredResources.add(resource);
                return null;
            }).when(mockContext).register(ArgumentMatchers.any(Resource.class));

            mockedCore.when(Core::getGlobalContext).thenReturn(mockContext);

            // Setup PhasedInit that should be activated after restore
            AtomicBoolean isActivated = new AtomicBoolean(false);
            PhasedInit<RuntimeException> phasedInit = () -> {
                isActivated.set(true);
            };

            // Register the component
            PhasedInitSupport.registerOrActivate(phasedInit);

            // Verify it was not activated immediately
            assertFalse(isActivated.get(), "activate should not be called immediately when phased init is supported");

            // Verify resources were registered
            assertEquals(2, registeredResources.size(),
                "Two resources should be registered (cleanup and phased init wrapper)");

            // Find the ResourceImplementation that wraps our PhasedInit
            Resource phasedInitResource = registeredResources.stream()
                .filter(r -> r.getClass().getSimpleName().equals(ResourceImplementation.class.getSimpleName()))
                .findFirst()
                .orElse(null);

            assertTrue(phasedInitResource != null, "PhasedInit should have been wrapped in ResourceImplementation");

            // Simulate checkpoint lifecycle: beforeCheckpoint then afterRestore
            registeredResources.forEach(r -> {
                try {
                    r.beforeCheckpoint(mockContext);
                } catch (Exception ex) {
                    fail("beforeCheckpoint should not throw an exception: " + ex.getMessage(), ex);
                }
            });
            assertFalse(isActivated.get(), "activate should still not be called after beforeCheckpoint");

            registeredResources.forEach(r -> {
                try {
                    r.afterRestore(mockContext);
                } catch (Exception ex) {
                    fail("afterRestore should not throw an exception: " + ex.getMessage(), ex);
                }
            });
            assertTrue(isActivated.get(), "activate should be called after afterRestore");
        }
    }

}
