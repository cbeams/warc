package org.iokit.imf;

import org.iokit.core.parse.ParsingException;

import java.util.Objects;

public class Field {

    public static final char SEPARATOR = ':';

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
        return String.format("%s%c %s", name.toString(), SEPARATOR, value);
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


    public interface Type {

        FieldName getName();
    }


    public static class Parser implements org.iokit.core.parse.Parser<Field> {

        private final org.iokit.core.parse.Parser<FieldName> nameParser;
        private final org.iokit.core.parse.Parser<FieldValue> valueParser;

        public Parser() {
            this(new FieldName.Parser(), new FieldValue.Parser());
        }

        public Parser(org.iokit.core.parse.Parser<FieldName> nameParser,
                      org.iokit.core.parse.Parser<FieldValue> valueParser) {
            this.nameParser = nameParser;
            this.valueParser = valueParser;
        }

        public Field parse(String input) throws ParsingException {
            int separatorIndex = input.indexOf(SEPARATOR);
            if (separatorIndex == -1)
                throw new MissingSeparatorException(input);

            return new Field(
                nameParser.parse(input.substring(0, separatorIndex)),
                valueParser.parse(input.substring(separatorIndex + 1, input.length())));
        }


        public static class MissingSeparatorException extends ParsingException {

            public MissingSeparatorException(String input) {
                super("%s input must contain '%c' separator. Input was [%s]",
                    Field.class.getSimpleName(), SEPARATOR, input);
            }
        }
    }
}
