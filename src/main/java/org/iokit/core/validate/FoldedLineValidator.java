package org.iokit.core.validate;

import static org.iokit.core.token.Ascii.isAsciiControlChar;

public class FoldedLineValidator extends Validator<String> {

    @Override
    public void validate(String input) throws ValidatorException {
        if (!this.isEnabled())
            return;

        char[] chars = input.toCharArray();
        for (int index = 0, increment = 1; index < chars.length; index += increment, increment = 1) {
            char c = chars[index];

            if (c == '\t')
                continue;

            if (c == '\r'
                && chars.length > index + 2
                && chars[index + 1] == '\n'
                && (chars[index + 2] == ' ' || chars[index + 2] == '\t')) {

                increment = 3;
                continue;
            }

            if (isAsciiControlChar(c))
                throw new InvalidCharacterException(input, c, index);
        }
    }
}
