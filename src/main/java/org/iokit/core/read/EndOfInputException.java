package org.iokit.core.read;

public class EndOfInputException extends IOKitReader.Exception {

    public EndOfInputException(String message) {
        super(message);
    }

    public EndOfInputException() {
        super();
    }
}
