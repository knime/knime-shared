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

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;

import org.apache.commons.logging.LogFactory;
import org.knime.core.node.util.CheckUtils;

/**
 * A utility to track load averages over different intervals. The load average is calculated as an exponentially
 * weighted moving average (EWMA) over given intervals. The tracker is updated periodically.
 *
 * @author Bernd Wiswedel
 * @param <K> The type of the key to distinguish different load averages. This is often an enum or a String and is
 *            purely a convenience for the consuming class to distinguish different load averages.
 * @since 6.5
 */
public final class LoadTracker<K> implements AutoCloseable {

    private static final int DECAY_OFFSET = 0;

    private static final int LOAD_AVERAGE_OFFSET = 1;

    /** The number of times errors retrieving the current value will be logged, avoid log flood. */
    private static final int MAX_ERROR_LOGS = 10;

    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger(0);

    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1,
        r -> new Thread(r, "KNIME-LoadTracker-Updater-%d".formatted(THREAD_COUNTER.incrementAndGet())));

    private final Map<K, double[]> m_loadAverageMap;

    private final DoubleSupplier m_measure;

    private final ScheduledFuture<?> m_updateFuture;

    private final boolean m_isIgnoreCloseInvocation;

    private int m_nrErrorsLogged;

    private LoadTracker(final Builder<K> b) {
        final var updateIntervalInMillis = b.m_updateInterval.toMillis();
        m_measure = b.m_measure;
        final var initialLoadAverage = m_measure.getAsDouble();
        m_loadAverageMap = b.m_keyToIntervalMap.entrySet().stream().collect(Collectors.toMap( //
            Entry::getKey, //
            e -> new double[]{ //
                Math.exp(-updateIntervalInMillis / (double)e.getValue().toMillis()), //
                initialLoadAverage}, //
            (t, u) -> t, //
            LinkedHashMap::new));
        m_isIgnoreCloseInvocation = b.m_isIgnoreCloseInvocation;
        m_updateFuture = EXECUTOR.scheduleAtFixedRate(this::update, 0, updateIntervalInMillis, TimeUnit.MILLISECONDS);
    }

    private void update() {
        final double value;
        try {
            value = m_measure.getAsDouble();
        } catch (Exception e) { // NOSONAR (3rd party client code
            m_nrErrorsLogged += 1;
            if (m_nrErrorsLogged <= MAX_ERROR_LOGS) {
                var messageBuilder = new StringBuilder("Error computing load value");
                if (m_nrErrorsLogged == MAX_ERROR_LOGS) {
                    messageBuilder.append(
                        " - error count exceeded (%d), will not report future errors".formatted(MAX_ERROR_LOGS));
                }
                LogFactory.getLog(LoadTracker.class).error(messageBuilder.toString(), e);
            }
            return;
        }
        for (final var average : m_loadAverageMap.values()) {
            final double decay = average[DECAY_OFFSET];
            average[LOAD_AVERAGE_OFFSET] = average[LOAD_AVERAGE_OFFSET] * decay + value * (1 - decay);
        }
    }

    /**
     * @return the nrErrorsLogged (used in test)
     */
    int getNrErrorsLogged() {
        return m_nrErrorsLogged;
    }

    @Override
    public void close() {
        if (!m_isIgnoreCloseInvocation) {
            m_updateFuture.cancel(true);
        }
    }

    /**
     * Retrieve current load estimate for a given interval identifier (key).
     * @param key The key to distinguish the different load averages, as specified during build.
     * @return The current load average for the given key
     * @throws IllegalArgumentException If the key is unknown.
     * @see #getLoadAverage()
     */
    public double getLoadAverage(final K key) {
        return m_loadAverageMap.get(key)[LOAD_AVERAGE_OFFSET];
    }

    /**
     * Retrieve the current load estimate for the first (often only) interval that was specified during construction.
     *
     * @return The current load average.
     * @see #getLoadAverage(Object)
     */
    public double getLoadAverage() {
        return m_loadAverageMap.values().iterator().next()[LOAD_AVERAGE_OFFSET];
    }

    /**
     * Builds and starts a tracker that's tracks a single measure with a single interval. The single load average value
     * is to be retrieved via {@link #getLoadAverage()}.
     *
     * @param updateInterval The frequency with which the load average is updated, often in second range)
     * @param measure The supplying measure.
     * @param interval The interval over which the load average is calculated (e.g. 1min, 5min, etc)
     * @return The tracker
     */
    public static LoadTracker<Void> singleLoadTracker(final Duration updateInterval, final DoubleSupplier measure,
        final Duration interval) {
        return LoadTracker.<Void> builder(updateInterval, measure).addInterval(null, interval).start();
    }

    /**
     * Builder for {@link LoadTracker}.
     *
     * @param <K> The type of the key to distinguish different load averages, often an enum or String. On the builder
     *            instance, different load average intervals need to be added via
     *            {@link Builder#addInterval(Object, Duration)} (at least one)
     * @param updateInterval The update interval for the load updates.
     * @param measure The measure to track.
     * @return A builder to be further configured and finally started via {@link Builder#start()}.
     */
    public static <K> Builder<K> builder(final Duration updateInterval, final DoubleSupplier measure) {
        return new Builder<>(updateInterval, measure);
    }

    /**
     * A Builder returned by #builder(Duration, DoubleSupplier).
     * @param <K> Types of keys to distinguish different load averages.
     */
    public static final class Builder<K> {

        private final Duration m_updateInterval;

        private final Map<K, Duration> m_keyToIntervalMap;

        private final DoubleSupplier m_measure;

        private boolean m_isIgnoreCloseInvocation;

        private Builder(final Duration updateInterval, final DoubleSupplier measure) {
            CheckUtils.checkArgument(updateInterval.toMillis() > 0, "Update interval must be positive");
            m_measure = CheckUtils.checkArgumentNotNull(measure);
            m_updateInterval = updateInterval;
            m_keyToIntervalMap = new LinkedHashMap<>();
            m_isIgnoreCloseInvocation = false;
        }

        /**
         * Adds an interval for which the load average is calculated under a given key.
         *
         * @param key The key to distinguish the different load averages (this is defined by the consuming class and not
         *            relevant for this tracker to function).
         * @param interval The interval (e.g. load averages on *nix systems track averages over 1, 5, and 15 minutes).
         * @return this builder
         */
        public Builder<K> addInterval(final K key, final Duration interval) {
            CheckUtils.checkArgument(interval.compareTo(m_updateInterval) > 0,
                "Observation period (%d) must be larger than update interval (%d)",
                interval, m_updateInterval);
            m_keyToIntervalMap.put(key, interval);
            return this;
        }

        /**
         * Whether to ignore close invocation. If set to {@code true}, the tracker will not be stopped when
         * {@link LoadTracker#close()} is invoked. This is usually the case for static trackers.
         *
         * @param isIgnoreCloseInvocation That property. Default is false (i.e. close invocation will stop the tracker).
         * @return this builder
         */
        public Builder<K> setIgnoreCloseInvocation(final boolean isIgnoreCloseInvocation) {
            m_isIgnoreCloseInvocation = isIgnoreCloseInvocation;
            return this;
        }

        /**
         * Starts and returns the tracker.
         * @return The tracker
         */
        public LoadTracker<K> start() {
            CheckUtils.checkState(!m_keyToIntervalMap.isEmpty(), "At least one interval must be added");
            return new LoadTracker<>(this);
        }
    }

}
