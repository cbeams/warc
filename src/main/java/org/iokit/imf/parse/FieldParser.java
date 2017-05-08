package org.iokit.imf.parse;

import org.iokit.imf.Field;

import org.iokit.core.parse.NullSafeParser;
import org.iokit.core.parse.Parser;
import org.iokit.core.parse.ParsingException;

public class FieldParser extends NullSafeParser<Field> {

    private final Parser<Field.Name> nameParser;
    private final Parser<Field.Value> valueParser;

    public FieldParser() {
        this(new FieldNameParser(), new FieldValueParser());
    }

    public FieldParser(Parser<Field.Name> nameParser, Parser<Field.Value> valueParser) {
        this.nameParser = nameParser;
        this.valueParser = valueParser;
    }

    public Field parseNullSafe(String input) throws ParsingException {
        int separatorIndex = input.indexOf(Field.SEPARATOR);
        if (separatorIndex == -1)
            throw new MissingSeparatorException(input);

        return new Field(
            nameParser.parse(input.substring(0, separatorIndex)),
            valueParser.parse(input.substring(separatorIndex + 1, input.length())));
    }


    public static class MissingSeparatorException extends ParsingException {

        public MissingSeparatorException(String input) {
            super("%s input must contain '%c' separator. Input was [%s]",
                Field.class.getSimpleName(), Field.SEPARATOR, input);
        }
    }
}
