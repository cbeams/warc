package org.iokit.warc;

import org.iokit.imf.Field;

public class WarcField extends Field {

    public static final String MIME_TYPE = "application/warc-fields";

    public WarcField(Name name, Value value) {
        super(name, value);
    }
}
