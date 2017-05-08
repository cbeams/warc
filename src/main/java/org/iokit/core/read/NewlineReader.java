package org.iokit.core.read;

import java.io.EOFException;

public class NewlineReader implements Reader<Void> {

    private final LineReader lineReader;

    public NewlineReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public Void read() throws ReaderException, EOFException {
        String value = lineReader.read();

        if (!value.isEmpty())
            throw new ReaderException("expected to read a newline but got [%s]", value);

        return null;
    }
}
