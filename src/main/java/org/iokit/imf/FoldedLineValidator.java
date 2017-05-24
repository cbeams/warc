package org.iokit.imf;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import static org.iokit.core.Ascii.*;
import static org.iokit.line.LineTerminator.CR_LF;

public class FoldedLineValidator implements Validator<String> {

    @Override
    public void validate(String input) throws ValidatorException {

        char[] chars = input.toCharArray();
        for (int index = 0, increment = 1; index < chars.length; index += increment, increment = 1) {
            char c = chars[index];

            if (c == TAB)
                continue;

            if (c == CR_LF.bytes[0]
                && chars.length > index + CR_LF.bytes.length
                && chars[index + 1] == CR_LF.bytes[1]
                && (chars[index + 2] == SPACE || chars[index + 2] == TAB)) {

                increment = 3;
                continue;
            }

            if (isAsciiControlChar(c))
                throw new InvalidCharacterException(input, c, index);
        }
    }
}
