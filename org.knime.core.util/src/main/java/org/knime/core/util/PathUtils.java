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
 * Created on 30.10.2013 by thor
 */
package com.knime.enterprise.repository.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility function based around the new Path API in Java 7.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @since 4.5
 */
public final class PathUtils {
    private PathUtils() {
    }

    private static final List<Path> TEMP_FILES = Collections.synchronizedList(new ArrayList<Path>());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (Path p : TEMP_FILES) {
                    if (!Files.exists(p)) {
                        continue;
                    }

                    try {
                        if (Files.isRegularFile(p)) {
                            Files.delete(p);
                        } else if (Files.isDirectory(p)) {
                            deleteDirectoryIfExists(p);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Deletes the given directory recursively.
     *
     * @param startDir start directory that should be deleted
     * @throws IOException if an I/O error occurs file deleting the directory
     */
    public static void deleteDirectoryIfExists(final Path startDir) throws IOException {
        deleteDirectoryIfExists(startDir, PathFilters.acceptAll);
    }

    /**
     * Deletes the given directory recursively using a path filter. Note that the directory may still exist afterwards
     * if not all files were removed because of the filter.
     *
     * @param startDir start directory that should be deleted
     * @param filter a path filter
     * @throws IOException if an I/O error occurs file deleting the directory
     */
    public static void deleteDirectoryIfExists(final Path startDir, final PathFilter filter) throws IOException {
        if (!Files.exists(startDir)) {
            return;
        }

        Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (filter.accept(file)) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
                if (filter.accept(dir)) {
                    return FileVisitResult.CONTINUE;
                } else {
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                if (exc == null) {
                    if ((filter == PathFilters.acceptAll) || isEmpty(dir)) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                } else {
                    throw exc;
                }
            }
        });
    }

    /**
     * Creates a temporary directory that is automatically deleted when the JVM shuts down.
     *
     * @param prefix the prefix string to be used in generating the file's name
     *
     * @return an abstract pathname denoting a newly-created empty directory
     * @throws IOException if the directory could not be created
     */
    public static Path createTempDir(final String prefix) throws IOException {
        return createTempDir(prefix, null);
    }

    /**
     * Creates a temporary file that is automatically deleted when the JVM shuts down.
     *
     * @param prefix the prefix string to be used in generating the file's name
     * @param suffix the suffix string to be used in generating the file's name
     *
     * @return an abstract pathname denoting a newly-created empty file
     * @throws IOException if the file could not be created
     */
    public static Path createTempFile(final String prefix, final String suffix) throws IOException {
        Path tempFile = Files.createTempFile(prefix, suffix);
        TEMP_FILES.add(tempFile);
        return tempFile;
    }

    /**
     * Creates a temporary directory that is automatically deleted when the JVM shuts down.
     *
     * @param prefix the prefix string to be used in generating the file's name
     * @param dir the directory in which the file is to be created, or <code>null</code> if the default temporary-file
     *            directory is to be used
     * @return an abstract pathname denoting a newly-created empty directory
     * @throws IOException if the directory could not be created
     */
    public static Path createTempDir(final String prefix, final Path dir) throws IOException {
        Path rootDir = (dir == null) ? Paths.get(System.getProperty("java.io.tmpdir")) : dir;

        Path tempDir = Files.createTempDirectory(rootDir, prefix);
        TEMP_FILES.add(tempDir);
        return tempDir;
    }

    /**
     * Recursively copied the source directory to the destination directory. The source directory is not copied
     * <em>into</em> the destination but the destination directory has the same contents as the source directory in the
     * end. If the destination directory already exists, the contents are merged with the source taking precedence.
     *
     * @param source the source directory
     * @param destination the destination directory
     * @throws IOException if an I/O error occurs while copying
     */
    public static void copyDirectory(final Path source, final Path destination) throws IOException {
        copyDirectory(source, destination, PathFilters.acceptAll);
    }

    /**
     * Recursively copied the source directory to the destination directory. The source directory is not copied
     * <em>into</em> the destination but the destination directory has the same contents as the source directory in the
     * end. If the destination directory already exists, the contents are merged with the source taking precedence.
     *
     * @param source the source directory
     * @param destination the destination directory
     * @param filter a filter for files that should be moved
     * @throws IOException if an I/O error occurs while copying
     */
    public static void copyDirectory(final Path source, final Path destination, final PathFilter filter)
        throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
                if (filter.accept(dir)) {
                    Files.createDirectories(destination.resolve(source.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                } else {
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (filter.accept(file)) {
                    Files.copy(file, destination.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }


    /**
     * Recursively moves the source directory to the destination directory. The source directory is not moved
     * <em>into</em> the destination but the destination directory has the same contents as the source directory in the
     * end. If the destination directory already exists, the contents are merged with the source taking precedence.
     *
     * @param source the source directory
     * @param destination the destination directory
     * @throws IOException if an I/O error occurs while copying
     */
    public static void moveDirectory(final Path source, final Path destination) throws IOException {
        if (!Files.isDirectory(destination)
            && Files.getFileStore(source).equals(Files.getFileStore(destination.getParent()))) {
            Files.move(source, destination);
        } else {
            moveDirectory(source, destination, PathFilters.acceptAll);
        }
    }

    /**
     * Recursively moves the source directory to the destination directory. The source directory is not moved
     * <em>into</em> the destination but the destination directory has the same contents as the source directory in the
     * end. If the destination directory already exists, the contents are merged with the source taking precedence. A
     * filter can be specified in order to skip certain files for directories while moving. Note that the source
     * directory may still exists afterwards if some files have not been moved because of the filter. Also directories
     * are not moved but only their contents
     *
     * @param source the source directory
     * @param destination the destination directory
     * @param filter a filter for files that should be moved
     * @throws IOException if an I/O error occurs while copying
     */
    public static void moveDirectory(final Path source, final Path destination, final PathFilter filter)
        throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
                if (filter.accept(dir)) {
                    Files.createDirectories(destination.resolve(source.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                } else {
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }
                if (filter.accept(dir) && isEmpty(dir)) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (filter.accept(file)) {
                    Files.move(file, destination.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Returns whether the given directory is empty.
     *
     * @param directory any directory
     * @return <code>true</code> if the directory is empty, <code>false</code> otherwise
     * @throws IOException if the path is not a directory of if an I/O error occurs
     */
    public static boolean isEmpty(final Path directory) throws IOException {
        try (DirectoryStream<Path> dirContents = Files.newDirectoryStream(directory)) {
            return !dirContents.iterator().hasNext();
        }
    }
}
