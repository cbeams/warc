package org.iokit.core.write;

import org.iokit.lang.Try;

public class ConcatenationWriter<V> extends Writer<V> {

    private final Writer<V> valueWriter;
    private final ParameterlessWriter concatenatorWriter;

    public ConcatenationWriter(Writer<V> valueWriter, ParameterlessWriter concatenatorWriter) {
        super(valueWriter.out);
        this.valueWriter = valueWriter;
        this.concatenatorWriter = concatenatorWriter;
    }

    @Override
    public void write(V record) {
        valueWriter.write(record);
        concatenatorWriter.write();
    }

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
