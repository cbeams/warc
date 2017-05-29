package org.iokit.core.parse;

import org.iokit.core.IOKitException;

public interface IOKitParser<V> {

    V parse(String input);


    class Exception extends IOKitException {

        public Exception(String message, Object... args) {
            super(String.format(message, args));
        }
    }
}
