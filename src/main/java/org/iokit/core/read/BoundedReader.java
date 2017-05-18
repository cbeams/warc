package org.iokit.core.read;

public class BoundedReader<T> extends CountingReader<T> {

    private int minimumCount;

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
