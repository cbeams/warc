package org.iokit.message;

import org.iokit.line.LineReader;
import org.iokit.line.LineWriter;

import org.iokit.core.write.IOKitWriter;

import org.iokit.core.read.OptionalReader;

import org.iokit.core.parse.IOKitParser;

import org.iokit.core.IOKitException;

import java.util.Objects;
import java.util.Optional;

import static org.iokit.line.NewlineReader.isNewline;
import static org.iokit.util.Ascii.COLON;

public class Field {

    public static final char SEPARATOR = COLON;

    private final FieldName name;
    private final FieldValue value;

    public Field(FieldName name, FieldValue value) {
        this.name = name;
        this.value = value;
    }

    public FieldName getName() {
        return name;
    }

    public FieldValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s%c %s", name, SEPARATOR, value);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Field field = (Field) that;
        return Objects.equals(name, field.name) &&
            Objects.equals(value, field.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }


    public static class Reader extends OptionalReader<Field> {

        private final LineReader lineReader;
        private final Field.Parser fieldParser;

        public Reader(LineReader lineReader) {
            this(lineReader, new Field.Parser());
        }

        public Reader(LineReader lineReader, Field.Parser fieldParser) {
            super(lineReader.in);
            this.lineReader = lineReader;
            this.fieldParser = fieldParser;
        }

        @Override
        public Optional<Field> readOptional() {
            Optional<String> line = lineReader.readOptional().filter(s -> !isNewline(s));

            return line.isPresent() ?
                Optional.of(fieldParser.parse(line.get())) :
                Optional.empty();
        }
    }


    public static class Writer extends IOKitWriter<Field> {

        private final LineWriter lineWriter;

        public Writer(LineWriter lineWriter) {
            super(lineWriter.out);
            this.lineWriter = lineWriter;
        }

        public void write(Field field) {
            lineWriter.write(String.format("%s%c %s", field.getName(), SEPARATOR, field.getValue().getFoldedValue())); // TODO: FoldedLineWriter, folding policy (preserve, fold-at, unfold)
        }
    }


    public static class Parser implements IOKitParser<Field> {

        private final FieldName.Parser nameParser;
        private final FieldValue.Parser valueParser;

        public Parser() {
            this(new FieldName.Parser(), new FieldValue.Parser());
        }

        public Parser(FieldName.Parser nameParser, FieldValue.Parser valueParser) {
            this.nameParser = nameParser;
            this.valueParser = valueParser;
        }

        public Field parse(String input) {
            int separatorIndex = input.indexOf(SEPARATOR);
            if (separatorIndex == -1)
                throw new MissingSeparatorException(input);

            return new Field(
                nameParser.parse(input.substring(0, separatorIndex)),
                valueParser.parse(input.substring(separatorIndex + 1, input.length())));
        }


        public static class MissingSeparatorException extends IOKitException {

            public MissingSeparatorException(String input) {
                super("%s input must contain '%c' separator. Input was [%s]",
                    Field.class.getSimpleName(), SEPARATOR, input);
            }
        }
    }
}
