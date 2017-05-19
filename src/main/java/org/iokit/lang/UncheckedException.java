package org.iokit.lang;

public class UncheckedException extends RuntimeException {

    public UncheckedException(Throwable cause, String message, Object[] args) {
        super(String.format(message, args), cause);
    }

    public UncheckedException(String message, Object... args) {
        super(String.format(message, args));
    }

    public UncheckedException(Throwable cause) {
        super(cause);
    }

    public UncheckedException() {
    }
}
