package org.iokit.core.read;

public class CountingReader<T> extends SequenceReader<T> {

    private long currentCount = 0;

    public CountingReader(Reader<T> reader) {
        super(reader);
    }

    @Override
    public T read() throws ReaderException {
        T value = super.read();

        if (value != null)
            currentCount++;

        return value;
    }

    public long getCurrentCount() {
        return currentCount;
    }
}
