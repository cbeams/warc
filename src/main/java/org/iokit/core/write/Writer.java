package org.iokit.core.write;

import org.iokit.core.read.ReaderException;

public abstract class Writer<T> {

    public abstract void write(T value) throws ReaderException;
}
