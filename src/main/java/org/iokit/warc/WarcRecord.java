package org.iokit.warc;

import org.iokit.imf.Message;

import org.iokit.line.LineInputStream;
import org.iokit.line.LineReader;
import org.iokit.line.LineTerminator;
import org.iokit.line.LineWriter;

import org.iokit.core.read.ParameterizedReader;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.function.BiFunction;

/**
 * Per http://bibnum.bnf.fr/WARC/ and
 * http://bibnum.bnf.fr/WARC/WARC_ISO_28500_version1_latestdraft.pdf.
 */
public class WarcRecord extends Message<WarcHeader, WarcBody> { // TODO: generate delegate methods for WarcHeader. Everything should be super-convenient and directly accessible from WarcRecord

    public static final LineTerminator DEFAULT_LINE_TERMINATOR = LineTerminator.CR_LF;

    public WarcRecord(WarcHeader header, WarcBody body) {
        super(header, body);
    }


    public static class Reader extends Message.Reader<WarcHeader, WarcBody, WarcRecord> {

        public Reader(InputStream in) {
            this(new LineInputStream(in, DEFAULT_LINE_TERMINATOR));
        }

        public Reader(LineInputStream in) {
            this(new LineReader(in));
        }

        public Reader(LineReader lineReader) {
            this(new WarcHeader.Reader(lineReader), new WarcBody.Reader(lineReader.in));
        }

        public Reader(org.iokit.core.read.Reader<WarcHeader> headerReader,
                      ParameterizedReader<WarcHeader, WarcBody> bodyReader) {
            this(headerReader, bodyReader, WarcRecord::new);
        }

        public Reader(org.iokit.core.read.Reader<WarcHeader> headerReader,
                      ParameterizedReader<WarcHeader, WarcBody> bodyReader,
                      BiFunction<WarcHeader, WarcBody, WarcRecord> recordFactory) {
            super(headerReader, bodyReader, recordFactory);
        }
    }


    public static class Writer extends Message.Writer<WarcHeader, WarcBody, WarcRecord> {

        public Writer(OutputStream out) {
            this(new LineWriter(out, DEFAULT_LINE_TERMINATOR));
        }

        public Writer(LineWriter lineWriter) {
            this(new WarcHeader.Writer(lineWriter), new WarcBody.Writer(lineWriter.out));
        }

        public Writer(org.iokit.core.write.Writer<WarcHeader> headerWriter,
                      org.iokit.core.write.Writer<WarcBody> bodyWriter) {
            super(headerWriter, bodyWriter);
        }
    }
}
