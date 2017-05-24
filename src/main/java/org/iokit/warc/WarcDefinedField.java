package org.iokit.warc;

import org.iokit.imf.DefinedField;
import org.iokit.imf.FieldName;

public enum WarcDefinedField implements DefinedField { // TODO: support field-type specific validation
    WARC_Type("WARC-Type"),
    WARC_Record_ID("WARC-Record-ID"),
    Content_Type("Content-Type"),
    Content_Length("Content-Length"),
    WARC_Date("WARC-Date");

    private final FieldName fieldName;

    WarcDefinedField(String fieldName) {
        this.fieldName = new FieldName(fieldName);
    }

    @Override
    public FieldName fieldName() {
        return fieldName;
    }
}
