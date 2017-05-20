package org.iokit.core.validate;

import static org.iokit.core.token.Ascii.*;
import static org.iokit.warc.Separator.isSeparatorChar;

public class TokenValidator extends Validator<String> {

    public static final int MIN_TOKEN_LENGTH = 1;

    @Override
    public void validate(String input) throws ValidatorException {
        if (!this.isEnabled())
            return;

        if (input.length() < MIN_TOKEN_LENGTH)
            throw new InvalidLengthException(input, MIN_TOKEN_LENGTH);

        if (input.chars().anyMatch(c -> !isAsciiChar(c) || isAsciiControlChar(c) || isSeparatorChar(c)))
            throw new InvalidCharacterException(input);
    }
}
