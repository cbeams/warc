package org.iokit.core.read;

public class BoundedReader<T> extends CountingReader<T> {

    public static final int DEFAULT_MINIMUM_READ_COUNT = 0;

    private int minimumCount;

    public BoundedReader(Reader<T> reader) {
        this(reader, DEFAULT_MINIMUM_READ_COUNT);
    }

    public BoundedReader(Reader<T> reader, int minimumCount) {
        super(reader);
        this.minimumCount = minimumCount;
    }

    @Override
    public T read() throws ReaderException {
        T value = super.read();

        if (value == null)
            if (getCurrentCount() < minimumCount)
                throw new ReaderException(
                    "expected to read at least %d value(s), but %d were found", minimumCount, getCurrentCount());

        return value;
    }

    private void setMinimumCount(int minimumCount) {
        this.minimumCount = minimumCount;
    }
}
