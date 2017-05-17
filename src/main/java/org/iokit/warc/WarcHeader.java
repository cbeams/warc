package org.iokit.warc;

import org.iokit.imf.Field;
import org.iokit.imf.StartLineHeader;

import java.util.Set;

import static org.iokit.warc.WarcField.Type.*;

public class WarcHeader extends StartLineHeader<WarcVersion> {

    public WarcHeader(WarcVersion version, Set<Field> fields) {
        super(version, fields);
    }

    public WarcVersion getVersion() {
        return startLineValue;
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
        return Integer.valueOf(getField(Content_Length).getValue().toString()); // TODO: should be long
    }

    public String getRecordId() {
        return getField(WARC_Record_ID).getValue().toString();
    }
}
