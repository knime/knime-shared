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
import java.io.InputStream;
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
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

    private static final PermissionsHandler PERM_HANDLER = System.getProperty("os.name").startsWith("Windows") ?
        new WindowsPermissionsHandler(): new UnixPermissionsHandler();

    private interface PermissionsHandler {
        void setArchiveMode(ZipArchiveEntry e, Path p) throws IOException;
        void setFileMode(ZipArchiveEntry e, Path p) throws IOException;
    }

    private static class WindowsPermissionsHandler implements PermissionsHandler {
        @Override
        public void setArchiveMode(final ZipArchiveEntry e, final Path p) throws IOException {
            // nothing to do
        }

        @Override
        public void setFileMode(final ZipArchiveEntry e, final Path p) throws IOException {
            // nothing to do
        }
    }

    private static class UnixPermissionsHandler implements PermissionsHandler {
        @Override
        public void setArchiveMode(final ZipArchiveEntry entry, final Path path) throws IOException {
            Set<PosixFilePermission> p = Files.getPosixFilePermissions(path);
            int mode = (p.contains(PosixFilePermission.OWNER_READ) ? 0400 : 0) |
                    (p.contains(PosixFilePermission.OWNER_WRITE) ? 0200 : 0) |
                    (p.contains(PosixFilePermission.OWNER_EXECUTE) ? 0100 : 0) |
                    (p.contains(PosixFilePermission.GROUP_READ) ? 0040 : 0) |
                    (p.contains(PosixFilePermission.GROUP_WRITE) ? 0020 : 0) |
                    (p.contains(PosixFilePermission.GROUP_EXECUTE) ? 0010 : 0) |
                    (p.contains(PosixFilePermission.OTHERS_READ) ? 0004 : 0) |
                    (p.contains(PosixFilePermission.OTHERS_WRITE) ? 0002 : 0) |
                    (p.contains(PosixFilePermission.OTHERS_EXECUTE) ? 0001 : 0);
            entry.setUnixMode(mode);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setFileMode(final ZipArchiveEntry entry, final Path path) throws IOException {
            int mode = entry.getUnixMode();
            if (mode > 0) {
                Set<PosixFilePermission> perms = new HashSet<>();
                if ((mode & 0400) != 0) {
                    perms.add(PosixFilePermission.OWNER_READ);
                }
                if ((mode & 0200) != 0) {
                    perms.add(PosixFilePermission.OWNER_WRITE);
                }
                if ((mode & 0100) != 0) {
                    perms.add(PosixFilePermission.OWNER_EXECUTE);
                }
                if ((mode & 0040) != 0) {
                    perms.add(PosixFilePermission.GROUP_READ);
                }
                if ((mode & 0020) != 0) {
                    perms.add(PosixFilePermission.GROUP_WRITE);
                }
                if ((mode & 0010) != 0) {
                    perms.add(PosixFilePermission.GROUP_EXECUTE);
                }
                if ((mode & 0004) != 0) {
                    perms.add(PosixFilePermission.OTHERS_READ);
                }
                if ((mode & 0002) != 0) {
                    perms.add(PosixFilePermission.OTHERS_WRITE);
                }
                if ((mode & 0001) != 0) {
                    perms.add(PosixFilePermission.OTHERS_EXECUTE);
                }
                Files.setPosixFilePermissions(path, perms);
            }
        }
    }


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

        ShutdownHelper.getInstance().appendShutdownHook(() -> {
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
     * Creates a temporary file that is automatically deleted when the JVM shuts down. If you want to explicitly delete
     * the file in your code please use {@link #deleteFileIfExists(Path)} so that it is removed from the list of files
     * to delete during shutdown.
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
     * Creates a temporary file that is automatically deleted when the JVM shuts down. If you want to explicitly delete
     * the file in your code please use {@link #deleteFileIfExists(Path)} so that it is removed from the list of files
     * to delete during shutdown.
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

    /**
     * Zips the contents of the given directory (and its subdirectories) into the provided stream.
     *
     * @param dir the directory to ZIP
     * @param out the output stream to which the compressed contents will be written
     * @param compressionLevel the compression level (0-9)
     * @throws IOException if an I/O error occurs
     * @since 5.9
     */
    public static void zip(final Path dir, final OutputStream out, final int compressionLevel) throws IOException {
        try (ZipOutputStream zout = new ZipOutputStream(out)) {
            zout.setLevel(compressionLevel);
            zip(dir, zout);
        }
    }

    /**
     * Zips the contents of the given directory (and its subdirectories) into the provided ZIP stream. The stream will
     * not be closed by this method
     *
     * @param dir the directory to ZIP
     * @param zout an existing ZIP output stream
     * @throws IOException if an I/O error occurs
     * @since 5.9
     */
    public static void zip(final Path dir, final ZipOutputStream zout) throws IOException {
        byte[] buf = new byte[16384];

        Deque<Path> queue = new ArrayDeque<>(32);
        queue.push(dir);

        while (!queue.isEmpty()) {
            Path p = queue.poll();
            if (Files.isDirectory(p)) {
                if (p != dir) {
                    // don't add an (empty) entry for the root directory itself
                    zout.putNextEntry(new ZipEntry(dir.relativize(p) + "/"));
                }
                try (DirectoryStream<Path> contents = Files.newDirectoryStream(p)) {
                    contents.forEach(e -> queue.add(e));
                }
            } else {
                zout.putNextEntry(new ZipEntry(dir.relativize(p).toString()));
                try (InputStream is = Files.newInputStream(p)) {
                    IOUtils.copyLarge(is, zout, buf);
                }
            }
        }
    }

    /**
     * Zips the contents of the given directory (and its subdirectories) into the provided ZIP stream. The stream will
     * not be closed by this method. In contrast to {@link #zip(Path, ZipOutputStream)} this method is capable of adding
     * Unix file permissions to the ZIP file.
     *
     * @param dir the directory to ZIP
     * @param zout an existing ZIP output stream
     * @throws IOException if an I/O error occurs
     * @since 5.9
     */
    public static void zip(final Path dir, final ZipArchiveOutputStream zout) throws IOException {
        zip(dir, zout, false);
    }

    /**
     * Zips the contents of the given directory (and its subdirectories) into the provided ZIP stream. The stream will
     * not be closed by this method. In contrast to {@link #zip(Path, ZipOutputStream)} this method is capable of adding
     * Unix file permissions to the ZIP file.
     *
     * @param dir the directory to ZIP
     * @param zout an existing ZIP output stream
     * @param includeRootDir <code>true</code> if the start directory should be included in the zip file and all
     *            archived files prefixed with it, <code>false</code> only the contents of the start directory should be
     *            added
     * @throws IOException if an I/O error occurs
     * @since 5.9
     */
    public static void zip(final Path dir, final ZipArchiveOutputStream zout, final boolean includeRootDir)
        throws IOException {
        byte[] buf = new byte[16384];

        Deque<Path> queue = new ArrayDeque<>(32);
        queue.push(dir);

        String prefix = includeRootDir ? dir.getFileName().toString() + "/" : "";

        while (!queue.isEmpty()) {
            Path p = queue.poll();
            if (Files.isDirectory(p)) {
                if (includeRootDir || (p != dir)) {
                    // only add an (empty) entry for the root directory itself if requested
                    String name = prefix + dir.relativize(p);
                    ZipArchiveEntry entry = new ZipArchiveEntry(name.endsWith("/") ? name : name + "/");
                    entry.setTime(Files.getLastModifiedTime(p).toMillis());
                    PERM_HANDLER.setArchiveMode(entry, p);
                    zout.putArchiveEntry(entry);
                    zout.closeArchiveEntry();
                }
                try (DirectoryStream<Path> contents = Files.newDirectoryStream(p)) {
                    contents.forEach(e -> queue.add(e));
                }
            } else {
                ZipArchiveEntry entry = new ZipArchiveEntry(prefix + dir.relativize(p));
                entry.setTime(Files.getLastModifiedTime(p).toMillis());
                entry.setSize(Files.size(p));
                PERM_HANDLER.setArchiveMode(entry, p);
                zout.putArchiveEntry(entry);
                try (InputStream is = Files.newInputStream(p)) {
                    IOUtils.copyLarge(is, zout, buf);
                }
                zout.closeArchiveEntry();
            }
        }
    }


    /**
     * Stores the content of the zip file in the specified directory. This method is capable of restoring file
     * permissions stored in the ZIP under Linux and macOS.
     *
     * @param zipFile a ZIP file
     * @param dir the destination directory the content of the zip file is stored in
     * @throws IOException if it was not able to store the content
     * @since 5.9
     */
    public static void unzip(final ZipFile zipFile, final Path dir) throws IOException {
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            String name = entry.getName().replace('\\', '/');

            if (entry.isDirectory()) {
                if (!name.isEmpty() && !name.equals("/")) {
                    Path d = dir.resolve(name);
                    Files.createDirectories(d);
                    PERM_HANDLER.setFileMode(entry, d);
                }
            } else {
                Path f = dir.resolve(name);
                Files.createDirectories(f.getParent());

                try (OutputStream out = Files.newOutputStream(f); InputStream in = zipFile.getInputStream(entry)) {
                    IOUtils.copyLarge(in, out);
                }
                Files.setLastModifiedTime(f, entry.getLastModifiedTime());
                PERM_HANDLER.setFileMode(entry, f);
            }
        }
    }

    /**
     * Deletes the given file if it exists. In case it's temporary file created by {@link PathUtils} then it's removed
     * from the list of files to delete during shutdown.
     *
     * @param p a path
     * @throws IOException if an I/O error occurs
     * @since 5.13
     */
    public static void deleteFileIfExists(final Path p) throws IOException {
        Files.deleteIfExists(p);
        TEMP_FILES.remove(p);
    }
}
