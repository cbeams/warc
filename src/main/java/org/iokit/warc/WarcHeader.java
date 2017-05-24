package org.iokit.warc;

import org.iokit.imf.FieldSet;
import org.iokit.imf.StartLineHeader;

import org.iokit.core.write.LineWriter;

import org.iokit.core.read.LineReader;

import static org.iokit.warc.WarcDefinedField.*;

public class WarcHeader extends StartLineHeader<WarcVersion> {

    public WarcHeader(WarcVersion version, FieldSet fieldSet) {
        super(version, fieldSet);
    }

    public WarcVersion getVersion() {
        return startLine;
    }

    public WarcRecord.Type getRecordType() {
        return getFieldValue(WARC_Type).map(WarcRecord.Type::of).get(); // TODO: decide what to do with unchecked gets
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


    public static class Reader extends StartLineHeader.Reader<WarcVersion, WarcHeader> {

        public Reader(LineReader lineReader) {
            this(new WarcVersion.Reader(lineReader), new WarcFieldSet.Reader(lineReader));
        }

        public Reader(org.iokit.core.read.Reader<WarcVersion> versionReader, FieldSet.Reader fieldSetReader) {
            super(versionReader, fieldSetReader, WarcHeader::new);
        }
    }


    public static class Writer extends StartLineHeader.Writer {

        public Writer(LineWriter lineWriter) {
            this(new WarcVersion.Writer(lineWriter), new FieldSet.Writer(lineWriter), lineWriter);
        }

        public Writer(org.iokit.core.write.Writer<WarcVersion> versionWriter,
                      FieldSet.Writer fieldSetWriter, LineWriter lineWriter) {
            super(versionWriter, fieldSetWriter, lineWriter);
        }
    }
}
