package org.iokit.warc;

import org.iokit.general.ConcatenationReader;
import org.iokit.general.ConcatenationWriter;
import org.iokit.general.LineReader;
import org.iokit.general.LineWriter;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitOutputStream;
import org.iokit.core.Try;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Warc {

    public static final String MIME_TYPE = "application/warc";


    public static class Reader extends ConcatenationReader<WarcRecord> {

        public static final int DEFAULT_MINIMUM_READ_COUNT = 1;

        public Reader(String warcFilePath) {
            this(new File(warcFilePath));
        }

        public Reader(File warcFile) {
            this(Try.toCall(() -> new FileInputStream(warcFile)));
        }

        public Reader(InputStream in) {
            this(new IOKitInputStream(IOKitInputStream.Adapter.adaptFrom(in), WarcRecord.DEFAULT_LINE_TERMINATOR));
        }

        public Reader(IOKitInputStream in) {
            this(new LineReader(in));
        }

        public Reader(LineReader lineReader) {
            this(new WarcRecord.Reader(lineReader), new WarcConcatenator.Reader(lineReader));
        }

        public Reader(WarcRecord.Reader recordReader, WarcConcatenator.Reader concatenatorReader) {
            super(recordReader, concatenatorReader);
            setMinimumReadCount(DEFAULT_MINIMUM_READ_COUNT);
        }
    }


    public static class Writer extends ConcatenationWriter<WarcRecord> {

        // TODO: add and test String ctor
        // TODO: implement getByteCount up the stack

        public Writer(File warcFile) {
            this(Try.toCall(() -> new IOKitOutputStream.AdaptableFileOutputStream(warcFile)));
        }

        public Writer(IOKitOutputStream.AdaptableFileOutputStream out) {
            this(IOKitOutputStream.Adapter.adaptFrom(out));
        }

        public Writer(OutputStream out) {
            this(new IOKitOutputStream(out, WarcRecord.DEFAULT_LINE_TERMINATOR));
        }

        public Writer(IOKitOutputStream out) {
            this(new LineWriter(out));
        }

        public Writer(LineWriter lineWriter) {
            this(new WarcRecord.Writer(lineWriter), new WarcConcatenator.Writer(lineWriter));
        }

        public Writer(WarcRecord.Writer recordWriter, WarcConcatenator.Writer concatenatorWriter) {
            super(recordWriter, concatenatorWriter);
            // TODO: set minimum write count
        }
    }
}
