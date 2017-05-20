package org.iokit.imf;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.InvalidLengthException;
import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import static org.iokit.core.Ascii.*;
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
