package org.iokit.core.read;

public class BoundedReader<T> extends CountingReader<T> {

    private int minimumReadCount;

    public BoundedReader(Reader<T> reader, int minimumReadCount) {
        super(reader);
        this.minimumReadCount = minimumReadCount;
    }

    @Override
    public T read() throws ReaderException {
        T value = super.read();

        if (value == null)
            if (readCount < minimumReadCount)
                throw new ReaderException(
                    "Expected to read at least %d value(s), but %d were found", minimumReadCount, readCount);

        return value;
    }

    public void setMinimumReadCount(int minimumReadCount) {
        this.minimumReadCount = minimumReadCount;
    }
}
