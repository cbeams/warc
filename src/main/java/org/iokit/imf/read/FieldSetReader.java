package org.iokit.imf.read;

import org.iokit.imf.FieldSet;

import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.validate.Validator;

import java.util.function.Supplier;

import static java.util.stream.Collectors.toCollection;

public class FieldSetReader extends Reader<FieldSet> {

    private final FieldReader fieldReader;
    private final Validator<FieldSet> fieldSetValidator;

    public FieldSetReader(FieldReader fieldReader, Validator<FieldSet> fieldSetValidator) {
        super(fieldReader.getInput());
        this.fieldReader = fieldReader;
        this.fieldSetValidator = fieldSetValidator;
    }

    public FieldSet read() throws ReaderException {
        FieldSet fieldSet = fieldReader.stream().collect(toCollection(newFieldSet()));
        fieldSetValidator.validate(fieldSet);
        return fieldSet;
    }

    protected Supplier<FieldSet> newFieldSet() {
        return FieldSet::new;
    }
}
