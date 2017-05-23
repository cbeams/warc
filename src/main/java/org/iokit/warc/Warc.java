package org.iokit.warc;

import org.iokit.core.write.LineWriter;

import org.iokit.core.read.ConcatenationReader;
import org.iokit.core.read.LineReader;

import org.iokit.core.output.mapping.MappableFileOutputStream;
import org.iokit.core.output.mapping.MappedOutputStream;

import org.iokit.core.input.LineInputStream;
import org.iokit.core.input.mapping.MappedInputStream;

import org.iokit.lang.Try;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Warc {

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
            this(new LineInputStream(in, WarcRecord.Reader.DEFAULT_LINE_TERMINATOR));
        }

        public Reader(LineInputStream in) {
            this(new LineReader(in));
        }

        public Reader(LineReader lineReader) {
            this(new WarcRecord.Reader(lineReader), new WarcConcatenator.Reader(lineReader));
        }

        public Reader(org.iokit.core.read.Reader<WarcRecord> recordReader,
                      org.iokit.core.read.Reader<Boolean> concatenatorReader) {
            super(recordReader, concatenatorReader);
            setMinimumReadCount(DEFAULT_MINIMUM_READ_COUNT);
        }
    }


    public static class Writer extends org.iokit.core.write.Writer<WarcRecord> {

        private final org.iokit.core.write.Writer<WarcRecord> recordWriter;
        private final org.iokit.core.write.Writer<Void> concatenatorWriter;

        public Writer(File warcFile) {
            this(Try.toCall(() -> new MappableFileOutputStream(warcFile)));
        }

        public Writer(MappableFileOutputStream out) {
            this(new MappedOutputStream(out));
        }

        public Writer(OutputStream out, Class<? extends OutputStream> toType) { // TODO: test
            this(new MappedOutputStream(out, toType));
        }

        public Writer(OutputStream out) {
            this(new LineWriter(out, WarcRecord.Writer.DEFAULT_LINE_TERMINATOR));
        }

        public Writer(LineWriter lineWriter) {
            this(new WarcRecord.Writer(lineWriter), new WarcConcatenator.Writer(lineWriter));
        }

        public Writer(org.iokit.core.write.Writer<WarcRecord> recordWriter,
                      org.iokit.core.write.Writer<Void> concatenatorWriter) {
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
}
