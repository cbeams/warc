package org.iokit.imf;

import org.iokit.core.parse.ParsingException;

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
        return Objects.equals(this.getValue().toLowerCase(), name.getValue().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getValue().toLowerCase());
    }


    public static class Parser implements org.iokit.core.parse.Parser<FieldName> {

        private final org.iokit.core.parse.Parser<Token> tokenParser;

        public Parser() {
            this(new TokenParser());
        }

        public Parser(org.iokit.core.parse.Parser<Token> tokenParser) {
            this.tokenParser = tokenParser;
        }

        public FieldName parse(String input) throws ParsingException {
            return new FieldName(tokenParser.parse(input).getValue());
        }
    }
}
