package org.iokit.warc;

import org.iokit.imf.DefinedField;

public enum WarcDefinedField implements DefinedField { // TODO: support field-type specific validation
    WARC_Type("WARC-Type"),
    WARC_Record_ID("WARC-Record-ID"),
    Content_Type("Content-Type"),
    Content_Length("Content-Length"),
    WARC_Date("WARC-Date");

    private final String fieldName;

    WarcDefinedField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
