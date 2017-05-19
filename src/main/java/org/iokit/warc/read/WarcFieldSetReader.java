package org.iokit.warc.read;

import org.iokit.warc.validate.WarcFieldSetValidator;

import org.iokit.imf.read.FieldReader;
import org.iokit.imf.read.FieldSetReader;

import org.iokit.imf.Field;

import org.iokit.core.read.FoldedLineReader;
import org.iokit.core.read.LineReader;

import org.iokit.core.validate.Validator;

import java.util.Set;

public class WarcFieldSetReader extends FieldSetReader {

    public WarcFieldSetReader(LineReader lineReader) {
        this(new FoldedLineReader(lineReader));
    }

    public WarcFieldSetReader(FoldedLineReader lineReader) {
        this(new FieldReader(lineReader), new WarcFieldSetValidator());
    }

    public WarcFieldSetReader(FieldReader fieldReader, Validator<Set<Field>> fieldSetValidator) {
        super(fieldReader, fieldSetValidator);
    }
}
