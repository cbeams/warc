package org.iokit.warc.read;

import org.iokit.warc.WarcRecordVersion;
import org.iokit.warc.parse.WarcRecordVersionParser;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.parse.Parser;
import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class WarcRecordVersionReader implements Reader<WarcRecordVersion> {

    private final LineReader lineReader;
    private final Parser<WarcRecordVersion> parser;

    public WarcRecordVersionReader(LineReader lineReader) {
        this(lineReader, new WarcRecordVersionParser());
    }

    public WarcRecordVersionReader(LineReader lineReader, Parser<WarcRecordVersion> parser) {
        this.lineReader = lineReader;
        this.parser = parser;
    }

    public WarcRecordVersion read() throws ReaderException, EOFException {
        try {
            return parser.parse(lineReader.read());
        } catch (ParsingException ex) {
            throw new ReaderException(ex);
        }
    }
}
