package org.iokit.core.validate;

import org.iokit.core.IOKitException;

public interface IOKitValidator<V> {

    void validate(V input);

    class Exception extends IOKitException {

        public Exception(String message, Object... args) {
            super(String.format(message, args));
        }
    }
}
