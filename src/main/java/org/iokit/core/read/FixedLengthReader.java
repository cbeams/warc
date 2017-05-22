package org.iokit.core.read;

public interface FixedLengthReader<T> {

    T read(int length) throws ReaderException;
}
