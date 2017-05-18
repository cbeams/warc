package org.iokit.core.read;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;

public abstract class CloseableReader<T> implements Reader<T> {

    private final Closeable closeable;

    public CloseableReader(Closeable closeable) {
        this.closeable = closeable;
    }

    @Override
    public void close() {
        try {
            closeable.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
