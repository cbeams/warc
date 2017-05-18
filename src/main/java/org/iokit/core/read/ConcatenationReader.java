package org.iokit.core.read;

public class ConcatenationReader<T> extends BoundedReader<T> {

    public static final boolean DEFAULT_EXPECT_TRAILING_CONCATENATOR = true;

    private final Reader<?> concatenatorReader;

    private boolean expectTrailingConcatenator;

    public ConcatenationReader(Reader<T> reader, Reader<?> concatenatorReader) {
        this(reader, concatenatorReader, DEFAULT_MINIMUM_READ_COUNT);
    }

    public ConcatenationReader(Reader<T> reader, Reader<?> concatenatorReader, int minimumReadCount) {
        this(reader, concatenatorReader, minimumReadCount, DEFAULT_EXPECT_TRAILING_CONCATENATOR);
    }

    public ConcatenationReader(Reader<T> reader, Reader<?> concatenatorReader, int minimumReadCount,
                               boolean expectTrailingConcatenator) {
        super(reader, minimumReadCount);
        this.concatenatorReader = concatenatorReader;
        this.expectTrailingConcatenator = expectTrailingConcatenator;
    }

    public T read() throws ReaderException {
        T value = super.read();

        if (value != null)
            readConcatenator();

        return value;
    }

    protected void readConcatenator() {
        try {
            concatenatorReader.read();
        } catch (EndOfInputException ex) {
            if (expectTrailingConcatenator) {
                throw new ReaderException(ex, "" +
                    "Encountered end of input where a trailing concatenator was expected. " +
                    "Call setExpectTrailingConcatenator(false) to avoid this error.");
            }
        }
    }

    public void setExpectTrailingConcatenator(boolean expectTrailingConcatenator) {
        this.expectTrailingConcatenator = expectTrailingConcatenator;
    }
}
