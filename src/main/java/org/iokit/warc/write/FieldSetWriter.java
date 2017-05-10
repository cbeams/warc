package org.iokit.warc.write;

import org.iokit.imf.Field;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Set;

public class FieldSetWriter {

    private final FieldWriter fieldWriter;

    public FieldSetWriter(OutputStream output) {
        this(new FieldWriter(output));
    }

    public FieldSetWriter(FieldWriter fieldWriter) {
        this.fieldWriter = fieldWriter;
    }

    public void write(Set<Field> fields) throws IOException {
        for (Field field : fields)
            fieldWriter.write(field);
    }
}
