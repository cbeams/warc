package org.iokit.warc;

import org.iokit.imf.Field;
import org.iokit.imf.FieldName;
import org.iokit.imf.FieldValue;

import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import java.util.Set;

public class WarcField extends Field {

    public static final String MIME_TYPE = "application/warc-fields";

    public WarcField(FieldName name, FieldValue value) {
        super(name, value);
    }


    public enum Type implements Field.Type {
        WARC_Type("WARC-Type"),
        WARC_Record_ID("WARC-Record-ID"),
        Content_Type("Content-Type"),
        Content_Length("Content-Length"),
        WARC_Date("WARC-Date");

        private final FieldName name;

        Type(String name) {
            this.name = new FieldName(name);
        }

        @Override
        public FieldName getName() {
            return name;
        }
    }


    public static class SetValidator extends Validator<Set<Field>> {

        @Override
        public void validate(Set<Field> fields) throws ValidatorException {
            // TODO
        }
    }
}
