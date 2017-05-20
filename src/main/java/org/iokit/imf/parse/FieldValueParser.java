package org.iokit.imf.parse;

import org.iokit.imf.Field;

import org.iokit.core.validate.InvalidCharacterException;
import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import org.iokit.core.parse.ParsingException;
import org.iokit.core.parse.ValidatingParser;

import static org.iokit.core.token.Ascii.isAsciiControlChar;

public class FieldValueParser extends ValidatingParser<Field.Value> {

    public FieldValueParser(Validator<String> validator) {
        super(validator);
    }

    public FieldValueParser() {
        this(new Validator<String>() {
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
        });
    }

    public Field.Value parseValidated(String input) throws ParsingException {
        return new Field.Value(input.trim());
    }
}
