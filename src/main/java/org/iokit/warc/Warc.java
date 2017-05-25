package org.iokit.warc;

import org.iokit.magic.MappableFileOutputStream;
import org.iokit.magic.MappedInputStream;
import org.iokit.magic.MappedOutputStream;

import org.iokit.line.LineInputStream;
import org.iokit.line.LineReader;
import org.iokit.line.LineWriter;

import org.iokit.core.write.ConcatenationWriter;

import org.iokit.core.read.ConcatenationReader;

import org.iokit.lang.Try;

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
            this(new MappedInputStream(in));
        }

        public Reader(MappedInputStream in) {
            this(new LineInputStream(in, WarcRecord.DEFAULT_LINE_TERMINATOR));
        }

        public Reader(LineInputStream in) {
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
            this(Try.toCall(() -> new MappableFileOutputStream(warcFile)));
        }

        public Writer(MappableFileOutputStream out) {
            this(new MappedOutputStream(out));
        }

        public Writer(OutputStream out, Class<? extends OutputStream> toType) { // TODO: test this ctor
            this(new MappedOutputStream(out, toType));
        }

        public Writer(OutputStream out) {
            this(new LineWriter(out, WarcRecord.DEFAULT_LINE_TERMINATOR));
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
