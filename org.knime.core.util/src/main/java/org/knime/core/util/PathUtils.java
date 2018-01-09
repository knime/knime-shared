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
 * ---------------------------------------------------------------------
 *
 * Created on 30.10.2013 by thor
 */
package org.knime.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

/**
 * Utility function based around the new Path API in Java 7.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 5.0
 */
public final class PathUtils {
    private PathUtils() {
    }

    private static final List<Path> TEMP_FILES = Collections.synchronizedList(new ArrayList<Path>());

    /**
     * Permission set in which everybody has all permissions.
     */
    public static final Set<PosixFilePermission> RWX_ALL_PERMISSIONS;

    static {
        Set<PosixFilePermission> perms = new HashSet<>();
        //add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        RWX_ALL_PERMISSIONS = Collections.unmodifiableSet(perms);

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
     * Removes all entried in the given directory recursively.
     *
     * @param startDir start directory whose contents should be deleted
     * @throws IOException if an I/O error occurs while deleting the directory
     * @since 5.2
     */
    public static void cleanDirectoryIfExists(final Path startDir) throws IOException {
        if (!Files.exists(startDir)) {
            return;
        }

        try (DirectoryStream<Path> dirContents = Files.newDirectoryStream(startDir)) {
            for (Path e : dirContents) {
                if (Files.isDirectory(e)) {
                    deleteDirectoryIfExists(e);
                } else {
                    Files.delete(e);
                }
            }
        }
    }


    /**
     * Deletes the given directory recursively.
     *
     * @param startDir start directory that should be deleted
     * @throws IOException if an I/O error occurs while deleting the directory
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
     * @throws IOException if an I/O error occurs while deleting the directory
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
     * Creates a temporary file that is automatically deleted when the JVM shuts down.
     *
     * @param dir the directory in which the file is to be created
     * @param prefix the prefix string to be used in generating the file's name
     * @param suffix the suffix string to be used in generating the file's name
     *
     * @return an abstract pathname denoting a newly-created empty file
     * @throws IOException if the file could not be created
     * @since 5.1
     */
    public static Path createTempFile(final Path dir, final String prefix, final String suffix) throws IOException {
        Path tempFile = Files.createTempFile(dir, prefix, suffix);
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
     * Creates a symlink that is automatically deleted when the JVM shuts down.
     *
     * @param link the path the symlink will point to
     * @param target the target path to create the symlink
     * @return the path object of the newly created symlink
     * @throws IOException if an I/O exception occurs while creating symlink
     * @throws UnsupportedOperationException if the file system does not support symbolic links
     * @since 5.7
     */
    public static Path createSymLink(final Path link, final Path target) throws IOException {
        Path symLink = Files.createSymbolicLink(link, target);
        TEMP_FILES.add(symLink);
        return symLink;
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
     * @return the number of files (not directories!) that have been copied
     * @throws IOException if an I/O error occurs while copying
     */
    public static long copyDirectory(final Path source, final Path destination, final PathFilter filter)
        throws IOException {
        final AtomicLong copiedFiles = new AtomicLong();

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
                    copiedFiles.incrementAndGet();
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return copiedFiles.get();
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
     * @return the number of files (not directories!) that have been moved
     * @throws IOException if an I/O error occurs while copying
     */
    public static long moveDirectory(final Path source, final Path destination, final PathFilter filter)
        throws IOException {
        final AtomicLong movedFiles = new AtomicLong();

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
                    movedFiles.incrementAndGet();
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return movedFiles.get();
    }


    /**
     * Recursively counts the files (not directories!) in the given directory. Optionally a path filter can be supplied.
     *
     * @param directory the starting directory
     * @param filter a filter for files that should be counted
     * @return the number of files (not directories!)
     * @throws IOException if an I/O error occurs while traversing the directory tree
     * @since 5.1
     */
    public static long countFiles(final Path directory, final PathFilter filter)
        throws IOException {
        final AtomicLong count = new AtomicLong();

        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
                if (filter.accept(dir)) {
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
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (filter.accept(file)) {
                    count.incrementAndGet();
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return count.get();
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

    /**
     * Recursively sets the permission of the given path.
     *
     * @param path any path
     * @param perms the new permissions
     * @throws IOException if an error occurs while setting the permissions and/or traversing the filesystem
     */
    public static void chmodRecursively(final Path path, final Set<PosixFilePermission> perms) throws IOException {
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
                Files.setPosixFilePermissions(dir, perms);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Files.setPosixFilePermissions(file, perms);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                } else {
                    return FileVisitResult.CONTINUE;
                }
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                } else {
                    return FileVisitResult.CONTINUE;
                }
            }
        });
    }

    /**
     * This method does the same as {@link Path#resolve(String)} except that it handles the case when the child path
     * starts with a separator. Instead of starting from the root again it really appends the child path to the parent.
     * I.e. <tt>resolvePath("/foo", "/bar")</tt> will result in <tt>/foo/bar</tt> instead of <tt>/bar</tt>.
     *
     * @param parent the parent path
     * @param child the child path
     * @return the resolved path
     */
    public static Path resolvePath(final Path parent, final String child) {
        final String separator = parent.getFileSystem().getSeparator();
        if (child.startsWith(separator)) {
            return parent.resolve(child.substring(separator.length()));
        } else if (child.startsWith("/")) {
            return parent.resolve(child.substring(1));
        } else {
            return parent.resolve(child);
        }
    }

    /**
     * Stores the content of the zip stream in the specified directory.
     *
     * @param zipStream must contain a zip archive. Is unpacked an stored in the specified directory.
     * @param dir the destination directory the content of the zip stream is stored in
     * @throws IOException if it was not able to store the content
     * @since 5.4
     */
    public static void unzip(final ZipInputStream zipStream, final Path dir) throws IOException {
        ZipEntry e;
        while ((e = zipStream.getNextEntry()) != null) {
            String name = e.getName().replace('\\', '/');

            if (e.isDirectory()) {
                if (!name.isEmpty() && !name.equals("/")) {
                    Path d = dir.resolve(name);
                    Files.createDirectories(d);
                }
            } else {
                Path f = dir.resolve(name);
                Files.createDirectories(f.getParent());

                try (OutputStream out = Files.newOutputStream(f)) {
                    IOUtils.copyLarge(zipStream, out);
                }
                Files.setLastModifiedTime(f, e.getLastModifiedTime());
            }
        }
    }
}
