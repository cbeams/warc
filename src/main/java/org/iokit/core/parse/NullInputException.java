package org.iokit.core.parse;

public class NullInputException extends ParsingException {

    public NullInputException() {
        super("input must not be null");
    }
}
