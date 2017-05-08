package org.iokit.core.read;

import java.io.EOFException;

/**
 * @see Reader
 */
public interface FixedLengthReader<T> {

    T read(int length) throws ReaderException, EOFException;
}
