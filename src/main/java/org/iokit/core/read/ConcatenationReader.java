package org.iokit.core.read;

import java.util.Optional;

public class ConcatenationReader<T> extends CountingReader<T> {

    private final Reader<T> reader;
    private final ConcatenatorReader concatenatorReader;

    public ConcatenationReader(Reader<T> reader, ConcatenatorReader concatenatorReader) {
        super(reader.getInput());
        this.reader = reader;
        this.concatenatorReader = concatenatorReader;
    }

    @Override
    protected Optional<T> readBeforeCounting() throws ReaderException {

        Optional<T> value = input.isComplete() ?
            Optional.empty() :
            Optional.of(reader.read());

        value.ifPresent(v ->
            concatenatorReader.read());

        return value;
    }

    public void setExpectTrailingConcatenator(boolean expectTrailingConcatenator) {
        concatenatorReader.setExpectTrailingConcatenator(expectTrailingConcatenator);
    }
}
