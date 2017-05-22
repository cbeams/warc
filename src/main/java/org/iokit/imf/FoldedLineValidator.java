package org.iokit.imf;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import static org.iokit.core.Ascii.isAsciiControlChar;

public class FoldedLineValidator extends Validator<String> {

    @Override
    public void validate(String input) throws ValidatorException {
        if (!this.isEnabled())
            return;

        char[] chars = input.toCharArray();
        for (int index = 0, increment = 1; index < chars.length; index += increment, increment = 1) {
            char c = chars[index];

            if (c == '\t') // TODO: extract to Ascii class, e.g. Ascii.TAB
                continue;

            if (c == '\r'  // TODO: extract to Ascii class, e.g. Ascii.CR and refactor LineTerminator to use it too
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
