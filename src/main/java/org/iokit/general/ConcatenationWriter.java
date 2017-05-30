package org.iokit.general;

import org.iokit.core.IOKitWriter;
import org.iokit.core.Try;

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
        out.startSegment();

        valueWriter.write(value);
        concatenatorWriter.write();

        out.finishSegment();
    }

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
