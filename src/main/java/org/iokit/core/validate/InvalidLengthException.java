package org.iokit.core.validate;

import org.iokit.core.IOKitException;

public class InvalidLengthException extends IOKitException {

    public InvalidLengthException(String input, int minLength) {
        super("input [%s] must contain at least %d character(s)", input, minLength);
    }
}
