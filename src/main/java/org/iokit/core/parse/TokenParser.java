package org.iokit.core.parse;

import org.iokit.core.validate.TokenValidator;
import org.iokit.core.validate.Validator;

import org.iokit.core.token.Token;

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
