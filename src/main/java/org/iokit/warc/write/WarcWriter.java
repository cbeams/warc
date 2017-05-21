package org.iokit.warc.write;

import org.iokit.warc.WarcRecord;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class WarcWriter extends Writer<WarcRecord> {

    private final Writer<WarcRecord> recordWriter;
    private final Writer<Void> concatenatorWriter;

    public WarcWriter(OutputStream out) {
        this(new LineWriter(out));
    }

    public WarcWriter(LineWriter lineWriter) {
        this(new WarcRecordWriter(lineWriter), new WarcConcatenatorWriter(lineWriter));
    }

    public WarcWriter(Writer<WarcRecord> recordWriter, Writer<Void> concatenatorWriter) {
        super(recordWriter.getOutput());
        this.recordWriter = recordWriter;
        this.concatenatorWriter = concatenatorWriter;
    }

    @Override
    public void write(WarcRecord record) {
        recordWriter.write(record);
        concatenatorWriter.write(null);
    }

    @Override
    public void close() {
        Try.toRun(output::close);
    }
}
