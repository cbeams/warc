package org.iokit.message;

import org.iokit.core.write.IOKitWriter;

import org.iokit.core.read.IOKitReader;

import org.iokit.core.validate.IOKitValidator;

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

    public Optional<Field> getField(DefinedField field) {
        return getField(field.fieldName());
    }

    public Optional<Field> getField(FieldName name) {
        return this.stream()
            .filter(field -> field.getName().equals(name))
            .findFirst();
    }

    public String getRequiredFieldValue(DefinedField field) {
        return getRequiredFieldValue(field, Function.identity());
    }

    public <V> V getRequiredFieldValue(DefinedField field, Function<String, V> mapper) {
        return getFieldValue(field).map(mapper).orElseThrow(() -> new FieldNotFoundException(field.fieldName()));
    }


    public abstract static class Reader<FS extends FieldSet> extends IOKitReader<FS> {

        private final Field.Reader fieldReader;
        private final Validator<FS> fieldSetValidator;

        public Reader(Field.Reader fieldReader, FieldSet.Validator<FS> fieldSetValidator) {
            super(fieldReader.in);
            this.fieldReader = fieldReader;
            this.fieldSetValidator = fieldSetValidator;
        }

        public FS read() {
            FS fieldSet = fieldReader.stream().collect(toCollection(newFieldSet()));
            fieldSetValidator.validate(fieldSet);
            return fieldSet;
        }

        protected abstract Supplier<FS> newFieldSet();
    }


    public abstract static class Writer<FS extends FieldSet> extends IOKitWriter<FS> {

        private final Field.Writer fieldWriter;

        public Writer(Field.Writer fieldWriter) {
            super(fieldWriter.out);
            this.fieldWriter = fieldWriter;
        }

        public final void write(FS fieldSet) {
            fieldSet.forEach(fieldWriter::write);
            writeAfterFields();
        }

        protected abstract void writeAfterFields();
    }


    public abstract static class Validator<FS extends FieldSet> implements IOKitValidator<FS> {
    }
}
