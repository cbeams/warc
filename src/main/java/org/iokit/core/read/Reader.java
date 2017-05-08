package org.iokit.core.read;

import java.io.EOFException;

/**
 * @see FixedLengthReader
 */
public interface Reader<T> {

    T read() throws ReaderException, EOFException;
}
