package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

public class CountingReader<T> extends FilterReader<T> {

    private long currentCount = 0;

    public CountingReader(LineInputStream input, Reader<T> reader) {
        super(input, reader);
    }

    @Override
    public T read() throws ReaderException {
        T value = super.read();

        if (value != null)
            currentCount++;

        return value;
    }

    public long getCurrentCount() {
        return currentCount;
    }
}
