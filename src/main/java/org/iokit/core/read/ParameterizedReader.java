package org.iokit.core.read;

import java.io.EOFException;

public interface ParameterizedReader<P, T> {

    T read(P param) throws ReaderException, EOFException;
}
