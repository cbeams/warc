package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import java.io.EOFException;

public class CountingReader<T> extends ConcatenationReader<T> {

    private long currentCount = 0;

    public CountingReader(LineInputStream input, Reader<T> valueReader, Reader<?> concatenatorReader) {
        super(input, valueReader, concatenatorReader);
    }

    @Override
    public T read() throws ReaderException, EOFException {
        T value = super.read();

        if (value != null)
            currentCount++;

        return value;
    }

    public long getCurrentCount() {
        return currentCount;
    }
}
