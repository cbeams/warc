package org.iokit.core.read;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

/**
 * @see Reader
 */
public interface FixedLengthReader<T> {

    T read(int length) throws EOFException, ParsingException;
}
