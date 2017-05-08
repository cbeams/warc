package org.iokit.core.validate;

import org.iokit.core.IOKitException;

public class ValidatorException extends IOKitException {

    public ValidatorException(String message, Object... args) {
        super(String.format(message, args));
    }
}
