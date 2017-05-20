package org.iokit.warc.read;

import org.iokit.warc.WarcField;

import org.iokit.imf.read.FieldReader;
import org.iokit.imf.read.FieldSetReader;

import org.iokit.imf.Field;

import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.LineReader;

import org.iokit.core.validate.Validator;

import java.util.Set;

public class WarcFieldSetReader extends FieldSetReader {

    public static final Validator<Set<Field>> DEFAULT_FIELD_SET_VALIDATOR = new WarcField.SetValidator();

    public WarcFieldSetReader(LineReader lineReader) {
        this(new FoldedLineReader(lineReader.getInput()));
    }

    public WarcFieldSetReader(FoldedLineReader lineReader) {
        this(new FieldReader(lineReader), DEFAULT_FIELD_SET_VALIDATOR);
    }

    public WarcFieldSetReader(FieldReader fieldReader, Validator<Set<Field>> fieldSetValidator) {
        super(fieldReader, fieldSetValidator);
    }
}
