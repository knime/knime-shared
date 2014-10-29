/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME.com, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 *
 * Created on 01.11.2013 by thor
 */
package com.knime.enterprise.repository.util;

import java.nio.file.Files;
import java.nio.file.Path;

import com.knime.enterprise.utility.KnimeServerConstants;

/**
 * This class holds some common path filters.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 */
public final class PathFilters {
    private PathFilters() {

    }

    /**
     * Filter that accepts all paths.
     */
    public static final PathFilter acceptAll = new PathFilter() {
        @Override
        public boolean accept(final Path path) {
            return true;
        }
    };

    /**
     * Filter that accepts everything except snapshot directories (see {@link KnimeServerConstants#SNAPSHOT_DIR}).
     */
    public static final PathFilter excludeSnapshotDir = new PathFilter() {
        @Override
        public boolean accept(final Path path) {
            return !(path.endsWith(KnimeServerConstants.SNAPSHOT_DIR) && Files.isDirectory(path));
        }
    };

    /**
     * Filter that accepts everything except the metainfo directories (see {@link KnimeServerConstants#METAINFO_DIR}).
     */
    public static final PathFilter excludeMetainfoDir = new PathFilter() {
        @Override
        public boolean accept(final Path path) {
            return !(path.endsWith(KnimeServerConstants.METAINFO_DIR) && Files.isDirectory(path));
        }
    };

    /**
     * Filter that accepts everything except snapshot directories and the scheduled jobs file (see
     * {@link KnimeServerConstants#SNAPSHOT_DIR} and {@link KnimeServerConstants#SCHEDULED_JOBS_FILE}).
     */
    public static final PathFilter excludeSnapshotsAndScheduledJobs = new PathFilter() {
        @Override
        public boolean accept(final Path path) {
            if (Files.isDirectory(path) && path.endsWith(KnimeServerConstants.SNAPSHOT_DIR)) {
                return false;
            }
            if (Files.isRegularFile(path) && path.endsWith(KnimeServerConstants.SCHEDULED_JOBS_FILE)) {
                return false;
            }

            return true;
        }
    };

}
