package org.iokit.core.parse;

import org.iokit.core.validate.Validator;

public abstract class ValidatingParser<T> implements Parser<T> {

    private final Validator<String> validator;

    public ValidatingParser(Validator<String> validator) {
        this.validator = validator;
    }

    @Override
    public T parse(String input) {
        validator.validate(input);
        return parseValidated(input);
    }

    public abstract T parseValidated(String input);
}
