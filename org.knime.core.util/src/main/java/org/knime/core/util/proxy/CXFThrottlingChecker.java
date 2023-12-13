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
 *   Jul 25, 2023 (Leon Wenzler, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.util.proxy;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

import org.knime.core.node.util.CheckUtils;

/**
 * Checks whether REST requests using CXF should be throttled. This is due to CXF having thread
 * pile-up issues, where AP instances can even go OOM. See their issue
 * <a href="https://issues.apache.org/jira/browse/CXF-8885">CXF-8885</a>.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.3
 */
public final class CXFThrottlingChecker {

    /**
     * Property that is introduced with AP 5.1.3 / 5.2.0 to manually set an upper bound of REST-related
     * threads, upon which requests are throttled by exponentially increasing wait times.
     */
    public static final String THROTTLE_THREAD_LIMIT = "org.knime.cxf.throttle.maxThreads";

    // base time to wait when limit is reached
    private static final int BASE_WAIT_MS = 100;

    // multiplier of base time, i.e. doubled wait every ten new threads
    private static final double WAIT_MULTIPLIER = Math.pow(2.0, 1.0 / 10.0);

    // interval in which the waiting thread is woken up to update
    private static final int POLL_INTERVAL_MS = 500;

    // if we wait for a long time, support recovery from waiting with GC
    private static final int GC_INTERVAL_MS = 30000;

    private static int configuredThreadLimit;

    static {
        try {
            configuredThreadLimit = Integer.parseInt(System.getProperty(THROTTLE_THREAD_LIMIT));
        } catch (NumberFormatException ignored) { // NOSONAR
            // default to no throttling
            configuredThreadLimit = -1;
        }
    }

    private static final Predicate<Thread> INDICATIVE_THREAD_NAME = thread -> {
        final var name = thread.getName();
        return name.startsWith("HttpClient") || name.startsWith("I/O dispatcher") || name.startsWith("CXF");
    };

    /**
     * Whether throttling is currently performed.
     *
     * @return is current wait time per call greater zero?
     */
    public static boolean isThrottling() {
        return getCurrentWaitTime() > 0;
    }

    /**
     * Calculates the current wait time, based on the formula
     * <tt>T = {@value #BASE_WAIT_MS}ms * 2^(#threadsOverLimit / 10)</tt>
     *
     * @return current wait time, depending on the number of threads.
     */
    private static long getCurrentWaitTime() {
        if (configuredThreadLimit <= 0) {
            return 0;
        }
        final var threadsOverLimit = Thread.getAllStackTraces().keySet().stream()//
            .filter(INDICATIVE_THREAD_NAME)//
            .count() - configuredThreadLimit;
        if (threadsOverLimit <= 0) {
            return 0;
        }
        return (long)(BASE_WAIT_MS * Math.pow(WAIT_MULTIPLIER, threadsOverLimit));
    }

    /**
     * Adds support for throttling based on a thread limit. If the {@link #configuredThreadLimit}
     * is greater or equal to one, this method waits until the current number REST-related threads
     * (specified by {@link #INDICATIVE_THREAD_NAME}) is below the limit.
     * <p>
     * The wait time is calculated as <tt>T = {@value #BASE_WAIT_MS}ms * 2^(#threadsOverLimit / 10)</tt>.
     * This means the throttling time is getting increased exponentially.
     * However, every <tt>{@value #POLL_INTERVAL_MS}ms</tt> the current thread state is checked again,
     * and the wait time potentially decreased (never increased from the original estimate).
     * <p>
     * Invokes the garbage collector roughly every {@value #GC_INTERVAL_MS}ms of waiting time.
     *
     * @param <T> Result type parameter.
     * @param task The task that will return a result of type T. Will usually perform a request.
     * @return Result of type T.
     * @throws Exception generic exception if thread or execution got cancelled.
     */
    public static <T> T callThrottled(final Callable<T> task) throws Exception {
        return callThrottledWithMonitor(task, CheckCanceledMonitor.NOOP_MONITOR);
    }

    /**
     * Adds support for throttling based on a thread limit. If the {@link #configuredThreadLimit}
     * is greater or equal to one, this method waits until the current number REST-related threads
     * (specified by {@link #INDICATIVE_THREAD_NAME}) is below the limit.
     * <p>
     * The wait time is calculated as <tt>T = 100ms * 2^(#threadsOverLimit / 10)</tt>.
     * This means the throttling time is getting increased exponentially.
     * However, every <tt>500ms</tt>, the current thread state is checked again,
     * and the wait time potentially decreased (never increased from the original estimate).
     * <p>
     * Invokes the garbage collector roughly every {@value #GC_INTERVAL_MS}ms of waiting time.
     *
     * @param <T> Result type parameter.
     * @param task The task that will return a result of type T. Will usually perform a request.
     * @param monitor Execution monitor that checks for cancellation
     * @return Result of type T.
     * @throws Exception generic exception if thread or execution got cancelled.
     */
    public static <T> T callThrottledWithMonitor(final Callable<T> task, final CheckCanceledMonitor monitor)
        throws Exception {
        CheckUtils.checkArgumentNotNull(task);
        CheckUtils.checkArgumentNotNull(monitor);
        long lastPerformedGC = 0;
        long waitUntilMillis = System.currentTimeMillis() + getCurrentWaitTime();
        while (System.currentTimeMillis() < waitUntilMillis) {
            monitor.checkCanceled();
            Thread.sleep(POLL_INTERVAL_MS);
            lastPerformedGC += POLL_INTERVAL_MS;
            // call GC roughly in the interval of 30s
            if (lastPerformedGC > GC_INTERVAL_MS) {
                System.gc(); // NOSONAR
                lastPerformedGC = 0;
            }
            // update wait deadline, but limit to original estimate as upper bound
            // this avoids infinite waiting
            waitUntilMillis =
                Math.min(System.currentTimeMillis() + getCurrentWaitTime(), waitUntilMillis);
        }
        return task.call(); // threads are below limit, call now
    }

    /**
     * Only a utility class.
     */
    private CXFThrottlingChecker() {
    }

    /**
     * To be able to cancel waiting when throttling.
     *
     * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
     */
    @FunctionalInterface
    public interface CheckCanceledMonitor {
        /**
         * {@link CheckCanceledMonitor} that does nothing.
         */
        CheckCanceledMonitor NOOP_MONITOR = () -> {};

        /**
         * @throws Exception If the execution of the has been canceled during waiting.
         */
        void checkCanceled() throws Exception; // NOSONAR
    }
}
