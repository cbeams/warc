package org.iokit.core.read;

/**
 * @see Reader
 */
public interface FixedLengthReader<T> {

    T read(int length) throws ReaderException;
}
