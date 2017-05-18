package org.iokit.core.read;

public interface ParameterizedReader<P, T> {

    T read(P param) throws ReaderException;
}
