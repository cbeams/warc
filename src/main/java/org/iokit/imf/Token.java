package org.iokit.imf;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.InvalidLengthException;
import org.iokit.core.validate.ValidatorException;

import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.ValidatingParser;

import java.util.Objects;

import static org.iokit.core.Ascii.isAsciiChar;
import static org.iokit.core.Ascii.isAsciiControlChar;

public class Token {

    protected final String value;

    public Token(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Token token = (Token) that;
        return Objects.equals(this.value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }


    public static class Validator implements org.iokit.core.validate.Validator<String> {

        public static final int MIN_TOKEN_LENGTH = 1;

        private final Specials specials;

        public Validator(Specials specials) {
            this.specials = specials;
        }

        @Override
        public void validate(String input) throws ValidatorException {
            if (input.length() < MIN_TOKEN_LENGTH)
                throw new InvalidLengthException(input, MIN_TOKEN_LENGTH);

            if (input.chars().anyMatch(c -> !isAsciiChar(c) || isAsciiControlChar(c) || specials.isSpecial(c)))
                throw new InvalidCharacterException(input);
        }
    }


    public static class Parser extends ValidatingParser<Token> {

        public Parser(Specials specials) {
            this(new Validator(specials));
        }

        public Parser(org.iokit.core.validate.Validator validator) {
            super(validator);
        }

        public Token parseValidated(String input) throws ParsingException {
            return new Token(input);
        }
    }
}
