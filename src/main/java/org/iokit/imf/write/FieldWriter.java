package org.iokit.imf.write;

import org.iokit.imf.Field;

import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class FieldWriter extends Writer<Field> {

    private final OutputStream output;

    public FieldWriter(OutputStream output) {
        this.output = output;
    }

    public void write(Field field) {
        Try.toRun(() -> {
            output.write(field.getName().getValue().getBytes());
            output.write(": ".getBytes());
            output.write(field.getValue().getFoldedValue().getBytes());
            output.write("\r\n".getBytes());
        });
    }
}
