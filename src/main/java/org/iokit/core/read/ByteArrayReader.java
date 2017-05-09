package org.iokit.core.read;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class ByteArrayReader implements AutoCloseable, FixedLengthReader<byte[]> {

    private final InputStream input;

    public ByteArrayReader(InputStream input) {
        this.input = input;
    }

    @Override
    public byte[] read(int length) throws ReaderException, EOFException {
        byte[] array = new byte[length];

        int actual;
        try {
            actual = input.read(array, 0, length);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        if (actual != length)
            throw new ReaderException("expected to read %d bytes, but read only %d", length, actual);

        return array;
    }

    @Override
    public void close() throws Exception {
        input.close();
    }
}
