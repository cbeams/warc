package org.iokit.imf.parse;

import org.iokit.imf.Field;

import org.iokit.core.parse.NullSafeParser;
import org.iokit.core.parse.Parser;
import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.SimpleTokenParser;

import org.iokit.core.token.Token;

public class FieldNameParser extends NullSafeParser<Field.Name> {

    private final Parser<Token> tokenParser;

    public FieldNameParser() {
        this(new SimpleTokenParser());
    }

    public FieldNameParser(Parser<Token> tokenParser) {
        this.tokenParser = tokenParser;
    }

    public Field.Name parseNullSafe(String input) throws ParsingException {
        return new Field.Name(tokenParser.parse(input).getValue());
    }
}
