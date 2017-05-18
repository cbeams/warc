package org.iokit.core;

public class IOKitException extends RuntimeException {

    public IOKitException(String message, Object... args) {
        super(String.format(message, args));
    }

    public IOKitException(Throwable cause) {
        super(cause);
    }

    public IOKitException() {
    }
}
