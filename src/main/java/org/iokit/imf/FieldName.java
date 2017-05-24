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
        return this.value.equalsIgnoreCase(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }


    public static class Parser implements org.iokit.core.parse.Parser<FieldName> {

        private final TokenParser tokenParser;

        public Parser() {
            this(Specials.NONE);
        }

        public Parser(Specials specials) {
            this(new TokenValidator(specials));
        }

        public Parser(TokenValidator tokenValidator) {
            this.tokenParser = new TokenParser(tokenValidator);
        }

        public Parser(TokenParser tokenParser) {
            this.tokenParser = tokenParser;
        }

        public FieldName parse(String input) throws ParsingException {
            return new FieldName(tokenParser.parse(input).value);
        }
    }
}
