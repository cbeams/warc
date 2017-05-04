package org.iokit.core.read;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class ByteArrayReader implements FixedLengthReader<byte[]> {

    private final InputStream in;

    public ByteArrayReader(InputStream in) {
        this.in = in;
    }

    @Override
    public byte[] read(int length) throws EOFException, ParsingException {
        byte[] array = new byte[length];

        int actual;
        try {
            actual = in.read(array, 0, length);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        if (actual != length)
            throw new ParsingException("expected to read %d bytes, but read only %d", length, actual);

        return array;
    }
}
