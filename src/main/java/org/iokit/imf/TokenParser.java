package org.iokit.imf;

import org.iokit.core.validate.Validator;

import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.ValidatingParser;

public class TokenParser extends ValidatingParser<Token> {

    public TokenParser() {
        this(new TokenValidator());
    }

    public TokenParser(Validator<String> validator) {
        super(validator);
    }

    public Token parseValidated(String input) throws ParsingException {
        return new Token(input);
    }
}
