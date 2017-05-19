package org.iokit.core.read;

public class ConcatenatorReader extends InputReader<Boolean> {

    public static final boolean DEFAULT_EXPECT_TRAILING_CONCATENATOR = true;

    private final Reader<Boolean> reader;
    private boolean expectTrailingConcatenator;

    public ConcatenatorReader(Reader<Boolean> reader) {
        this(reader, DEFAULT_EXPECT_TRAILING_CONCATENATOR);
    }

    public ConcatenatorReader(Reader<Boolean> reader, boolean expectTrailingConcatenator) {
        super(reader.getInput());
        this.reader = reader;
        this.expectTrailingConcatenator = expectTrailingConcatenator;
    }

    @Override
    public Boolean read() throws ReaderException {
        boolean successful = reader.read();

        if (!successful && expectTrailingConcatenator)
            throw new EndOfInputException("" +
                "Encountered end of input where a trailing concatenator was expected. " +
                "Call setExpectTrailingConcatenator(false) to avoid this error.");

        return successful;
    }

    public void setExpectTrailingConcatenator(boolean expectTrailingConcatenator) {
        this.expectTrailingConcatenator = expectTrailingConcatenator;
    }
}
