package org.iokit.core.read;

/**
 * @see FixedLengthReader
 */
public interface Reader<T> {

    T read() throws ReaderException;
}
