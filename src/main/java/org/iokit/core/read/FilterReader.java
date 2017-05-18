package org.iokit.core.read;

import org.iokit.core.input.Input;

public class FilterReader<T> extends AbstractReader<T> {

    private final Reader<T> reader;

    public FilterReader(Input input, Reader<T> reader) {
        super(input);
        this.reader = reader;
    }

    @Override
    public T read() throws ReaderException {
        return input.isComplete() ? null : reader.read();
    }
}
