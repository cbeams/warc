package org.iokit.core.parse;

public class InvalidCharacterException extends ParsingException {

    public InvalidCharacterException(String input) {
        super("input [%s] contains one or more invalid characters", input);
    }

    public InvalidCharacterException(String input, char c, int index) {
        super("input [%s] contains invalid character '%c' (decimal value '%d') at index %d", input, c, (int) c, index);
    }
}
