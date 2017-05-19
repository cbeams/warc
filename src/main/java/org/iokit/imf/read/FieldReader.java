package org.iokit.imf.read;

import org.iokit.imf.Field;
import org.iokit.imf.parse.FieldParser;

import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.parse.Parser;

import java.util.Optional;

public class FieldReader extends Reader<Field> {

    private final FoldedLineReader lineReader;
    private final Parser<Field> fieldParser;

    public FieldReader(FoldedLineReader lineReader) {
        this(lineReader, new FieldParser());
    }

    public FieldReader(FoldedLineReader lineReader, Parser<Field> fieldParser) {
        super(lineReader.getInput());
        this.lineReader = lineReader;
        this.fieldParser = fieldParser;
    }

    public Optional<Field> readOptional() throws ReaderException {
        Optional<String> input = lineReader.readOptional().filter(s -> !s.isEmpty());

        if (!input.isPresent())
            return Optional.empty();

        return Optional.of(fieldParser.parse(input.get()));
    }
}
