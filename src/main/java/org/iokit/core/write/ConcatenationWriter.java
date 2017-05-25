package org.iokit.core.write;

import org.iokit.lang.Try;

public class ConcatenationWriter<T> extends Writer<T> {

    private final Writer<T> valueWriter;
    private final ParameterlessWriter concatenatorWriter;

    public ConcatenationWriter(Writer<T> valueWriter, ParameterlessWriter concatenatorWriter) {
        super(valueWriter.out);
        this.valueWriter = valueWriter;
        this.concatenatorWriter = concatenatorWriter;
    }

    @Override
    public void write(T record) {
        valueWriter.write(record);
        concatenatorWriter.write();
    }

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
