package org.iokit.imf.write;

import org.iokit.imf.FieldSet;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

public class FieldSetWriter extends Writer<FieldSet> {

    private final FieldWriter fieldWriter;

    public FieldSetWriter(LineWriter lineWriter) {
        this(new FieldWriter(lineWriter));
    }

    public FieldSetWriter(FieldWriter fieldWriter) {
        super(fieldWriter.out);
        this.fieldWriter = fieldWriter;
    }

    public void write(FieldSet fieldSet) {
        fieldSet.forEach(fieldWriter::write);
    }
}
