package io.webgraph.warc;

import org.iokit.gzip.MultiMemberGzipOutputStream;

import org.iokit.general.ConcatenationWriter;
import org.iokit.general.LineWriter;

import org.iokit.core.IOKitOutputStream;

import java.io.File;
import java.io.OutputStream;

public class WarcWriter extends ConcatenationWriter<WarcRecord> {

    // TODO: add and test String ctor
    // TODO: implement getByteCount up the stack

    public WarcWriter(File warcFile) {
        this(warcFile, new MultiMemberGzipOutputStream.Adapter());
    }

    public WarcWriter(File warcFile, IOKitOutputStream.Adapter... adapters) {
        this(IOKitOutputStream.adapt(warcFile, WarcRecord.LINE_TERMINATOR, adapters));
    }

    public WarcWriter(OutputStream out) {
        this(new IOKitOutputStream(out, WarcRecord.LINE_TERMINATOR));
    }

    public WarcWriter(IOKitOutputStream out) {
        this(new LineWriter(out));
    }

    public WarcWriter(LineWriter lineWriter) {
        this(new WarcRecord.Writer(lineWriter), new WarcConcatenator.Writer(lineWriter));
    }

    public WarcWriter(WarcRecord.Writer recordWriter, WarcConcatenator.Writer concatenatorWriter) {
        super(recordWriter, concatenatorWriter);
        // TODO: set minimum write count
    }
}
