package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import java.io.InputStream;

import java.util.Optional;

public class ConcatenationReader<T> extends CountingReader<T> {

    public static final boolean DEFAULT_EXPECT_TRAILING_CONCATENATOR = true;

    private boolean expectTrailingConcatenator = DEFAULT_EXPECT_TRAILING_CONCATENATOR;

    private final Reader<T> reader;
    private final Reader<Boolean> concatenatorReader;

    public ConcatenationReader(Reader<T> reader, Reader<Boolean> concatenatorReader) {
        super(reader.getInput());
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
        InputStream input = reader.getInput();

        if (input instanceof LineInputStream)
            return ((LineInputStream) input).isComplete() ?
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
                "Encountered end of input where a trailing concatenator was expected. " +
                "Call setExpectTrailingConcatenator(false) to avoid this error.");
    }

    public void setExpectTrailingConcatenator(boolean expectTrailingConcatenator) {
        this.expectTrailingConcatenator = expectTrailingConcatenator;
    }
}
