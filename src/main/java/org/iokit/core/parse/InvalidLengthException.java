package org.iokit.core.parse;

public class InvalidLengthException extends ParsingException {

    public InvalidLengthException(String input, int minLength) {
        super("input [%s] must contain at least %d character(s)", input, minLength);
    }
}
