package org.iokit.core.write;

import org.iokit.lang.Try;

public class ConcatenationWriter<V> extends IOKitWriter<V> {

    private final IOKitWriter<V> valueWriter;
    private final ParameterlessWriter concatenatorWriter;

    public ConcatenationWriter(IOKitWriter<V> valueWriter, ParameterlessWriter concatenatorWriter) {
        super(valueWriter.out);
        this.valueWriter = valueWriter;
        this.concatenatorWriter = concatenatorWriter;
    }

    @Override
    public void write(V value) {
        if (segmenter.supported)
            segmenter.startSegment();

        valueWriter.write(value);
        concatenatorWriter.write();

        if (segmenter.supported)
            segmenter.finishSegment();
    }

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
