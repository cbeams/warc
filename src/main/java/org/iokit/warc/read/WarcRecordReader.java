package org.iokit.warc.read;

import org.iokit.warc.WarcBody;
import org.iokit.warc.WarcHeader;
import org.iokit.warc.WarcRecord;

import org.iokit.imf.read.MessageReader;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ParameterizedReader;
import org.iokit.core.read.Reader;

import org.iokit.core.input.LineInputStream;

import org.iokit.core.LineTerminator;

import java.io.InputStream;

import java.util.function.BiFunction;

public class WarcRecordReader extends MessageReader<WarcHeader, WarcBody, WarcRecord> {

    public static final LineTerminator DEFAULT_LINE_TERMINATOR = LineTerminator.CR_LF;

    public WarcRecordReader(InputStream in) {
        this(new LineInputStream(in, DEFAULT_LINE_TERMINATOR));
    }

    public WarcRecordReader(LineInputStream in) {
        this(new LineReader(in));
    }

    public WarcRecordReader(LineReader lineReader) {
        this(new WarcHeaderReader(lineReader), new WarcBodyReader(lineReader.in));
    }

    public WarcRecordReader(Reader<WarcHeader> headerReader, ParameterizedReader<WarcHeader, WarcBody> bodyReader) {
        this(headerReader, bodyReader, WarcRecord::new);
    }

    public WarcRecordReader(Reader<WarcHeader> headerReader, ParameterizedReader<WarcHeader, WarcBody> bodyReader,
                            BiFunction<WarcHeader, WarcBody, WarcRecord> recordFactory) {
        super(headerReader, bodyReader, recordFactory);
    }
}
