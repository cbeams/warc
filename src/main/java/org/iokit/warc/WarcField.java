package org.iokit.warc;

import org.iokit.message.Field;
import org.iokit.message.FieldName;
import org.iokit.message.FieldValue;

import org.iokit.line.LineReader;

public class WarcField extends Field {

    public static final String MIME_TYPE = "application/warc-fields";

    public WarcField(FieldName name, FieldValue value) {
        super(name, value);
    }


    public static class Reader extends Field.Reader {

        public Reader(LineReader lineReader) {
            this(lineReader, new WarcField.Parser());
        }

        public Reader(LineReader lineReader, WarcField.Parser fieldParser) {
            super(lineReader, fieldParser);
        }
    }


    public static class Parser extends Field.Parser {

        public Parser() {
            super(new WarcFieldName.Parser(), new FieldValue.Parser());
        }
    }
}
