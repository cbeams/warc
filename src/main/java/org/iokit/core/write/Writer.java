package org.iokit.core.write;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.OutputStream;

public abstract class Writer<T> implements Closeable {

    protected final OutputStream out;

    public Writer(OutputStream out) {
        this.out = out;
    }

    public abstract void write(T value);

    public OutputStream getOutputStream() {
        return out;
    }

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
