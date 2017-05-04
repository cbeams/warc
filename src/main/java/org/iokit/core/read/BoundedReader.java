package org.iokit.core.read;

import org.iokit.core.parse.ParsingException;

import org.iokit.core.input.CompletionAwareInput;

import java.io.EOFException;

import static java.lang.String.format;

public class BoundedReader<T> extends CountingReader<T> {

    private int minimumCount;

    public BoundedReader(CompletionAwareInput input, Reader<T> reader, Reader<Void> separatorReader, int minimumCount) {
        super(input, reader, separatorReader);
        this.minimumCount = minimumCount;
    }

    @Override
    public T read() throws EOFException, ParsingException {
        T value = super.read();

        if (value == null)
            if (getCurrentCount() < minimumCount)
                throw new RuntimeException(format(
                    "expected to read at least %d value(s), but %d were found", minimumCount, getCurrentCount()));

        return value;
    }

    private void setMinimumCount(int minimumCount) {
        this.minimumCount = minimumCount;
    }
}
