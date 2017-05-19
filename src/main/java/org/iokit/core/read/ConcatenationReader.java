package org.iokit.core.read;

import java.util.Optional;

public class ConcatenationReader<T> extends BoundedReader<T> {

    public static final int DEFAULT_MINIMUM_READ_COUNT = 0;

    private final ConcatenatorReader concatenatorReader;

    public ConcatenationReader(Reader<T> reader, ConcatenatorReader concatenatorReader) {
        this(reader, concatenatorReader, DEFAULT_MINIMUM_READ_COUNT);
    }

    public ConcatenationReader(Reader<T> reader, ConcatenatorReader concatenatorReader, int minimumReadCount) {
        super(reader, minimumReadCount);
        this.concatenatorReader = concatenatorReader;
    }

    @Override
    public Optional<T> readOptional() throws ReaderException {
        Optional<T> value = super.readOptional();

        value.ifPresent(v ->
            concatenatorReader.read());

        return value;
    }

    public void setExpectTrailingConcatenator(boolean expectTrailingConcatenator) {
        concatenatorReader.setExpectTrailingConcatenator(expectTrailingConcatenator);
    }
}
