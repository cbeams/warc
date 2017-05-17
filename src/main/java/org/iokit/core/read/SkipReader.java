package org.iokit.core.read;

import java.io.EOFException;

public class SkipReader implements Reader<Void> {

    private final int count;
    private final Reader<?> reader;

    public SkipReader(int count, Reader<?> reader) {
        this.count = count;
        this.reader = reader;
    }

    @Override
    public Void read() throws ReaderException, EOFException {
        for (int i=0; i < count; i++)
            reader.read();

        return null;
    }
}
