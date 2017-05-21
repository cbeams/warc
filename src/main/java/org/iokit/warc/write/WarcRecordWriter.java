package org.iokit.warc.write;

import org.iokit.warc.WarcBody;
import org.iokit.warc.WarcHeader;
import org.iokit.warc.WarcRecord;

import org.iokit.core.write.Writer;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class WarcRecordWriter extends Writer<WarcRecord> implements Closeable {

    private final OutputStream output;
    private final Writer<WarcHeader> headerWriter;
    private final Writer<WarcBody> bodyWriter;

    public WarcRecordWriter(OutputStream output) {
        this(output, new WarcHeaderWriter(output), new WarcBodyWriter(output));
    }

    public WarcRecordWriter(OutputStream output, Writer<WarcHeader> headerWriter, Writer<WarcBody> bodyWriter) {
        this.output = output;
        this.headerWriter = headerWriter;
        this.bodyWriter = bodyWriter;
    }

    @Override
    public void write(WarcRecord record) {
        headerWriter.write(record.getHeader());
        bodyWriter.write(record.getBody());
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
