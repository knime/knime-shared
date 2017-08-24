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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;

/**
 * This class implements a two-stage queue for bytes. In the first stage a bounded in-memory buffer is used. When the
 * writer (using the {@link OutputStream} interface) fills the buffer all subsequent data is buffered on disk. The
 * consumer simply calls {@link #transferTo(OutputStream)} and all data that is written by the writer is transfered into
 * the given output stream until the writer closes the queue.<br />
 * If the queue has switched into disk mode the writer first fills one complete chunk before the reader can consume it.
 * However in this case the consumer has proven to be slower than the writer (otherwise the in-memory buffer would still
 * be used) therefore the small delay until data is available again is tolerable.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @since 5.4
 */
public class DiskBasedByteQueue extends OutputStream {
    /**
     * Exception that is thrown then the queue has been closed but further read or write access occurs.
     *
     * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
     * @since 5.5
     */
    public static class QueueClosedException extends IOException {
        private static final long serialVersionUID = -1273277247159534336L;

        /**
         * Constructs an {@code QueueClosedException} with the specified detail message.
         *
         * @param message the detail message
         */
        public QueueClosedException(final String message) {
            super(message);
        }
    }


    private abstract static class Buffer extends OutputStream {
        /**
         * Returns the number of free bytes in the buffer.
         *
         * @return the numbre of free bytes
         */
        abstract long freeBytes();

        /**
         * Transfers the contents of this buffer into the given output stream. The method only returns when the buffer
         * is closed by the writer (via {@link #close()}).
         *
         * @param os an output stream
         * @throws InterruptedException if the thread is interrupted while waiting for new data to write
         * @throws IOException if an I/O error occurs
         */
        abstract void transferTo(OutputStream os) throws InterruptedException, IOException;

        /**
         * Cleans up any temporary data after the buffer has been closed.
         *
         * @throws IOException if an I/O error occurs
         */
        void cleanup() throws IOException {
        }
    }

    /**
     * In-memory buffer that assumes there are at most two threads working on it: one writer using the
     * {@link OutputStream} interface and one "reader" that calls {@link #transferTo(OutputStream)}. The implementation
     * assumes that there is always enough space for data that is written by one of the write methods. Clients must
     * check with {@link #freeBytes()} before writing, otherwise the data is overwritten.
     *
     * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
     */
    private static class InMemoryBuffer extends Buffer {
        private final byte[] m_buffer;

        private int m_readPos;

        private int m_writePos;

        private final AtomicInteger m_freeBytes;

        private final AtomicInteger m_usedBytes = new AtomicInteger(0);

        private volatile boolean m_closed;

        private static final int MIN_WRITE_SIZE = 4096;

        private final ReentrantLock m_writeLock = new ReentrantLock();

        private final Condition m_bytesAvailable = m_writeLock.newCondition();

        /**
         * Creates a new buffer.
         *
         * @param bufferSize the size of the buffer (in bytes).
         */
        InMemoryBuffer(final int bufferSize) {
            m_buffer = new byte[bufferSize];
            m_freeBytes = new AtomicInteger(bufferSize);
        }

        @Override
        public void write(final int b) throws IOException {
            assert m_freeBytes.get() >= 1 : "Not enough space in buffer";

            m_buffer[m_writePos++] = (byte)b;
            m_writePos %= m_buffer.length;
            m_freeBytes.decrementAndGet();
            if (m_usedBytes.incrementAndGet() >= MIN_WRITE_SIZE) {
                signalWriter();
            }
        }

        private void signalWriter() {
            m_writeLock.lock();
            try {
                m_bytesAvailable.signal();
            } finally {
                m_writeLock.unlock();
            }
        }

        @Override
        public void write(final byte[] b) throws IOException {
            assert m_freeBytes.get() >= b.length : "Not enough space in buffer";

            for (int i = 0; i < b.length; i++) {
                m_buffer[m_writePos++] = b[i];
                m_writePos %= m_buffer.length;
            }
            m_freeBytes.addAndGet(-b.length);
            if (m_usedBytes.addAndGet(b.length) >= MIN_WRITE_SIZE) {
                signalWriter();
            }
        }

        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            assert m_freeBytes.get() >= len : "Not enough space in buffer";

            for (int i = off; i < off + len; i++) {
                m_buffer[m_writePos++] = b[i];
                m_writePos %= m_buffer.length;
            }
            m_freeBytes.addAndGet(-len);
            if (m_usedBytes.addAndGet(len) >= MIN_WRITE_SIZE) {
                signalWriter();
            }
        }

        @Override
        public void flush() throws IOException {
            signalWriter();
        }

        @Override
        public void close() throws IOException {
            m_closed = true;
            signalWriter();
        }

        @Override
        public long freeBytes() {
            return m_freeBytes.get();
        }

        @Override
        public void transferTo(final OutputStream os) throws InterruptedException, IOException {
            do {
                m_writeLock.lock();
                try {
                    if (!m_closed && (m_usedBytes.get() < MIN_WRITE_SIZE)) {
                        m_bytesAvailable.await();
                    }
                } finally {
                    m_writeLock.unlock();
                }

                int count = m_usedBytes.get();
                if (m_readPos + count < m_buffer.length) {
                    os.write(m_buffer, m_readPos, count);
                    m_readPos += count;
                } else {
                    int diff = m_buffer.length - m_readPos;
                    os.write(m_buffer, m_readPos, diff);
                    m_readPos = 0;
                    os.write(m_buffer, 0, count - diff);
                    m_readPos = count - diff;
                }
                m_readPos %= m_buffer.length;
                m_usedBytes.addAndGet(-count);
                m_freeBytes.addAndGet(count);
            } while (!m_closed || m_usedBytes.get() > 0);
        }
    }

    /**
     * Buffer that uses files on disk to temporarily store data. The writer first writes a complete chunk before the
     * "reader" can consume it. Chunks that have been consumed are immediately deleted. There is no limit in the total
     * size of the buffer.
     *
     * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
     */
    private static class OnDiskBuffer extends Buffer {
        private final Path m_tempDir;

        private final int m_maxChunkSize;

        private final BlockingQueue<Path> m_chunks = new LinkedBlockingQueue<>();

        private final String m_prefix;

        private Path m_currentWriteChunk;

        private OutputStream m_currentWriteStream;

        private long m_currentChunkSize;

        private int m_chunkCounter;

        private volatile IOException m_consumerException;

        private static final Path CLOSED = Paths.get("");

        /**
         * Creates a new disk based buffer.
         *
         * @param tempDir
         * @param prefix
         * @param chunkSize
         * @throws IOException
         */
        public OnDiskBuffer(final Path tempDir, final String prefix, final int chunkSize) throws IOException {
            m_tempDir = tempDir;
            m_maxChunkSize = chunkSize;
            m_prefix = prefix;
            openNewChunk();
        }

        private void checkChunkSize() throws IOException {
            if (m_currentChunkSize >= m_maxChunkSize) {
                flush();
            }
            if (m_consumerException != null) {
                throw m_consumerException;
            }
        }

        private void openNewChunk() throws IOException {
            if (m_currentWriteChunk == CLOSED) {
                throw new IOException("Queue has been closed");
            }
            m_currentWriteChunk = PathUtils.createTempFile(m_tempDir, m_prefix, "." + m_chunkCounter);
            m_chunkCounter++;
            m_currentChunkSize = 0;
            m_currentWriteStream = new BufferedOutputStream(Files.newOutputStream(m_currentWriteChunk));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final int b) throws IOException {
            m_currentWriteStream.write(b);
            m_currentChunkSize++;
            checkChunkSize();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final byte[] b) throws IOException {
            m_currentWriteStream.write(b);
            m_currentChunkSize += b.length;
            checkChunkSize();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            m_currentWriteStream.write(b, off, len);
            m_currentChunkSize += len;
            checkChunkSize();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            m_currentWriteStream.close();
            m_chunks.offer(m_currentWriteChunk);
            m_currentWriteChunk = CLOSED;
            m_chunks.offer(CLOSED);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long freeBytes() {
            return Long.MAX_VALUE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void transferTo(final OutputStream os) throws InterruptedException, IOException {
            while (true) {
                Path chunk = m_chunks.take();
                if (chunk == CLOSED) {
                    break;
                }
                try (InputStream is = Files.newInputStream(chunk)) {
                    IOUtils.copy(is, os);
                } catch (IOException ex) {
                    m_consumerException = ex;
                    throw ex;
                }

                Files.delete(chunk);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        void cleanup() throws IOException {
            assert m_currentWriteChunk == CLOSED : "Queue has not been closed yet";
            for (Path p : m_chunks) {
                if (p != CLOSED) {
                    Files.deleteIfExists(p);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void flush() throws IOException {
            m_currentWriteStream.close();
            m_chunks.offer(m_currentWriteChunk);
            openNewChunk();
        }
    }

    private volatile Buffer m_writeBuffer;

    private volatile Buffer m_readBuffer;

    private final Path m_tempDir;

    private final String m_prefix;

    private final int m_diskChunkSize;

    private volatile boolean m_closed;


    /**
     * Creates a new queue.
     *
     * @param tempDir the directory in which temporary files should be created
     * @param prefix a prefix for the temporary files
     * @param memoryBufferSize the maximum size of the in-memory buffer
     * @param diskChunkSize the maximum size of chunks on disk
     */
    public DiskBasedByteQueue(final Path tempDir, final String prefix, final int memoryBufferSize,
        final int diskChunkSize) {
        m_tempDir = tempDir;
        m_prefix = prefix;
        m_writeBuffer = new InMemoryBuffer(memoryBufferSize);
        m_readBuffer = m_writeBuffer;
        m_diskChunkSize = diskChunkSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final int b) throws IOException {
        if (m_closed) {
            throw new QueueClosedException("Queue has been closed");
        }

        if (m_writeBuffer.freeBytes() >= 1) {
            m_writeBuffer.write(b);
        } else {
            Buffer old = m_writeBuffer;
            m_writeBuffer = new OnDiskBuffer(m_tempDir, m_prefix, m_diskChunkSize);
            old.close();
            m_writeBuffer.write(b);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final byte[] b) throws IOException {
        if (m_closed) {
            throw new QueueClosedException("Queue has been closed");
        }
        if (m_writeBuffer.freeBytes() >= b.length) {
            m_writeBuffer.write(b);
        } else {
            Buffer old = m_writeBuffer;
            m_writeBuffer = new OnDiskBuffer(m_tempDir, m_prefix, m_diskChunkSize);
            old.close();
            m_writeBuffer.write(b);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (m_closed) {
            throw new QueueClosedException("Queue has been closed");
        }

        if (m_writeBuffer.freeBytes() >= len) {
            m_writeBuffer.write(b, off, len);
        } else {
            Buffer old = m_writeBuffer;
            m_writeBuffer = new OnDiskBuffer(m_tempDir, m_prefix, m_diskChunkSize);
            old.close();
            m_writeBuffer.write(b, off, len);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
        m_writeBuffer.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        m_closed = true;
        m_writeBuffer.close();
        m_readBuffer.close();
    }

    /**
     * Transfers the contents of this queue into the given output stream. The method only returns when the queue is
     * closed by the writer (via {@link #close()}).
     *
     * @param os an output stream
     * @throws InterruptedException if the thread is interrupted while waiting for new data to write
     * @throws IOException if an I/O error occurs
     */
    public void transferTo(final OutputStream os) throws InterruptedException, IOException {
        if (m_closed) {
            throw new QueueClosedException("Queue has been closed");
        }
        m_readBuffer.transferTo(os);
        if (m_readBuffer != m_writeBuffer) {
            // the write buffer has switched and there is potential data available
            m_readBuffer = m_writeBuffer;
            m_readBuffer.transferTo(os);
        }
    }

    /**
     * Cleans up any temporary data after the queue has been closed.
     *
     * @throws IOException if an I/O error occurs
     */
    public void cleanup() throws IOException {
        m_writeBuffer.cleanup();
    }
}
