package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.TransformReader;
import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;

import java.util.LinkedHashSet;
import java.util.Set;

public class FieldSetReader extends TransformReader<FieldReader, Set<Field>> {

    public FieldSetReader(LineReader reader) {
        this(new FieldReader(new FoldedLineReader(reader)));
    }

    public FieldSetReader(FieldReader reader) {
        super(reader);
    }

    public Set<Field> read() throws ReaderException {
        LinkedHashSet<Field> fields = new LinkedHashSet<>();

        Field field;
        while ((field = reader.read()) != null)
            fields.add(field);

        // TODO: validate that fields have required fields e.g. WARC-Type

        return fields;
    }
}
