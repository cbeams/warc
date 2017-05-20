package org.iokit.imf.read;

import org.iokit.imf.Field;

import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.validate.Validator;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

public class FieldSetReader extends Reader<Set<Field>> {

    private final FieldReader fieldReader;
    private final Validator<Set<Field>> fieldSetValidator;

    public FieldSetReader(FieldReader fieldReader, Validator<Set<Field>> fieldSetValidator) {
        super(fieldReader.getInput());
        this.fieldReader = fieldReader;
        this.fieldSetValidator = fieldSetValidator;
    }

    public Set<Field> read() throws ReaderException {
        Set<Field> fields = fieldReader.stream().collect(toCollection(LinkedHashSet::new));
        fieldSetValidator.validate(fields);
        return fields;
    }
}
