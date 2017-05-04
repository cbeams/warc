package org.iokit.warc.read;

import org.iokit.warc.WarcRecordHeader;

import org.iokit.imf.read.FieldSetReader;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;

import org.iokit.core.parse.ParsingException;

import java.io.EOFException;

public class WarcRecordHeaderReader implements Reader<WarcRecordHeader> {

    private final WarcRecordVersionReader versionReader;
    private final FieldSetReader fieldSetReader;

    public WarcRecordHeaderReader(LineReader lineReader) {
        this(new WarcRecordVersionReader(lineReader), new FieldSetReader(lineReader));
    }

    public WarcRecordHeaderReader(WarcRecordVersionReader versionReader, FieldSetReader fieldSetReader) {
        this.versionReader = versionReader;
        this.fieldSetReader = fieldSetReader;
    }

    public WarcRecordHeader read() throws EOFException, ParsingException {
        return new WarcRecordHeader(
            versionReader.read(),
            fieldSetReader.read());
    }
}
