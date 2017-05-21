package org.iokit.imf.write;

import org.iokit.imf.Field;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

public class FieldWriter extends Writer<Field> {

    private final LineWriter lineWriter;

    public FieldWriter(LineWriter lineWriter) {
        super(lineWriter.getOutput());
        this.lineWriter = lineWriter;
    }

    public void write(Field field) {
        lineWriter.write(String.format("%s%c %s", field.getName(), Field.SEPARATOR, field.getValue().getFoldedValue()));
    }
}
