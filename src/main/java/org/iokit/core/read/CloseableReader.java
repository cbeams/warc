package org.iokit.core.read;

import org.iokit.lang.Try;

import java.io.Closeable;

public abstract class CloseableReader<T> implements Reader<T> {

    private final Closeable closeable;

    public CloseableReader(Closeable closeable) {
        this.closeable = closeable;
    }

    @Override
    public void close() {
        Try.toRun(closeable::close);
    }
}
