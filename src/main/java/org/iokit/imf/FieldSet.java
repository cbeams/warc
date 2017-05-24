package org.iokit.imf;

import org.iokit.line.LineWriter;

import org.iokit.core.read.ReaderException;

import org.iokit.core.validate.Validator;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toCollection;

public class FieldSet extends LinkedHashSet<Field> {

    // TODO: do 2-newline spaces in every case where there are only nested classes
    public static class Reader extends org.iokit.core.read.Reader<FieldSet> {

        private final Field.Reader fieldReader;
        private final Validator<FieldSet> fieldSetValidator;

        public Reader(Field.Reader fieldReader, Validator<FieldSet> fieldSetValidator) {
            super(fieldReader.in);
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
