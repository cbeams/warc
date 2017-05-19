package org.iokit.core.read;

public class CountingReader<T> extends SequenceReader<T> {

    protected long readCount = 0;

    public CountingReader(Reader<T> reader) {
        super(reader);
    }

    @Override
    public T read() throws ReaderException {
        T value = super.read();

        if (value != null)
            readCount++;

        return value;
    }

    public long getReadCount() {
        return readCount;
    }
}
