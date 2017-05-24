package org.iokit.warc;

import org.iokit.imf.FieldSet;
import org.iokit.imf.StartLineHeader;

import org.iokit.core.write.LineWriter;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;

import static org.iokit.warc.WarcDefinedField.*;

public class WarcHeader extends StartLineHeader<WarcVersion> {

    public WarcHeader(WarcVersion version, FieldSet fieldSet) {
        super(version, fieldSet);
    }

    public WarcVersion getVersion() {
        return startLineValue;
    }

    public WarcRecord.Type getRecordType() {
        return getFieldValue(WARC_Type).map(WarcRecord.Type::of).get();
    }

    public String getDate() {
        return getFieldValue(WARC_Date).get();
    }

    public String getContentType() {
        return getFieldValue(Content_Type).get();
    }

    public int getContentLength() {
        return getFieldValue(Content_Length).map(Integer::valueOf).get();
    }

    public String getRecordId() {
        return getFieldValue(WARC_Record_ID).get();
    }


    public static class Reader extends org.iokit.core.read.Reader<WarcHeader> {

        private final org.iokit.core.read.Reader<WarcVersion> versionReader;
        private final FieldSet.Reader fieldSetReader;

        public Reader(LineReader lineReader) {
            this(new WarcVersion.Reader(lineReader), new WarcFieldSet.Reader(lineReader));
        }

        public Reader(org.iokit.core.read.Reader<WarcVersion> versionReader, FieldSet.Reader fieldSetReader) {
            super(versionReader.in);
            this.versionReader = versionReader;
            this.fieldSetReader = fieldSetReader;
        }

        @Override
        public WarcHeader read() throws ReaderException {
            return new WarcHeader(
                versionReader.read(),
                fieldSetReader.read());
        }
    }


    public static class Writer extends org.iokit.core.write.Writer<WarcHeader> {

        private final org.iokit.core.write.Writer<WarcVersion> versionWriter;
        private final FieldSet.Writer fieldSetWriter;
        private final LineWriter lineWriter;

        public Writer(LineWriter lineWriter) {
            this(new WarcVersion.Writer(lineWriter), new FieldSet.Writer(lineWriter), lineWriter);
        }

        public Writer(org.iokit.core.write.Writer<WarcVersion> versionWriter,
                      FieldSet.Writer fieldSetWriter, LineWriter lineWriter) {
            super(versionWriter.out);
            this.versionWriter = versionWriter;
            this.fieldSetWriter = fieldSetWriter;
            this.lineWriter = lineWriter;
        }

        @Override
        public void write(WarcHeader header) {
            versionWriter.write(header.getVersion());
            fieldSetWriter.write(header.getFieldSet());
            lineWriter.write();
        }
    }
}
