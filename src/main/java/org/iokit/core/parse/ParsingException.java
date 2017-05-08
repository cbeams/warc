package org.iokit.core.parse;

import org.iokit.core.IOKitException;

public class ParsingException extends IOKitException {

    public ParsingException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }
}
