package org.iokit.imf;

import org.iokit.line.LineWriter;

import org.iokit.core.read.ReaderException;

import org.iokit.core.validate.Validator;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toCollection;

public class FieldSet extends LinkedHashSet<Field> {

    public Optional<String> getFieldValue(String name) {
        return getFieldValue(new FieldName(name));
    }

    public Optional<String> getFieldValue(DefinedField definedField) {
        return getFieldValue(definedField.fieldName());
    }

    public Optional<String> getFieldValue(FieldName name) {
        Optional<Field> field = getField(name);
        return field.isPresent() ?
            Optional.of(field.get().getValue()).map(FieldValue::toString) :
            Optional.empty();
    }

    public Optional<Field> getField(String name) {
        return getField(new FieldName(name));
    }

    public Optional<Field> getField(FieldName name) {
        return this.stream()
            .filter(field -> field.getName().equals(name))
            .findFirst();
    }

    public String getRequiredFieldValue(DefinedField field) {
        return getRequiredFieldValue(field, Function.identity());
    }

    public <T> T getRequiredFieldValue(DefinedField field, Function<String, T> mapper) {
        return getFieldValue(field).map(mapper).orElseThrow(() -> new FieldNotFoundException(field.fieldName()));
    }


    public abstract static class Reader<F extends FieldSet> extends org.iokit.core.read.Reader<F> {

        private final Field.Reader fieldReader;
        private final Validator<F> fieldSetValidator;

        public Reader(Field.Reader fieldReader, Validator<F> fieldSetValidator) {
            super(fieldReader.in);
            this.fieldReader = fieldReader;
            this.fieldSetValidator = fieldSetValidator;
        }

        public F read() throws ReaderException {
            F fieldSet = fieldReader.stream().collect(toCollection(newFieldSet()));
            fieldSetValidator.validate(fieldSet);
            return fieldSet;
        }

        protected abstract Supplier<F> newFieldSet();
    }


    public static class Writer extends org.iokit.core.write.Writer<FieldSet> {

        private final Field.Writer fieldWriter;

        public Writer(LineWriter lineWriter) {
            this(new Field.Writer(lineWriter));
        }

        public Writer(Field.Writer fieldWriter) {
            super(fieldWriter.out);
            this.fieldWriter = fieldWriter;
        }

        public void write(FieldSet fieldSet) {
            fieldSet.forEach(fieldWriter::write);
        }
    }
}
