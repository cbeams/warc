package org.iokit.warc;

import org.iokit.imf.FieldName;

public class WarcFieldName extends FieldName {

    public WarcFieldName(String value) {
        super(value);
    }


    public static class Parser extends FieldName.Parser {

        public Parser() {
            super(new WarcSpecials());
        }
    }
}
