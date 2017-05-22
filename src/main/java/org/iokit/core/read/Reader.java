package org.iokit.core.read;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.InputStream;

public abstract class Reader<T> implements Closeable {

    protected final InputStream input;

    public Reader(InputStream input) {
        this.input = input;
    }

    public abstract T read() throws ReaderException;

    public InputStream getInput() {
        return input;
    }

    @Override
    public void close() {
        Try.toRun(input::close);
    }
}
