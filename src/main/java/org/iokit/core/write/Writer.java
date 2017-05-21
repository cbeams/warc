package org.iokit.core.write;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.OutputStream;

public abstract class Writer<T> implements Closeable {

    protected final OutputStream output;

    public Writer(OutputStream output) {
        this.output = output;
    }

    public abstract void write(T value);

    public OutputStream getOutput() {
        return output;
    }

    @Override
    public void close() {
        Try.toRun(output::close);
    }
}
