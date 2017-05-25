package org.iokit.core.write;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.OutputStream;

public abstract class Writer<V> implements Closeable {

    public final OutputStream out;

    public Writer(OutputStream out) {
        this.out = out;
    }

    public abstract void write(V value);

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
