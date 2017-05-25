package org.iokit.warc;

import org.iokit.imf.FieldNotFoundException;
import org.iokit.imf.FieldSet;
import org.iokit.imf.FoldedLine;

import org.iokit.line.LineReader;

import org.iokit.core.validate.ValidatorException;

import java.util.function.Supplier;

import static org.iokit.warc.WarcDefinedField.*;

public class WarcFieldSet extends FieldSet {

    public WarcType getRecordType() {
        return getRequiredFieldValue(WARC_Type, WarcType::typeOf);
    }

    public String getDate() {
        return getRequiredFieldValue(WARC_Date);
    }

    public String getContentType() {
        return getRequiredFieldValue(Content_Type);
    }

    public int getContentLength() {
        return getRequiredFieldValue(Content_Length, Integer::valueOf);
    }

    public String getRecordId() {
        return getRequiredFieldValue(WARC_Record_ID);
    }


    public static class Validator implements org.iokit.core.validate.Validator<WarcFieldSet> { // TODO: pull up to (abstract?) FieldSet.Validator base class

        @Override
        public void validate(WarcFieldSet fieldSet) throws ValidatorException {
            WarcType type = fieldSet.getRecordType();
            for (WarcDefinedField field : WarcDefinedField.values())
                if (field.isRequiredFor(type) && !fieldSet.getField(field.fieldName()).isPresent())
                    throw new FieldNotFoundException(field.fieldName());
        }
    }


    public static class Reader extends FieldSet.Reader {

        public Reader(LineReader lineReader) {
            this(new FoldedLine.Reader(lineReader.in));
        }

        public Reader(FoldedLine.Reader lineReader) {
            this(new WarcField.Reader(lineReader), new WarcFieldSet.Validator());
        }

        public Reader(WarcField.Reader fieldReader, WarcFieldSet.Validator fieldSetValidator) {
            super(fieldReader, fieldSetValidator);
        }

        @Override
        protected Supplier<FieldSet> newFieldSet() {
            return WarcFieldSet::new;
        }
    }
}
