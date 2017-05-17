package org.iokit.warc.write;

import org.iokit.imf.Field;

import java.io.IOException;
import java.io.OutputStream;

public class FieldWriter {

    private final OutputStream output;

    public FieldWriter(OutputStream output) {
        this.output = output;
    }

    public void write(Field field) throws IOException {

        output.write(field.getName().getValue().getBytes());
        output.write(": ".getBytes());
        output.write(field.getValue().getFoldedValue().getBytes());
        output.write("\r\n".getBytes());
    }
}
