package org.iokit.warc.read;

import org.iokit.warc.WarcHeader;
import org.iokit.warc.WarcVersion;

import org.iokit.imf.read.FieldSetReader;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;

public class WarcHeaderReader extends Reader<WarcHeader> {

    private final Reader<WarcVersion> versionReader;
    private final FieldSetReader fieldSetReader;

    public WarcHeaderReader(LineReader lineReader) {
        this(new WarcVersionReader(lineReader), new WarcFieldSetReader(lineReader));
    }

    public WarcHeaderReader(Reader<WarcVersion> versionReader, FieldSetReader fieldSetReader) {
        super(versionReader.getInput());
        this.versionReader = versionReader;
        this.fieldSetReader = fieldSetReader;
    }

    @Override
    public WarcHeader read() throws ReaderException {
        return new WarcHeader(
            versionReader.read(),
            fieldSetReader.read());
    }
}
