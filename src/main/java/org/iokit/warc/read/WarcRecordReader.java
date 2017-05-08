package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;
import org.iokit.warc.WarcRecordBody;
import org.iokit.warc.WarcRecordHeader;

import org.iokit.imf.read.MessageReader;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.InputStream;

public class WarcRecordReader implements AutoCloseable, MessageReader<WarcRecord> {

    private final WarcInputStream in;
    private final WarcRecordHeaderReader headerReader;
    private final WarcRecordBodyReader bodyReader;

    public WarcRecordReader(String warcRecord) {
        this(warcRecord.getBytes());
    }

    public WarcRecordReader(byte[] warcBytes) {
        this(new ByteArrayInputStream(warcBytes));
    }

    public WarcRecordReader(InputStream in) {
        this(new WarcInputStream(in));
    }

    public WarcRecordReader(WarcInputStream in) {
        this(in, new LineReader(in));
    }

    public WarcRecordReader(WarcInputStream in, LineReader lineReader) {
        this(in, new WarcRecordHeaderReader(lineReader), new WarcRecordBodyReader(in));
    }

    public WarcRecordReader(WarcInputStream in, WarcRecordHeaderReader headerReader, WarcRecordBodyReader bodyReader) {
        this.in = in;
        this.headerReader = headerReader;
        this.bodyReader = bodyReader;
    }

    @Override
    public WarcRecord read() throws ReaderException, EOFException {
        WarcRecordHeader header = headerReader.read();
        WarcRecordBody body = bodyReader.read(header.getContentLength());
        return new WarcRecord(header, body);
    }

    @Override
    public void close() throws Exception {
        in.close();
    }
}
