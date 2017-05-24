package org.iokit.warc;

import org.iokit.imf.read.FieldReader;

import org.iokit.imf.Field;
import org.iokit.imf.FieldName;
import org.iokit.imf.FieldValue;

import org.iokit.core.read.LineReader;

public class WarcField extends Field {

    public static final String MIME_TYPE = "application/warc-fields";

    public WarcField(FieldName name, FieldValue value) {
        super(name, value);
    }


    public static class Reader extends FieldReader {

        public Reader(LineReader lineReader) {
            this(lineReader, new WarcField.Parser());
        }

        public Reader(LineReader lineReader, org.iokit.core.parse.Parser<Field> fieldParser) {
            super(lineReader, fieldParser);
        }
    }


    public static class Parser extends Field.Parser {

        public Parser() {
            super(new WarcFieldName.Parser(), new FieldValue.Parser());
        }
    }
}
