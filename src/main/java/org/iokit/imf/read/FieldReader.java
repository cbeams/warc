package org.iokit.imf.read;

import org.iokit.imf.Field;
import org.iokit.imf.parse.FieldParser;

import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.parse.Parser;

public class FieldReader implements Reader<Field> {

    private final FoldedLineReader lineReader;
    private final Parser<Field> fieldParser;

    public FieldReader(FoldedLineReader lineReader) {
        this(lineReader, new FieldParser());
    }

    public FieldReader(FoldedLineReader lineReader, Parser<Field> fieldParser) {
        this.lineReader = lineReader;
        this.fieldParser = fieldParser;
    }

    public Field read() throws ReaderException {
        String input = lineReader.read();

        if (input == null || input.isEmpty())
            return null;

        return fieldParser.parse(input);
    }
}
