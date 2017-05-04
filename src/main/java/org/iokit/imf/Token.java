package org.iokit.imf;

import org.iokit.core.parse.InvalidCharacterException;
import org.iokit.core.parse.InvalidLengthException;
import org.iokit.core.parse.NullInputException;
import org.iokit.core.parse.ParsingException;

import java.util.Objects;

import static org.iokit.core.parse.Ascii.*;
import static org.iokit.warc.Separator.isSeparatorChar;

public class Token {

    private static final int MIN_TOKEN_LENGTH = 1;

    private final String value;

    Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Token parse(String input) throws ParsingException {
        if (input == null)
            throw new NullInputException();

        if (input.length() < MIN_TOKEN_LENGTH)
            throw new InvalidLengthException(input, MIN_TOKEN_LENGTH);

        if (input.chars().anyMatch(Token::isIllegalChar))
            throw new InvalidCharacterException(input);

        return new Token(input);
    }

    private static boolean isIllegalChar(int c) {
        return !isAsciiChar(c) || isAsciiControlChar(c) || isSeparatorChar(c);
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
}
