package org.iokit.core.read;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public abstract class Reader<T> implements Closeable { // TODO: use V vs T throughout reader hierarchy?

    public final InputStream in;
    public final InputStreamCursor cursor;

    public Reader(InputStream in) {
        this(in, new InputStreamCursor(in));
    }

    public Reader(InputStream in, InputStreamCursor cursor) {
        this.in = requireNonNull(in);
        this.cursor = requireNonNull(cursor);
    }

    public abstract T read() throws ReaderException; // TODO: stop declaring unchecked exceptions at interface level?

    @Override
    public void close() {
        Try.toRun(in::close);
    }
}
