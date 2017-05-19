package org.iokit.core.read;

public class EndOfInputException extends ReaderException {

    public EndOfInputException(String message) {
        super(message);
    }

    public EndOfInputException() {
        super();
    }
}
