package org.iokit.general;

import org.iokit.core.IOKitException;

public class EndOfInputException extends IOKitException {

    public EndOfInputException(String message) {
        super(message);
    }

    public EndOfInputException() {
        super();
    }
}
