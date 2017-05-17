package org.iokit.warc.read;

import org.iokit.warc.WarcVersion;
import org.iokit.warc.parse.WarcRecordVersionParser;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.parse.Parser;
import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class WarcVersionReader implements Reader<WarcVersion> {

    private final LineReader lineReader;
    private final Parser<WarcVersion> parser;

    public WarcVersionReader(LineReader lineReader) {
        this(lineReader, new WarcRecordVersionParser());
    }

    public WarcVersionReader(LineReader lineReader, Parser<WarcVersion> parser) {
        this.lineReader = lineReader;
        this.parser = parser;
    }

    public WarcVersion read() throws ReaderException, EOFException {
        try {
            return parser.parse(lineReader.read());
        } catch (ParsingException ex) {
            throw new ReaderException(ex);
        }
    }
}
