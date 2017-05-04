package org.iokit.warc.read;

import org.iokit.warc.WarcRecordVersion;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class WarcRecordVersionReader implements Reader<WarcRecordVersion> {

    private final LineReader lineReader;

    public WarcRecordVersionReader(LineReader lineReader) {
        this.lineReader = lineReader;
    }

    public WarcRecordVersion read() throws EOFException, ParsingException {
        return WarcRecordVersion.parse(lineReader.read());
    }
}
