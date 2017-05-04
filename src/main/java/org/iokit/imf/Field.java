package org.iokit.imf;

import org.iokit.core.parse.InvalidCharacterException;
import org.iokit.core.parse.NullInputException;
import org.iokit.core.parse.ParsingException;

import java.util.Objects;

import static org.iokit.core.parse.Ascii.isAsciiControlChar;

public class Field {

    public static final char SEPARATOR = ':';

    private final Name name;
    private final Value value;

    public Field(Name name, Value value) { // TODO: return to package-private
        this.name = name;
        this.value = value;
    }

    public Name getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public static Field parse(String input) throws ParsingException {
        if (input == null)
            throw new NullInputException();

        int separatorIndex = input.indexOf(SEPARATOR);
        if (separatorIndex == -1)
            throw new MissingSeparatorException(input);

        return new Field(
            Name.parse(input.substring(0, separatorIndex)),
            Value.parse(input.substring(separatorIndex + 1, input.length())));
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


    public static class MissingSeparatorException extends ParsingException {

        public MissingSeparatorException(String input) {
            super("%s input must contain '%c' separator. Input was [%s]",
                Field.class.getSimpleName(), SEPARATOR, input);
        }
    }


    /**
     * Field names are case-insensitive.
     */
    public static class Name extends Token {

        Name(Token token) {
            this(token.getValue());
        }

        public Name(String value) { // TODO: return to package-private
            super(value);
        }

        public static Name parse(String input) throws ParsingException {
            if (input == null)
                throw new NullInputException();

            return new Name(Token.parse(input));
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) return true;
            if (that == null || getClass() != that.getClass()) return false;
            Name name = (Name) that;
            return Objects.equals(this.getValue().toLowerCase(), name.getValue().toLowerCase());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getValue().toLowerCase());
        }
    }


    public static class Value {

        private final String value;

        Value(String value) {
            this.value = value;
        }

        public static Value parse(String input) throws ParsingException {
            if (input == null)
                throw new NullInputException();

            char[] chars = input.toCharArray();
            for (int index = 0, increment = 1; index < chars.length; index += increment, increment = 1) {
                char c = chars[index];

                if (c == '\t')
                    continue;

                if (c == '\r'
                    && chars.length > index + 2
                    && chars[index + 1] == '\n'
                    && (chars[index + 2] == ' ' || chars[index + 2] == '\t')) {

                    increment = 3;
                    continue;
                }

                if (isAsciiControlChar(c))
                    throw new InvalidCharacterException(input, c, index);
            }

            return new Value(input.replaceAll("\\r\\n[ \\t]+", " ").trim());
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) return true;
            if (that == null || getClass() != that.getClass()) return false;
            Value value = (Value) that;
            return Objects.equals(this.value, value.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
