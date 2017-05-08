package org.iokit.core.parse;

import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

public abstract class ValidatingParser<T> extends NullSafeParser<T> {

    private final Validator validator;

    public ValidatingParser(Validator validator) {
        this.validator = validator;
    }

    @Override
    public T parseNullSafe(String input) throws ParsingException {
        if (validator.isEnabled()) {
            try {
                validator.validate(input);
            } catch (ValidatorException ex) {
                throw new ParsingException(ex);
            }
        }

        return parseValidated(input);
    }

    public abstract T parseValidated(String input) throws ParsingException;
}
