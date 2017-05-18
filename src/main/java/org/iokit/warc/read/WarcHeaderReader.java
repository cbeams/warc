package org.iokit.warc.read;

import org.iokit.warc.WarcHeader;

import org.iokit.imf.read.FieldSetReader;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.Reader;
import org.iokit.core.read.ReaderException;
import org.iokit.core.read.TransformReader;

public class WarcHeaderReader extends TransformReader<Reader, WarcHeader> {

    private final WarcVersionReader versionReader;
    private final FieldSetReader fieldSetReader;

    public WarcHeaderReader(LineReader lineReader) {
        this(new WarcVersionReader(lineReader), new FieldSetReader(lineReader));
    }

    public WarcHeaderReader(WarcVersionReader versionReader, FieldSetReader fieldSetReader) {
        super(versionReader);
        this.versionReader = versionReader;
        this.fieldSetReader = fieldSetReader;
    }

    public WarcHeader read() throws ReaderException {
        return new WarcHeader(
            versionReader.read(),
            fieldSetReader.read());
    }
}
