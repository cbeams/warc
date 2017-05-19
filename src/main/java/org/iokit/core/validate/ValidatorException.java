package org.iokit.core.validate;

import org.iokit.lang.UncheckedException;

public class ValidatorException extends UncheckedException {

    public ValidatorException(String message, Object... args) {
        super(String.format(message, args));
    }
}
