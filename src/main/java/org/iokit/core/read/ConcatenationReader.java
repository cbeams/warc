package org.iokit.core.read;

import org.iokit.core.input.Input;

public class ConcatenationReader<T> extends BoundedReader<T> {

    private final Input input;
    private final Reader<?> concatenatorReader;

    public ConcatenationReader(Input input, Reader<T> reader, Reader<?> concatenatorReader, int minCount) {
        super(input, reader, minCount);
        this.input = input;
        this.concatenatorReader = concatenatorReader;
    }

    public T read() throws ReaderException {
        T value = super.read();

        if (value != null)
            concatenatorReader.read();

        return value;
    }
}
