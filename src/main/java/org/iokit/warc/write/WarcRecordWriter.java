package org.iokit.warc.write;

import org.iokit.warc.WarcRecord;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class WarcRecordWriter implements Closeable {

    private final OutputStream output;
    private final WarcRecordHeaderWriter headerWriter;
    private final WarcRecordBodyWriter bodyWriter;

    public WarcRecordWriter(OutputStream output) {
        this(output, new WarcRecordHeaderWriter(output), new WarcRecordBodyWriter(output));
    }

    public WarcRecordWriter(OutputStream output, WarcRecordHeaderWriter headerWriter, WarcRecordBodyWriter bodyWriter) {
        this.output = output;
        this.headerWriter = headerWriter;
        this.bodyWriter = bodyWriter;
    }


    public void write(WarcRecord record) throws IOException {
        headerWriter.write(record.getHeader());
        bodyWriter.write(record.getBody());
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
