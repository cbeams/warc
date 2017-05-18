package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

public class FilterReader<T> extends AbstractReader<T> {

    private final Reader<T> reader;

    public FilterReader(LineInputStream input, Reader<T> reader) {
        super(input);
        this.reader = reader;
    }

    @Override
    public T read() throws ReaderException {
        return input.isComplete() ? null : reader.read();
    }
}
