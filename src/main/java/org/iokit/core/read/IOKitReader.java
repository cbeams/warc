package org.iokit.core.read;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.Try;

import java.io.Closeable;

import static java.util.Objects.requireNonNull;

public abstract class IOKitReader<V> implements Closeable {

    public final IOKitInputStream in;

    public IOKitReader(IOKitInputStream in) {
        this.in = requireNonNull(in);
    }

    public abstract V read();

    @Override
    public void close() {
        Try.toRun(in::close);
    }
}
