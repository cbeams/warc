package org.iokit.imf.read;

import org.iokit.imf.Field;
import org.iokit.imf.parse.FieldParser;

import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.ReaderException;
import org.iokit.core.read.TransformReader;

import org.iokit.core.parse.Parser;

import java.util.Optional;

public class FieldReader extends TransformReader<FoldedLineReader, Field> {

    private final Parser<Field> fieldParser;

    public FieldReader(FoldedLineReader reader) {
        this(reader, new FieldParser());
    }

    public FieldReader(FoldedLineReader reader, Parser<Field> fieldParser) {
        super(reader);
        this.fieldParser = fieldParser;
    }

    public Optional<Field> readOptional() throws ReaderException {
        Optional<String> input = reader.readOptional().filter(s -> !s.isEmpty());

        if (!input.isPresent())
            return Optional.empty();

        return Optional.of(fieldParser.parse(input.get()));
    }
}
