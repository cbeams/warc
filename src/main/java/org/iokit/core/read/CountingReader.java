package org.iokit.core.read;

import org.iokit.core.input.CompletionAwareInput;

import java.io.EOFException;

public class CountingReader<T> extends SequentialReader<T> {

    private long currentCount = 0;

    public CountingReader(CompletionAwareInput input, Reader<T> reader, Reader<Void> separatorReader) {
        super(input, reader, separatorReader);
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
