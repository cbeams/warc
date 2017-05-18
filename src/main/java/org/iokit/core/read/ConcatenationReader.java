package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import java.io.IOException;

public class ConcatenationReader<T> extends BoundedReader<T> {

    private final LineInputStream input;
    private final Reader<?> concatenatorReader;

    public ConcatenationReader(LineInputStream input, Reader<T> reader, Reader<?> concatenatorReader, int minCount) {
        super(input, reader, minCount);
        this.input = input;
        this.concatenatorReader = concatenatorReader;
    }

    public void seek(long offset) throws IOException {
        input.seek(offset);
    }

    public T read() throws ReaderException {
        if (input.isComplete())
            return null;

        T value = super.read();
        concatenatorReader.read();

        return value;
    }
}
