package org.iokit.core;

import org.iokit.core.write.OutputStreamSegmenter;

import java.io.Closeable;

import static java.util.Objects.requireNonNull;

public abstract class IOKitWriter<V> implements Closeable {

    public final IOKitOutputStream out;
    public final OutputStreamSegmenter segmenter;

    public IOKitWriter(IOKitOutputStream out) {
        this(out, new OutputStreamSegmenter(out));
    }

    public IOKitWriter(IOKitOutputStream out, OutputStreamSegmenter segmenter) {
        this.out = requireNonNull(out);
        this.segmenter = requireNonNull(segmenter);
    }

    public abstract void write(V value);

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
