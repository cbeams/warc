package org.iokit.core.read;

import java.util.Optional;

public class FilterReader<T> extends OptionalReader<T> {

    protected final Reader<T> reader;

    public FilterReader(Reader<T> reader) {
        super(reader.getInput());
        this.reader = reader;
    }

    @Override
    public Optional<T> readOptional() throws ReaderException {
        return input.isComplete() ? Optional.empty() : Optional.of(reader.read());
    }
}
