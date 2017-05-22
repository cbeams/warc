package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.OptionalReader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.parse.Parser;

import java.util.Optional;

import static org.iokit.core.read.NewlineReader.isNewline;

public class FieldReader extends OptionalReader<Field> {

    private final LineReader lineReader;
    private final Parser<Field> fieldParser;

    public FieldReader(LineReader lineReader) {
        this(lineReader, new Field.Parser());
    }

    public FieldReader(LineReader lineReader, Parser<Field> fieldParser) {
        super(lineReader.in);
        this.lineReader = lineReader;
        this.fieldParser = fieldParser;
    }

    @Override
    public Optional<Field> readOptional() throws ReaderException {
        Optional<String> line = lineReader.readOptional().filter(s -> !isNewline(s));

        return line.isPresent() ?
            Optional.of(fieldParser.parse(line.get())) :
            Optional.empty();
    }
}
