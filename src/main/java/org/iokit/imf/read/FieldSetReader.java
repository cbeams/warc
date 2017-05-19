package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;
import org.iokit.core.read.TransformReader;

import java.util.LinkedHashSet;
import java.util.Optional;
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

        Optional<Field> field;
        while ((field = reader.readOptional()).isPresent())
            fields.add(field.get());

        // TODO: validate that fields have required fields e.g. WARC-Type

        return fields;
    }
}
