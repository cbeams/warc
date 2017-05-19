package org.iokit.warc.read;

import org.iokit.warc.WarcVersion;
import org.iokit.warc.parse.WarcRecordVersionParser;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;
import org.iokit.core.read.TransformReader;

import org.iokit.core.parse.Parser;

public class WarcVersionReader extends TransformReader<LineReader, WarcVersion> {

    private final Parser<WarcVersion> parser;

    public WarcVersionReader(LineReader lineReader) {
        this(lineReader, new WarcRecordVersionParser());
    }

    public WarcVersionReader(LineReader reader, Parser<WarcVersion> parser) {
        super(reader);
        this.parser = parser;
    }

    public WarcVersion read() throws ReaderException {
        return parser.parse(reader.read());
    }
}
