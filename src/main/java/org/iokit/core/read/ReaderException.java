package org.iokit.core.read;

import org.iokit.core.IOKitException;

public class ReaderException extends IOKitException {

    public ReaderException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public ReaderException(String message, Object... args) {
        super(message, args);
    }

    public ReaderException(Throwable cause) {
        super(cause);
    }

    public ReaderException() {
        super();
    }
}
