package org.iokit.warc;

import org.iokit.imf.Field;
import org.iokit.imf.StartLineMessageHeader;

import java.util.Set;

import static org.iokit.warc.DefinedField.*;

public class WarcRecordHeader extends StartLineMessageHeader<WarcRecordVersion> {

    public WarcRecordHeader(WarcRecordVersion version, Set<Field> fields) {
        super(version, fields);
    }

    public WarcRecordVersion getVersion() {
        return startLineValue;
    }

    private Field getField(DefinedField field) {
        return getField(field.getName().toString());
    }

    public WarcRecord.Type getRecordType() {
        try {
            return WarcRecord.Type.valueOf(getField(WARC_Type).getValue().toString());
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
        return Integer.valueOf(getField(Content_Length).getValue().toString()); // TODO: could be long?
    }

    public String getRecordId() {
        return getField(WARC_Record_ID).getValue().toString();
    }

}
