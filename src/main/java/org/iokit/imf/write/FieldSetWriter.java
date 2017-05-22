package org.iokit.imf.write;

import org.iokit.imf.Field;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

import java.util.Set;

public class FieldSetWriter extends Writer<Set<Field>> {

    private final FieldWriter fieldWriter;

    public FieldSetWriter(LineWriter lineWriter) {
        this(new FieldWriter(lineWriter));
    }

    public FieldSetWriter(FieldWriter fieldWriter) {
        super(fieldWriter.out);
        this.fieldWriter = fieldWriter;
    }

    public void write(Set<Field> fields) {
        fields.forEach(fieldWriter::write);
    }
}
