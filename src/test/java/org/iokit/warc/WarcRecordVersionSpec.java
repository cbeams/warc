package org.iokit.warc;

import org.iokit.warc.parse.WarcRecordVersionParser;

import org.iokit.core.parse.ParsingException;

import io.beams.valjo.ValjoSpec;

public class WarcRecordVersionSpec extends ValjoSpec {

    public WarcRecordVersionSpec() {
        super(
            "WARC/1.0",
            "WARC/1.1"
        );
    }

    @Override
    protected Object parse(String input) throws ParsingException {
        return new WarcRecordVersionParser().parse(input);
    }
}
