package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

public class CountingReader<T> extends AbstractReader<T> {

    private final Reader<T> reader;

    private long currentCount = 0;

    public CountingReader(LineInputStream input, Reader<T> reader) {
        super(input);
        this.reader = reader;
    }

    @Override
    public T read() throws ReaderException {
        T value = reader.read();

        if (value != null)
            currentCount++;

        return value;
    }

    public long getCurrentCount() {
        return currentCount;
    }
}
