package org.iokit.core.read;

import org.iokit.core.IOKitException;
import org.iokit.core.Try;

import java.io.Closeable;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public abstract class IOKitReader<V> implements Closeable {

    public final InputStream in;
    public final InputStreamCursor cursor;

    public IOKitReader(InputStream in) {
        this(in, new InputStreamCursor(in));
    }

    public IOKitReader(InputStream in, InputStreamCursor cursor) {
        this.in = requireNonNull(in);
        this.cursor = requireNonNull(cursor);
    }

    public abstract V read();

    @Override
    public void close() {
        Try.toRun(in::close);
    }


    public static class Exception extends IOKitException {

        public Exception(Throwable cause, String message, Object... args) {
            super(cause, message, args);
        }

        public Exception(String message, Object... args) {
            super(message, args);
        }

        public Exception(Throwable cause) {
            super(cause);
        }

        public Exception() {
            super();
        }
    }
}
