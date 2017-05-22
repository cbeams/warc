package org.iokit.core.read;

import java.util.Optional;

public class ConcatenationReader<T> extends CountingReader<T> {

    public static final boolean DEFAULT_EXPECT_TRAILING_CONCATENATOR = true;

    private boolean expectTrailingConcatenator = DEFAULT_EXPECT_TRAILING_CONCATENATOR;

    private final Reader<T> reader;
    private final Reader<Boolean> concatenatorReader;

    public ConcatenationReader(Reader<T> reader, Reader<Boolean> concatenatorReader) {
        super(reader.in);
        this.reader = reader;
        this.concatenatorReader = concatenatorReader;
    }

    @Override
    protected Optional<T> readOptionalBeforeCounting() throws ReaderException {
        Optional<T> value = readValue();
        value.ifPresent(v -> readConcatenator());
        return value;
    }

    protected Optional<T> readValue() {
        if (cursor.supported)
            return cursor.isAtEOF() ?
                Optional.empty() :
                Optional.of(reader.read());

        try {
            return Optional.of(reader.read());
        } catch (EndOfInputException ex) {
            return Optional.empty();
        }
    }

    protected void readConcatenator() {
        if (!concatenatorReader.read() && expectTrailingConcatenator)
            throw new EndOfInputException("" +
                "Expected to read a trailing concatenator but actually encountered end of input. " +
                "Call setExpectTrailingConcatenator(false) to avoid this error.");
    }

    public void setExpectTrailingConcatenator(boolean expectTrailingConcatenator) {
        this.expectTrailingConcatenator = expectTrailingConcatenator;
    }
}
