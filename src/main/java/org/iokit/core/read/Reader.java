package org.iokit.core.read;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.InputStream;

public abstract class Reader<T> implements Closeable {

    public final InputStream in;

    public Reader(InputStream in) {
        this.in = in;
    }

    public abstract T read() throws ReaderException;

    @Override
    public void close() {
        Try.toRun(in::close);
    }
}
