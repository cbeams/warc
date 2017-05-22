package org.iokit.core.read;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.InputStream;

public abstract class Reader<T> implements Closeable {

    protected final InputStream in;

    public Reader(InputStream in) {
        this.in = in;
    }

    public abstract T read() throws ReaderException;

    public InputStream getInputStream() {
        return in;
    }

    @Override
    public void close() {
        Try.toRun(in::close);
    }
}
