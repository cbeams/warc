package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;
import org.iokit.warc.WarcRecordBody;
import org.iokit.warc.WarcRecordHeader;

import org.iokit.imf.read.MessageReader;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;

import org.iokit.core.input.LineInputStream;

import java.io.EOFException;

public class WarcRecordReader implements MessageReader<WarcRecord> {

    private final WarcRecordHeaderReader headerReader;
    private final WarcRecordBodyReader bodyReader;

    public WarcRecordReader(LineInputStream in) {
        this(in, new LineReader(in));
    }

    public WarcRecordReader(LineInputStream in, LineReader lineReader) {
        this(new WarcRecordHeaderReader(lineReader), new WarcRecordBodyReader(in));
    }

    public WarcRecordReader(WarcRecordHeaderReader headerReader, WarcRecordBodyReader bodyReader) {
        this.headerReader = headerReader;
        this.bodyReader = bodyReader;
    }

    @Override
    public WarcRecord read() throws ReaderException, EOFException {
        WarcRecordHeader header = headerReader.read();
        WarcRecordBody body = bodyReader.read(header.getContentLength());
        return new WarcRecord(header, body);
    }
}
