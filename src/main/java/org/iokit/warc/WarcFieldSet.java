package org.iokit.warc;

import org.iokit.imf.FieldSet;

import org.iokit.core.validate.ValidatorException;

public class WarcFieldSet extends FieldSet {

    public static class Validator extends org.iokit.core.validate.Validator<FieldSet> {

        @Override
        public void validate(FieldSet fieldSet) throws ValidatorException {
            // TODO: actually validate required fields, etc
        }
    }
}
