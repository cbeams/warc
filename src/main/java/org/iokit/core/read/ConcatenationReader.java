package org.iokit.core.read;

public class ConcatenationReader<T> extends BoundedReader<T> {

    private final Reader<?> concatenatorReader;

    public ConcatenationReader(Reader<T> reader, Reader<?> concatenatorReader, int minCount) {
        super(reader, minCount);
        this.concatenatorReader = concatenatorReader;
    }

    public T read() throws ReaderException {
        T value = super.read();

        if (value != null)
            concatenatorReader.read();

        return value;
    }
}
