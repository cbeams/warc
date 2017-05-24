package org.iokit.imf;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.InvalidLengthException;
import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import static org.iokit.core.Ascii.*;

public class TokenValidator implements Validator<String> { // TODO: make nested class of Token?

    public static final int MIN_TOKEN_LENGTH = 1;

    private final Specials specials;

    public TokenValidator(Specials specials) {
        this.specials = specials;
    }

    @Override
    public void validate(String input) throws ValidatorException {
        if (input.length() < MIN_TOKEN_LENGTH)
            throw new InvalidLengthException(input, MIN_TOKEN_LENGTH);

        if (input.chars().anyMatch(c -> !isAsciiChar(c) || isAsciiControlChar(c) || specials.isSpecial(c)))
            throw new InvalidCharacterException(input);
    }
}
