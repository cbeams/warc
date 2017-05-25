package org.iokit.core.read;

public interface ParameterizedReader<P, T> { // TODO: extend Reader and throw UOE from read()?

    T read(P param) throws ReaderException;
}
