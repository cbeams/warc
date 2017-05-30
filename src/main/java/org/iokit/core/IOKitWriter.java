package org.iokit.core;

import java.io.Closeable;

import static java.util.Objects.requireNonNull;

public abstract class IOKitWriter<V> implements Closeable {

    public final IOKitOutputStream out;

    public IOKitWriter(IOKitOutputStream out) {
        this.out = requireNonNull(out);
    }

    public abstract void write(V value);

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
