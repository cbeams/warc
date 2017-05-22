package org.iokit.warc.read;

import org.iokit.warc.WarcFieldSet;

import org.iokit.imf.read.FieldReader;
import org.iokit.imf.read.FieldSetReader;
import org.iokit.imf.read.FoldedLineReader;

import org.iokit.imf.FieldSet;

import org.iokit.core.read.LineReader;

import org.iokit.core.validate.Validator;

import java.util.function.Supplier;

public class WarcFieldSetReader extends FieldSetReader {

    public static final Validator<FieldSet> DEFAULT_FIELD_SET_VALIDATOR = new WarcFieldSet.Validator();

    public WarcFieldSetReader(LineReader lineReader) {
        this(new FoldedLineReader(lineReader.getInputStream()));
    }

    public WarcFieldSetReader(FoldedLineReader lineReader) {
        this(new FieldReader(lineReader), DEFAULT_FIELD_SET_VALIDATOR);
    }

    public WarcFieldSetReader(FieldReader fieldReader, Validator<FieldSet> fieldSetValidator) {
        super(fieldReader, fieldSetValidator);
    }

    @Override
    protected Supplier<FieldSet> newFieldSet() {
        return WarcFieldSet::new;
    }
}
