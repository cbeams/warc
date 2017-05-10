package org.iokit.core.parse;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.InvalidLengthException;
import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import org.iokit.core.token.SimpleToken;
import org.iokit.core.token.Token;

import static org.iokit.core.token.Ascii.*;
import static org.iokit.warc.Separator.isSeparatorChar;

public class SimpleTokenParser extends ValidatingParser<Token> {

    private static final int MIN_TOKEN_LENGTH = 1;

    public SimpleTokenParser(Validator validator) {
        super(validator);
    }

    public SimpleTokenParser() {
        this(new Validator() {
            @Override
            public void validate(String input) throws ValidatorException {
                if (!this.isEnabled())
                    return;

                if (input.length() < MIN_TOKEN_LENGTH)
                    throw new InvalidLengthException(input, MIN_TOKEN_LENGTH);

                if (input.chars().anyMatch(c -> !isAsciiChar(c) || isAsciiControlChar(c) || isSeparatorChar(c)))
                    throw new InvalidCharacterException(input);
           }
        });
    }

    public Token parseValidated(String input) throws ParsingException {
        return new SimpleToken(input);
    }
}
