package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class FieldReader implements Reader<Field> {

    private final LineReader lineReader;

    public FieldReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public Field read() throws ParsingException, EOFException {
        String input = lineReader.read();

        if (input == null || input.isEmpty())
            return null;

        return Field.parse(input);
    }
}
