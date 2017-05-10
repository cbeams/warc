package org.iokit.warc.write;

import org.iokit.warc.WarcRecord;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class WarcWriter implements Closeable {

    private final OutputStream output;
    private final WarcRecordWriter recordWriter;
    private final WarcRecordSeparatorWriter recordSeparatorWriter;

    public WarcWriter(OutputStream output) {
        this(output, new WarcRecordWriter(output), new WarcRecordSeparatorWriter(output));
    }

    public WarcWriter(OutputStream output, WarcRecordWriter recordWriter, WarcRecordSeparatorWriter recordSeparatorWriter) {
        this.output = output;
        this.recordWriter = recordWriter;
        this.recordSeparatorWriter = recordSeparatorWriter;
    }

    public void write(WarcRecord record) throws IOException {
        recordWriter.write(record);
        recordSeparatorWriter.write();
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
