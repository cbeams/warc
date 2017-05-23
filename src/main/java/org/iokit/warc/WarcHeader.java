package org.iokit.warc;

import org.iokit.imf.write.FieldSetWriter;

import org.iokit.imf.read.FieldSetReader;

import org.iokit.imf.FieldSet;
import org.iokit.imf.StartLineHeader;

import org.iokit.core.write.LineWriter;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.ReaderException;

import static org.iokit.warc.WarcField.Type.*;

public class WarcHeader extends StartLineHeader<WarcVersion> {

    public WarcHeader(WarcVersion version, FieldSet fieldSet) {
        super(version, fieldSet);
    }

    public WarcVersion getVersion() {
        return startLineValue;
    }

    public WarcRecord.Type getRecordType() {
        try {
            return WarcRecord.Type.valueOf(getField(WARC_Type).getValue().toString()); // TODO: add convenience getField accessor
        } catch (IllegalArgumentException ex) {
            return WarcRecord.Type.unknown;
        }
    }

    public String getDate() {
        return getField(WARC_Date).getValue().toString();
    }

    public String getContentType() {
        return getField(Content_Type).getValue().toString();
    }

    public int getContentLength() {
        return Integer.valueOf(getField(Content_Length).getValue().toString()); // TODO: should be long
    }

    public String getRecordId() {
        return getField(WARC_Record_ID).getValue().toString();
    }


    public static class Reader extends org.iokit.core.read.Reader<WarcHeader> {

        private final org.iokit.core.read.Reader<WarcVersion> versionReader;
        private final FieldSetReader fieldSetReader;

        public Reader(LineReader lineReader) {
            this(new WarcVersion.Reader(lineReader), new WarcFieldSet.Reader(lineReader));
        }

        public Reader(org.iokit.core.read.Reader<WarcVersion> versionReader, FieldSetReader fieldSetReader) {
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
        private final FieldSetWriter fieldSetWriter;
        private final LineWriter lineWriter;

        public Writer(LineWriter lineWriter) {
            this(new WarcVersion.Writer(lineWriter), new FieldSetWriter(lineWriter), lineWriter);
        }

        public Writer(org.iokit.core.write.Writer<WarcVersion> versionWriter,
                      FieldSetWriter fieldSetWriter, LineWriter lineWriter) {
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
