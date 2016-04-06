/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
 *   05.04.2016 (thor): created
 */
package org.knime.core.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Testcases for {@link DiskBasedByteQueue}.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 */
public class DiskBasesByteQueueTest {
    private static final ExecutorService ES = Executors.newCachedThreadPool();

    private Path m_tempDir;

    /**
     * Shuts down the executor.
     */
    @AfterClass
    public static void shutdownExecutor() {
        ES.shutdownNow();
    }

    /**
     * Creates a temporary directory for each test method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Before
    public void setup() throws IOException {
        m_tempDir = PathUtils.createTempDir(getClass().getName());
    }

    /**
     * Deletes the temporary directory.
     *
     * @throws IOException if an I/O error occurs
     */
    @After
    public void tearDown() throws IOException {
        PathUtils.deleteDirectoryIfExists(m_tempDir);
    }

    /**
     * Basic pure in-memory check with a single thread.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 2000)
    public void testLessThanCapacityOneThread() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 4096, 10 << 20);
        String expected = "This is a test at " + LocalDateTime.now();

        q.write(expected.getBytes("UTF-8"));
        q.close();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        q.transferTo(os);
        String actual = new String(os.toByteArray(), "UTF-8");
        assertThat("Unexpected contents transferred", actual, is(expected));
    }

    /**
     * Pure in-memory check with two threads.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 2000)
    public void testLessThanCapacityTwoThreads() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 4096, 10 << 20);

        String expected = "This is a test at " + LocalDateTime.now();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        byte[] b = expected.getBytes("UTF-8");
        q.write(b, 0, 5);
        q.write(b, 5, b.length - 5);
        q.close();

        f.get();
        String actual = new String(os.toByteArray(), "UTF-8");
        assertThat("Unexpected contents transferred", actual, is(expected));
    }

    /**
     * Pure in-memory check with two threads and single-byte writes.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 2000)
    public void testLessThanCapacitySingleBytes() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 8192, 10 << 20);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        for (int i = 0; i < 7001; i++) {
            q.write(33);
        }
        q.close();

        f.get();
        int size = os.toByteArray().length;
        assertThat("Unexpected number of bytes transferred", size, is(7001));
    }

    /**
     * Pure in-memory check with two threads and partial array writes.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 2000)
    public void testLessThanCapacityPartialArray() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 8192, 10 << 20);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        byte[] b = new byte[1025];
        for (int i = 0; i < 5; i++) {
            q.write(b, 1, 1023);
        }
        q.close();

        f.get();
        int size = os.toByteArray().length;
        assertThat("Unexpected number of bytes transferred", size, is(5 * 1023));
    }

    /**
     * Checks with two threads and more written data than the buffer size but still all in-memory.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 2000)
    public void testInMemoryOnly() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 16, 10 << 20);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Semaphore started = new Semaphore(0);
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            started.release();
            q.transferTo(os);
            return 1;
        });

        started.acquire();
        String expected = "123456789AB";
        for (int i = 0; i < 20; i++) {
            q.write(expected.getBytes("UTF-8"));
            q.flush();

            String actual = "";
            int j = 5;
            while (j-- > 0) {
                actual = new String(os.toByteArray(), "UTF-8");
                if (actual.length() > 0) {
                    break;
                }
                Thread.sleep(50);
            }

            assertThat("Unexpected contents transferred in iteration " + i, actual, is(expected));
            os.reset();
        }
        q.close();
        f.get();
    }

    /**
     * Checks with two threads and more written data than the buffer size but still all in-memory.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 10000)
    public void testInMemoryOnlyLarge() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 16384, 10 << 20);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Semaphore started = new Semaphore(0);
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            started.release();
            q.transferTo(os);
            return 1;
        });

        started.acquire();
        byte[] toWrite = new byte[3557];
        Arrays.fill(toWrite, (byte)45);
        int size = 0;
        for (int i = 0; i < 20; i++) {
            q.write(toWrite);
            int j = 5;
            while (j-- > 0) {
                int s = os.toByteArray().length;
                if (s > size) {
                    size = s;
                    break;
                }
                Thread.sleep(50);
            }
        }
        q.close();
        f.get();
        byte[] actual = os.toByteArray();
        assertThat("Unexpected number of bytes read", actual.length, is(20 * toWrite.length));
    }

    /**
     * Test with two concurrent threads and a large buffer.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 10000)
    public void testInMemoryConcurrent() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 1 << 20, 10 << 20);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        byte[] toWrite = new byte[3557];
        Arrays.fill(toWrite, (byte)45);
        for (int i = 0; i < 100; i++) {
            q.write(toWrite);
        }
        q.close();
        f.get();
        byte[] actual = os.toByteArray();
        assertThat("Unexpected number of bytes read", actual.length, is(100 * toWrite.length));
    }

    /**
     * Check with overflow into a single disk chunk.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testOnDiskOneChunk() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 4096, 10 << 20);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        byte[] toWrite = new byte[3557];
        Arrays.fill(toWrite, (byte)45);
        for (int i = 0; i < 100; i++) {
            q.write(toWrite);
            q.write(45);
            q.write(toWrite, 2, 3);
        }
        q.close();
        f.get();
        byte[] actual = os.toByteArray();
        assertThat("Unexpected number of bytes read", actual.length, is(100 * (toWrite.length + 4)));
    }

    /**
     * Check with multiple disk chunks and complete array writes.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 10000)
    public void testOnDiskMultipleChunksArray() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 4096, 128 << 10);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        byte[] toWrite = new byte[3557];
        Arrays.fill(toWrite, (byte)45);
        for (int i = 0; i < 100; i++) {
            q.write(toWrite);
        }
        q.close();
        f.get();
        byte[] actual = os.toByteArray();
        assertThat("Unexpected number of bytes read", actual.length, is(100 * toWrite.length));
    }

    /**
     * Check with multiple disk chunks and partial array writes.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 10000)
    public void testOnDiskMultipleChunksPartialArray() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 4096, 128 << 10);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        byte[] toWrite = new byte[3557];
        Arrays.fill(toWrite, (byte)45);
        for (int i = 0; i < 100; i++) {
            q.write(toWrite, 2, 3500);
        }
        q.close();
        f.get();
        byte[] actual = os.toByteArray();
        assertThat("Unexpected number of bytes read", actual.length, is(100 * 3500));
    }

    /**
     * Check with multiple disk chunks and single byte writes.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 10000)
    public void testOnDiskMultipleChunksSingleByte() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 4096, 128 << 10);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        for (int i = 0; i < (130 << 10); i++) {
            q.write(45);
        }
        q.close();
        f.get();
        byte[] actual = os.toByteArray();
        assertThat("Unexpected number of bytes read", actual.length, is(130 << 10));
        q.cleanup();
    }

    /**
     * Check with multiple disk chunks and flushes.
     *
     * @throws Exception if an error occurs
     */
    @Test(timeout = 10000)
    public void testOnDiskMultipleWithFlush() throws Exception {
        DiskBasedByteQueue q = new DiskBasedByteQueue(m_tempDir, "test", 4096, 128 << 10);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        Future<Integer> f = ES.submit(() -> {
            q.transferTo(os);
            return 1;
        });

        byte[] toWrite = new byte[127 << 10];
        for (int i = 0; i < 100; i++) {
            q.write(toWrite);
            q.flush();
        }
        q.close();
        f.get();
        byte[] actual = os.toByteArray();
        assertThat("Unexpected number of bytes read", actual.length, is(100 * toWrite.length));
        q.cleanup();
    }
}
