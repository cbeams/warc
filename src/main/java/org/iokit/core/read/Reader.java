package org.iokit.core.read;

import org.iokit.core.input.Input;

/**
 * @see FixedLengthReader
 */
public interface Reader<T> {

    T read() throws ReaderException;

    Input getInput();
}
