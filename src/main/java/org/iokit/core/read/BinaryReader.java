package org.iokit.core.read;

import org.iokit.core.IOKitException;
import org.iokit.core.Try;

import java.io.InputStream;

public class BinaryReader extends FixedLengthReader<byte[]> {

    public BinaryReader(InputStream in) {
        super(in);
    }

    @Override
    public byte[] read(int length) {
        byte[] array = new byte[length];

        int actual = Try.toCall(() -> in.read(array, 0, length));

        if (actual != length)
            throw new IOKitException("expected to read %d bytes, but read only %d", length, actual);

        return array;
    }
}
