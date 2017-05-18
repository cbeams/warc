package org.iokit.core.read;

import org.iokit.core.input.Input;

import java.io.Closeable;

/**
 * @see FixedLengthReader
 */
public interface Reader<T> extends Closeable {

    T read() throws ReaderException;

    Input getInput();

    @Override
    void close();
}
