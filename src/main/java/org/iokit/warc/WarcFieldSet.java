package org.iokit.warc;

import org.iokit.imf.read.FieldSetReader;
import org.iokit.imf.read.FoldedLineReader;

import org.iokit.imf.Field;
import org.iokit.imf.FieldSet;

import org.iokit.core.read.LineReader;

import org.iokit.core.validate.ValidatorException;

import java.util.function.Supplier;

public class WarcFieldSet extends FieldSet {

    public static class Validator implements org.iokit.core.validate.Validator<FieldSet> {

        @Override
        public void validate(FieldSet fieldSet) throws ValidatorException {
            // TODO: actually validate required fields, etc
        }
    }


    public static class Reader extends FieldSetReader {

        public static final org.iokit.core.validate.Validator<FieldSet> DEFAULT_FIELD_SET_VALIDATOR = new Validator();

        public Reader(LineReader lineReader) {
            this(new FoldedLineReader(lineReader.in));
        }

        public Reader(FoldedLineReader lineReader) {
            this(new WarcField.Reader(lineReader), DEFAULT_FIELD_SET_VALIDATOR);
        }

        public Reader(Field.Reader fieldReader, org.iokit.core.validate.Validator<FieldSet> fieldSetValidator) {
            super(fieldReader, fieldSetValidator);
        }

        @Override
        protected Supplier<FieldSet> newFieldSet() {
            return WarcFieldSet::new;
        }
    }
}
