package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

import java.util.LinkedHashSet;
import java.util.Set;

public class FieldSetReader implements Reader<Set<Field>> {

    private final FieldReader fieldReader;

    public FieldSetReader(LineReader lineReader) {
        this(new FieldReader(lineReader));
    }

    public FieldSetReader(FieldReader fieldReader) {
        this.fieldReader = fieldReader;
    }

    public Set<Field> read() throws EOFException, ParsingException {
        LinkedHashSet<Field> fields = new LinkedHashSet<>();

        Field field;
        while ((field = fieldReader.read()) != null)
            fields.add(field);

        // TODO: validate that fields have required fields e.g. WARC-Type

        return fields;
    }
}
