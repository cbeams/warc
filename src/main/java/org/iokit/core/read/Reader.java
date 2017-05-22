package org.iokit.core.read;

import org.iokit.core.input.InputStreamCursor;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public abstract class Reader<T> implements Closeable {

    public final InputStream in;
    public final InputStreamCursor cursor;

    public Reader(InputStream in) {
        this(in, new InputStreamCursor(in));
    }

    public Reader(InputStream in, InputStreamCursor cursor) {
        this.in = requireNonNull(in);
        this.cursor = requireNonNull(cursor);
    }

    public abstract T read() throws ReaderException;

    @Override
    public void close() {
        Try.toRun(in::close);
    }
}
