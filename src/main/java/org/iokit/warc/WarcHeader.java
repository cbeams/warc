package org.iokit.warc;

import org.iokit.imf.FieldSet;
import org.iokit.imf.StartLineHeader;

import org.iokit.line.LineReader;
import org.iokit.line.LineWriter;

import static org.iokit.warc.WarcDefinedField.*;

public class WarcHeader extends StartLineHeader<WarcVersion> {

    public WarcHeader(WarcVersion version, FieldSet fieldSet) {
        super(version, fieldSet);
    }

    public WarcVersion getVersion() {
        return startLine;
    }

    public WarcRecord.Type getRecordType() {
        return getRequiredFieldValue(WARC_Type, WarcRecord.Type::unknownSafeValueOf);
    }

    public String getDate() {
        return getRequiredFieldValue(WARC_Date);
    }

    public String getContentType() {
        return getRequiredFieldValue(Content_Type);
    }

    public int getContentLength() {
        return getRequiredFieldValue(Content_Length, Integer::valueOf);
    }

    public String getRecordId() {
        return getRequiredFieldValue(WARC_Record_ID);
    }


    public static class Reader extends StartLineHeader.Reader<WarcVersion, WarcHeader> {

        public Reader(LineReader lineReader) {
            this(new WarcVersion.Reader(lineReader), new WarcFieldSet.Reader(lineReader));
        }

        public Reader(org.iokit.core.read.Reader<WarcVersion> versionReader, FieldSet.Reader fieldSetReader) {
            super(versionReader, fieldSetReader, WarcHeader::new);
        }
    }


    public static class Writer extends StartLineHeader.Writer<WarcVersion, WarcHeader> {

        public Writer(LineWriter lineWriter) {
            this(new WarcVersion.Writer(lineWriter), new FieldSet.Writer(lineWriter), lineWriter);
        }

        public Writer(org.iokit.core.write.Writer<WarcVersion> versionWriter, FieldSet.Writer fieldSetWriter,
                      LineWriter lineWriter) { // TODO: eliminate need for passing along lineWriter here. Instead, create WarcFieldSet.Writer than always writes a trailing newline per the spec
            super(versionWriter, fieldSetWriter, lineWriter);
        }
    }
}
