package org.iokit.warc;

import org.iokit.imf.StartLineHeader;

import org.iokit.line.LineReader;
import org.iokit.line.LineWriter;

import java.util.Optional;

public class WarcHeader extends StartLineHeader<WarcVersion, WarcFieldSet> {

    public WarcHeader(WarcVersion version, WarcFieldSet fieldSet) {
        super(version, fieldSet);
    }

    public WarcVersion getVersion() {
        return startLine;
    }

    public WarcType getType() {
        return fieldSet.getType();
    }

    public String getRecordId() {
        return fieldSet.getRecordId();
    }

    public String getDate() {
        return fieldSet.getDate();
    }

    public int getContentLength() {
        return fieldSet.getContentLength();
    }

    public Optional<String> getContentType() {
        return fieldSet.getContentType();
    }

    public Optional<String> getConcurrentTo() {
        return fieldSet.getConcurrentTo();
    }

    public Optional<String> getBlockDigest() {
        return fieldSet.getBlockDigest();
    }

    public Optional<String> getPayloadDigest() {
        return fieldSet.getPayloadDigest();
    }

    public Optional<String> getIpAddress() {
        return fieldSet.getIpAddress();
    }

    public Optional<String> getRefersTo() {
        return fieldSet.getRefersTo();
    }

    public Optional<String> getTargetUri() {
        return fieldSet.getTargetUri();
    }

    public Optional<String> getTruncated() {
        return fieldSet.getTruncated();
    }

    public Optional<String> getWarcinfoID() {
        return fieldSet.getWarcinfoID();
    }

    public Optional<String> getFilename() {
        return fieldSet.getFilename();
    }

    public Optional<String> getProfile() {
        return fieldSet.getProfile();
    }

    public Optional<String> getIdentifiedPayloadType() {
        return fieldSet.getIdentifiedPayloadType();
    }

    public Optional<String> getSegmentOriginID() {
        return fieldSet.getSegmentOriginID();
    }

    public Optional<String> getSegmentNumber() {
        return fieldSet.getSegmentNumber();
    }

    public Optional<String> getSegmentTotalLength() {
        return fieldSet.getSegmentTotalLength();
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
            this(new WarcVersion.Writer(lineWriter), new WarcFieldSet.Writer(lineWriter));
        }

        public Writer(org.iokit.core.write.Writer<WarcVersion> versionWriter, WarcFieldSet.Writer fieldSetWriter) {
            super(versionWriter, fieldSetWriter);
        }
    }
}
