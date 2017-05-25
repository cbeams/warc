package org.iokit.imf;

import java.util.Objects;

/**
 * Field names are case-insensitive.
 */
public class FieldName extends Token {

    public FieldName(String value) {
        super(value);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        FieldName name = (FieldName) that;
        return this.value.equalsIgnoreCase(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }


    public static class Parser extends Token.Parser {

        public Parser() {
            this(Specials.NONE);
        }

        public Parser(Specials specials) {
            this(new Token.Validator(specials));
        }

        public Parser(Token.Validator validator) {
            super(validator);
        }

        public FieldName parse(String input) {
            return new FieldName(super.parse(input).value);
        }
    }
}
