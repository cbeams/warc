package org.iokit.imf.write;

import org.iokit.imf.Field;

import org.iokit.core.write.Writer;

import java.io.OutputStream;

import java.util.Set;

public class FieldSetWriter extends Writer<Set<Field>> {

    private final FieldWriter fieldWriter;

    public FieldSetWriter(OutputStream output) {
        this(new FieldWriter(output));
    }

    public FieldSetWriter(FieldWriter fieldWriter) {
        this.fieldWriter = fieldWriter;
    }

    public void write(Set<Field> fields) {
        for (Field field : fields)
            fieldWriter.write(field);
    }
}
