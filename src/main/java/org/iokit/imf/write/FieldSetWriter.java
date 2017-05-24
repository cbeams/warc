package org.iokit.imf.write;

import org.iokit.imf.Field;
import org.iokit.imf.FieldSet;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

public class FieldSetWriter extends Writer<FieldSet> {

    private final Field.Writer fieldWriter;

    public FieldSetWriter(LineWriter lineWriter) {
        this(new Field.Writer(lineWriter));
    }

    public FieldSetWriter(Field.Writer fieldWriter) {
        super(fieldWriter.out);
        this.fieldWriter = fieldWriter;
    }

    public void write(FieldSet fieldSet) {
        fieldSet.forEach(fieldWriter::write);
    }
}
