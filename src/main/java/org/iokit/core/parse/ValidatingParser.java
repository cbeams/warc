package org.iokit.core.parse;

import org.iokit.core.validate.Validator;

public abstract class ValidatingParser<T> extends NullSafeParser<T> {

    private final Validator validator;

    public ValidatingParser(Validator validator) {
        this.validator = validator;
    }

    @Override
    public T parseNullSafe(String input) throws ParsingException {
        if (validator.isEnabled())
            validator.validate(input);

        return parseValidated(input);
    }

    public abstract T parseValidated(String input) throws ParsingException;
}
