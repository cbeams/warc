package org.iokit.warc.parse;

import org.iokit.warc.WarcVersion;

import org.iokit.core.validate.Validator;

import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.ValidatingParser;

public class WarcRecordVersionParser extends ValidatingParser<WarcVersion> {

    public WarcRecordVersionParser() {
        this(new WarcRecordVersionValidator());
    }

    public WarcRecordVersionParser(Validator validator) {
        super(validator);
    }

    @Override
    public WarcVersion parseValidated(String input) throws ParsingException {
        return new WarcVersion(input);
    }
}
