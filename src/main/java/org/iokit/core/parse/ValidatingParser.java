package org.iokit.core.parse;

import org.iokit.core.validate.Validator;

public abstract class ValidatingParser<V> implements Parser<V> {

    private final Validator<String> validator;

    public ValidatingParser(Validator<String> validator) {
        this.validator = validator;
    }

    @Override
    public V parse(String input) {
        validator.validate(input);
        return parseValidated(input);
    }

    public abstract V parseValidated(String input);
}
