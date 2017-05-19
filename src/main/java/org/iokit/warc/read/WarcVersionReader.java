package org.iokit.warc.read;

import org.iokit.warc.WarcVersion;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.parse.Parser;

public class WarcVersionReader extends Reader<WarcVersion> {

    private final LineReader lineReader;
    private final Parser<WarcVersion> parser;

    public WarcVersionReader(LineReader lineReader) {
        this(lineReader, new WarcVersion.Parser());
    }

    public WarcVersionReader(LineReader lineReader, Parser parser) {
        super(lineReader.getInput());
        this.lineReader = lineReader;
        this.parser = parser;
    }

    public WarcVersion read() throws ReaderException {
        return parser.parse(lineReader.read());
    }
}
