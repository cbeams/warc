package org.iokit.warc.write;

import org.iokit.warc.WarcBody;
import org.iokit.warc.WarcHeader;
import org.iokit.warc.WarcRecord;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

import java.io.OutputStream;

public class WarcRecordWriter extends Writer<WarcRecord> {

    private final Writer<WarcHeader> headerWriter;
    private final Writer<WarcBody> bodyWriter;

    public WarcRecordWriter(OutputStream output) {
        this(new LineWriter(output));
    }

    public WarcRecordWriter(LineWriter lineWriter) {
        this(new WarcHeaderWriter(lineWriter), new WarcBodyWriter(lineWriter.getOutput()));
    }

    public WarcRecordWriter(Writer<WarcHeader> headerWriter, Writer<WarcBody> bodyWriter) {
        super(headerWriter.getOutput());
        this.headerWriter = headerWriter;
        this.bodyWriter = bodyWriter;
    }

    @Override
    public void write(WarcRecord record) {
        headerWriter.write(record.getHeader());
        bodyWriter.write(record.getBody());
    }
}
