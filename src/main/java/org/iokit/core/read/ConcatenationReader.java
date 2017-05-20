package org.iokit.core.read;

import java.util.Optional;

public class ConcatenationReader<T> extends OptionalReader<T> {

    public static final int DEFAULT_MINIMUM_READ_COUNT = 0;

    private final Reader<T> reader;
    private final ConcatenatorReader concatenatorReader;

    private int minimumReadCount;
    private long readCount;

    public ConcatenationReader(Reader<T> reader, ConcatenatorReader concatenatorReader) {
        this(reader, concatenatorReader, DEFAULT_MINIMUM_READ_COUNT);
    }

    public ConcatenationReader(Reader<T> reader, ConcatenatorReader concatenatorReader, int minimumReadCount) {
        super(reader.getInput());
        this.reader = reader;
        this.concatenatorReader = concatenatorReader;
        this.minimumReadCount = minimumReadCount;
    }

    @Override
    public Optional<T> readOptional() throws ReaderException {
        Optional<T> value = input.isComplete() ?
            Optional.empty() :
            Optional.of(reader.read());

        if (value.isPresent()) {
            readCount++;
            concatenatorReader.read();
        } else if (readCount < minimumReadCount) {
            throw new ReaderException(
                "Expected to read at least %d value(s), but %d were found", minimumReadCount, readCount);
        }

        return value;
    }

    public void setExpectTrailingConcatenator(boolean expectTrailingConcatenator) {
        concatenatorReader.setExpectTrailingConcatenator(expectTrailingConcatenator);
    }

    public void setMinimumReadCount(int minimumReadCount) {
        this.minimumReadCount = minimumReadCount;
    }

    public long getReadCount() {
        return readCount;
    }
}
