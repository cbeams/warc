package org.iokit.warc.write;

import org.iokit.warc.WarcRecord;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

import org.iokit.core.output.mapping.MappableFileOutputStream;
import org.iokit.core.output.mapping.MappedOutputStream;

import org.iokit.lang.Try;

import java.io.File;
import java.io.OutputStream;

public class WarcWriter extends Writer<WarcRecord> {

    private final Writer<WarcRecord> recordWriter;
    private final Writer<Void> concatenatorWriter;

    public WarcWriter(File warcFile) {
        this(Try.toCall(() -> new MappableFileOutputStream(warcFile)));
    }

    public WarcWriter(MappableFileOutputStream out) {
        this(new MappedOutputStream(out));
    }

    public WarcWriter(OutputStream out, Class<? extends OutputStream> toType) {
        this(new MappedOutputStream(out, toType));
    }

    public WarcWriter(OutputStream out) {
        this(new LineWriter(out));
    }

    public WarcWriter(LineWriter lineWriter) {
        this(new WarcRecordWriter(lineWriter), new WarcConcatenatorWriter(lineWriter));
    }

    public WarcWriter(Writer<WarcRecord> recordWriter, Writer<Void> concatenatorWriter) {
        super(recordWriter.out);
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
        Try.toRun(out::close);
    }
}
