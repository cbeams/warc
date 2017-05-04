package org.iokit.warc;

import org.iokit.imf.Field;

public enum DefinedField {
    WARC_Type("WARC-Type"),
    WARC_Record_ID("WARC-WarcRecord-ID"),
    WARC_Date("WARC-Date"),
    Content_Type("Content-Type"),
    Content_Length("Content-Length");

    private final Field.Name name;

    DefinedField(String name) {
        this.name = new Field.Name(name);
    }

    public Field.Name getName() {
        return name;
    }
}
