package org.iokit.core.read;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class CountOffReader implements Reader<Void> {

    private final int count;
    private final Reader<?> reader;

    public CountOffReader(int count, Reader<?> reader) {
        this.count = count;
        this.reader = reader;
    }

    @Override
    public Void read() throws EOFException, ParsingException {
        for (int i=0; i < count; i++)
            reader.read();

        return null;
    }
}
