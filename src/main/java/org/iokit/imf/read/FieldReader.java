package org.iokit.imf.read;

import org.iokit.imf.Field;
import org.iokit.imf.parse.FieldParser;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.parse.Parser;
import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class FieldReader implements Reader<Field> {

    private final LineReader lineReader;
    private final Parser<Field> fieldParser;

    public FieldReader(LineReader lineReader) {
        this(lineReader, new FieldParser());
    }

    public FieldReader(LineReader lineReader, Parser<Field> fieldParser) {
        this.lineReader = lineReader;
        this.fieldParser = fieldParser;
    }

    public Field read() throws ReaderException, EOFException {
        String input = lineReader.read();

        if (input == null || input.isEmpty())
            return null;

        try {
            return fieldParser.parse(input);
        } catch (ParsingException ex) {
            throw new ReaderException(ex);
        }
    }
}
