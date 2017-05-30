package org.iokit.warc;

import org.iokit.message.Body;
import org.iokit.message.Header;
import org.iokit.message.Message;

import org.iokit.general.LineReader;
import org.iokit.general.LineWriter;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitOutputStream;
import org.iokit.core.LineTerminator;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.EnumSet;
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
            this(new IOKitInputStream(in, EnumSet.of(DEFAULT_LINE_TERMINATOR)));
        }

        public Reader(IOKitInputStream in) {
            this(new LineReader(in));
        }

        public Reader(LineReader lineReader) {
            this(new WarcHeader.Reader(lineReader), new WarcBody.Reader(lineReader.in));
        }

        public Reader(Header.Reader<WarcHeader> headerReader,
                      Body.Reader<WarcHeader, WarcBody> bodyReader) {
            this(headerReader, bodyReader, WarcRecord::new);
        }

        public Reader(Header.Reader<WarcHeader> headerReader,
                      Body.Reader<WarcHeader, WarcBody> bodyReader,
                      BiFunction<WarcHeader, WarcBody, WarcRecord> recordFactory) {
            super(headerReader, bodyReader, recordFactory);
        }
    }


    public static class Writer extends Message.Writer<WarcHeader, WarcBody, WarcRecord> {

        public Writer(OutputStream out) {
            this(new IOKitOutputStream(out, DEFAULT_LINE_TERMINATOR));
        }

        public Writer(IOKitOutputStream out) {
            this(new LineWriter(out));
        }

        public Writer(LineWriter lineWriter) {
            this(new WarcHeader.Writer(lineWriter), new WarcBody.Writer(lineWriter.out));
        }

        public Writer(Header.Writer<WarcHeader> headerWriter,
                      Body.Writer<WarcBody> bodyWriter) {
            super(headerWriter, bodyWriter);
        }
    }
}
