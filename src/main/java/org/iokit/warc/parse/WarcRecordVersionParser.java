package org.iokit.warc.parse;

import org.iokit.warc.WarcRecordVersion;

import org.iokit.core.validate.Validator;

import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.ValidatingParser;

public class WarcRecordVersionParser extends ValidatingParser<WarcRecordVersion> {

    public WarcRecordVersionParser() {
        this(new WarcRecordVersionValidator());
    }

    public WarcRecordVersionParser(Validator validator) {
        super(validator);
    }

    @Override
    public WarcRecordVersion parseValidated(String input) throws ParsingException {
        return new WarcRecordVersion(input);
    }
}
