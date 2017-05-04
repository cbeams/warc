package org.iokit.core.read;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

/**
 * @see FixedLengthReader
 */
public interface Reader<T> {

    T read() throws EOFException, ParsingException;
}
