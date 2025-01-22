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
 *   Jan 22, 2025 (wiswedel): created
 */
package org.knime.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.function.DoubleSupplier;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.knime.core.util.LoadTracker.Builder;

/**
 * Tests {@link LoadTracker}.
 * @author Bernd Wiswedel
 */
@SuppressWarnings("static-method")
class LoadTrackerTest {

    private static final Log LOGGER = LogFactory.getLog(LoadTracker.class);

    enum Interval {
            SHORT, LONG
    }

    @Test
    final void testLoadValuesIn1Second() throws Exception {
        @SuppressWarnings("serial")
        MutableDouble valueProvider = new MutableDouble(0.0) {
            @Override
            public synchronized Double getValue() {
                return super.getValue();
            }
        };
        try (LoadTracker<Interval> loadTracker =
            LoadTracker.<Interval> builder(Duration.ofMillis(5), valueProvider::getValue) //
                .addInterval(Interval.SHORT, Duration.ofMillis(50)) //
                .addInterval(Interval.LONG, Duration.ofMillis(500)) //
                .start()) {
            Thread.sleep(500); // NOSONAR

            // should be [0.0, 0.0]
            final double shortLoadAvg1 = loadTracker.getLoadAverage(Interval.SHORT);
            final double longLoadAvg1 = loadTracker.getLoadAverage(Interval.LONG);
            assertEquals(0.0, shortLoadAvg1, "average 50ms interval");
            assertEquals(0.0, longLoadAvg1, "average 500ms interval");

            valueProvider.setValue(100);
            Thread.sleep(500); // NOSONAR

            // should be [99.99, 63.2]
            // 63.2 due to exponential decay = 1 - e^(-1)
            final double shortLoadAvg2;
            final double longLoadAvg2;
            final double firstValue;
            synchronized (valueProvider) { // just to make sure first and short can point to the same value
                shortLoadAvg2 = loadTracker.getLoadAverage(Interval.SHORT);
                longLoadAvg2 = loadTracker.getLoadAverage(Interval.LONG);
                firstValue = loadTracker.getLoadAverage();
            }
            LOGGER.debug("Load averages: %f and %f".formatted(shortLoadAvg2, longLoadAvg2));

            assertEquals(shortLoadAvg2, firstValue, "'first value' should be equal to short load average");
            assertTrue(shortLoadAvg2 > 0.0, "average 50ms interval larger 0: %.2f".formatted(shortLoadAvg2));
            assertTrue(longLoadAvg2 > 0.0, "average 500ms interval larger 0: %.2f".formatted(longLoadAvg2));
            assertTrue(shortLoadAvg2 < 100.0, "average 50ms interval smaller 100: %.2f".formatted(shortLoadAvg2));
            assertTrue(longLoadAvg2 < 100.0, "average 500ms interval smaller 100: %.2f".formatted(longLoadAvg2));
            assertTrue(shortLoadAvg2 > longLoadAvg2,
                "average 50ms > average 500ms but: %.2f <= %.2f".formatted(shortLoadAvg2, longLoadAvg2));
            assertTrue(shortLoadAvg2 > 95.0, "average 50ms interval larger 95: %.2f".formatted(shortLoadAvg2));
            assertTrue(longLoadAvg2 > 55.0, "average 500ms interval larger 55: %.2f".formatted(longLoadAvg2));
            assertTrue(longLoadAvg2 < 68.0, "average 500ms interval smaller 68: %.2f".formatted(longLoadAvg2));
        }
    }

    @Test
    void testExceptions() {
        final Builder<Void> builder = LoadTracker.<Void> builder(Duration.ofMillis(100), () -> 0.5);
        builder.setIgnoreCloseInvocation(true); // eh, irrelevant (only to get coverage)
        assertThrows(IllegalStateException.class, builder::start, "no interval added");
        assertThrows(IllegalArgumentException.class, () -> builder.addInterval(null, Duration.ofMillis(1)), // NOSONAR
            "interval smaller than update interval");
        assertThrows(IllegalArgumentException.class, // negative interval // NOSONAR
            () -> LoadTracker.<Void> builder(Duration.ofMillis(-100), () -> 0.5),
            "interval smaller than update interval");
    }

    @Test
    void testSingleMeasure() {
        try (LoadTracker<Void> loadTracker =
            LoadTracker.singleLoadTracker(Duration.ofMillis(100), () -> 0.5, Duration.ofMillis(1000))) {
            final double load = loadTracker.getLoadAverage();
            assertEquals(0.5, load, "load measure");
        }
    }

    @Test
    void testErrorCount() throws InterruptedException {
        MutableInt count = new MutableInt();
        DoubleSupplier failSupplier = () -> {
            if (count.getAndIncrement() == 0) {
                return 1.0; // do not fail call in constructor.
            }
            throw new RuntimeException("ignored");
        };
        try (LoadTracker<Void> loadTracker =
                LoadTracker.singleLoadTracker(Duration.ofMillis(5), failSupplier, Duration.ofMillis(1000))) {
            Thread.sleep(100); // NOSONAR
            final int errorCount = loadTracker.getNrErrorsLogged();
            LOGGER.debug("Error count: %d".formatted(errorCount));
            assertTrue(errorCount > 10, "expected error count > 10: " + errorCount);
        }
    }
}
