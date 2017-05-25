package org.iokit.warc;

import org.iokit.imf.FieldSet;
import org.iokit.imf.StartLineHeader;

import org.iokit.line.LineReader;
import org.iokit.line.LineWriter;

public class WarcHeader extends StartLineHeader<WarcVersion, WarcFieldSet> {

    public WarcHeader(WarcVersion version, WarcFieldSet fieldSet) {
        super(version, fieldSet);
    }

    public WarcVersion getVersion() {
        return startLine;
    }

    public WarcRecord.Type getRecordType() {
        return fieldSet.getRecordType();
    }

    public String getDate() {
        return fieldSet.getDate();
    }

    public String getContentType() {
        return fieldSet.getContentType();
    }

    public int getContentLength() {
        return fieldSet.getContentLength();
    }

    public String getRecordId() {
        return fieldSet.getRecordId();
    }


    public static class Reader extends StartLineHeader.Reader<WarcVersion, WarcFieldSet, WarcHeader> {

        public Reader(LineReader lineReader) {
            this(new WarcVersion.Reader(lineReader), new WarcFieldSet.Reader(lineReader));
        }

        public Reader(org.iokit.core.read.Reader<WarcVersion> versionReader, WarcFieldSet.Reader fieldSetReader) {
            super(versionReader, fieldSetReader, WarcHeader::new);
        }
    }


    public static class Writer extends StartLineHeader.Writer<WarcVersion, WarcFieldSet, WarcHeader> {

        public Writer(LineWriter lineWriter) {
            this(new WarcVersion.Writer(lineWriter), new FieldSet.Writer(lineWriter), lineWriter);
        }

        public Writer(org.iokit.core.write.Writer<WarcVersion> versionWriter, FieldSet.Writer fieldSetWriter,
                      LineWriter lineWriter) { // TODO: eliminate need for passing along lineWriter here. Instead, create WarcFieldSet.Writer than always writes a trailing newline per the spec
            super(versionWriter, fieldSetWriter, lineWriter);
        }
    }
}
