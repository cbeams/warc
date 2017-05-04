package org.iokit.core.parse;

public class ParsingException extends Exception {

    public ParsingException(String message, Object... args) {
        super(String.format(message, args));
    }
}
