package org.iokit.core;

import org.iokit.core.write.OutputStreamSegmenter;

import java.io.Closeable;
import java.io.OutputStream;

import static java.util.Objects.requireNonNull;

public abstract class IOKitWriter<V> implements Closeable {

    public final OutputStream out;
    public final OutputStreamSegmenter segmenter;

    public IOKitWriter(OutputStream out) {
        this(out, new OutputStreamSegmenter(out));
    }

    public IOKitWriter(OutputStream out, OutputStreamSegmenter segmenter) {
        this.out = requireNonNull(out);
        this.segmenter = requireNonNull(segmenter);
    }

    public abstract void write(V value);

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
