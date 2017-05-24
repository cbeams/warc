package org.iokit.warc;

import org.iokit.imf.MessageReader;

import org.iokit.imf.Message;
import org.iokit.imf.MessageWriter;

import org.iokit.core.write.LineWriter;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ParameterizedReader;

import org.iokit.core.input.LineInputStream;

import org.iokit.core.LineTerminator;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.function.BiFunction;

/**
 * Per http://bibnum.bnf.fr/WARC/ and
 * http://bibnum.bnf.fr/WARC/WARC_ISO_28500_version1_latestdraft.pdf.
 */
public class WarcRecord extends Message<WarcHeader, WarcBody> {

    public static final LineTerminator DEFAULT_LINE_TERMINATOR = LineTerminator.CR_LF;

    public WarcRecord(WarcHeader header, WarcBody body) {
        super(header, body);
    }


    public enum Type { // TODO: add other warc record types

        /**
         * A 'warcinfo' record describes the records that follow it, up through end of file,
         * end of input, or until next 'warcinfo' record. Typically, this appears once and at
         * the beginning of a WARC file. For a web archive, it often contains information
         * about the web crawl which generated the following records.
         * <p>
         * The format of this descriptive record block may vary, though the use of the
         * "application/warc-fields" content-type is recommended. Allowable fields include,
         * but are not limited to, all <a href="http://dublincore.org/documents/dcmi-terms/">
         * DCMI Metadata Terms</a> plus the following field definitions. All fields are
         * optional.
         */
        warcinfo,
        metadata,
        unknown;

        public static Type of(String name) {
            try {
                return Enum.valueOf(Type.class, name);
            } catch (IllegalArgumentException ex) {
                return WarcRecord.Type.unknown;
            }
        }
    }


    public static class Reader extends MessageReader<WarcHeader, WarcBody, WarcRecord> {

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


    public static class Writer extends MessageWriter<WarcHeader, WarcBody, WarcRecord> {

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
