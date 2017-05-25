package org.iokit.core.read;

public interface FixedLengthReader<T> { // TODO: extend Reader and throw UOE from read()?

    T read(int length) throws ReaderException;
}
