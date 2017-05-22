package org.iokit.core.parse;

import org.iokit.lang.UncheckedException;

public class ParsingException extends UncheckedException {

    public ParsingException(String message, Object... args) {
        super(String.format(message, args));
    }
}
