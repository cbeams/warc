package org.iokit.core.read;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class NewlineReader implements Reader<Void> {

    private final LineReader lineReader;

    public NewlineReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public Void read() throws ParsingException, EOFException {
        String value = lineReader.read();

        if (!value.isEmpty())
            throw new ParsingException("expected to read a newline but got [%s]", value);

        return null;
    }
}
