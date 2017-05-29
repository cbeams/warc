package org.iokit.core.write;

import org.iokit.lang.Try;

import java.io.Closeable;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

public abstract class Writer<V> implements Closeable {

    public final OutputStream out;
    public final OutputStreamSegmenter segmenter;

    public Writer(OutputStream out) {
        this(out, new OutputStreamSegmenter(out));
    }

    public Writer(OutputStream out, OutputStreamSegmenter segmenter) {
        this.out = requireNonNull(out);
        this.segmenter = requireNonNull(segmenter);
    }

    public abstract void write(V value);

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
