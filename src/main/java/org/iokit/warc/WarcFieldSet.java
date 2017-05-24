package org.iokit.warc;

import org.iokit.imf.Field;
import org.iokit.imf.FieldSet;
import org.iokit.imf.FoldedLine;

import org.iokit.line.LineReader;

import org.iokit.core.validate.ValidatorException;

import java.util.function.Supplier;

public class WarcFieldSet extends FieldSet {

    public static class Validator implements org.iokit.core.validate.Validator<FieldSet> { // TODO: pull up to (abstract?) FieldSet.Validator base class

        @Override
        public void validate(FieldSet fieldSet) throws ValidatorException {
            // TODO: actually validate required fields, etc
        }
    }


    public static class Reader extends FieldSet.Reader {

        public static final org.iokit.core.validate.Validator<FieldSet> DEFAULT_FIELD_SET_VALIDATOR = new Validator();

        public Reader(LineReader lineReader) {
            this(new FoldedLine.Reader(lineReader.in));
        }

        public Reader(FoldedLine.Reader lineReader) {
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
