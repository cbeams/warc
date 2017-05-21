package org.iokit.warc.write;

import org.iokit.warc.WarcRecord;

import org.iokit.core.write.Writer;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class WarcWriter extends Writer<WarcRecord> implements Closeable {

    private final OutputStream output;
    private final Writer<WarcRecord> recordWriter;
    private final Writer<Void> concatenatorWriter;

    public WarcWriter(OutputStream output) {
        this(output, new WarcRecordWriter(output), new WarcConcatenatorWriter(output));
    }

    public WarcWriter(OutputStream output, Writer<WarcRecord> recordWriter, Writer<Void> concatenatorWriter) {
        this.output = output;
        this.recordWriter = recordWriter;
        this.concatenatorWriter = concatenatorWriter;
    }

    @Override
    public void write(WarcRecord record) {
        recordWriter.write(record);
        concatenatorWriter.write(null);
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
