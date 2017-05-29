package org.iokit.core.parse;

import org.iokit.core.validate.IOKitValidator;

public abstract class ValidatingParser<V> implements Parser<V> {

    private final IOKitValidator<String> validator;

    public ValidatingParser(IOKitValidator<String> validator) {
        this.validator = validator;
    }

    @Override
    public V parse(String input) {
        validator.validate(input);
        return parseValidated(input);
    }

    public abstract V parseValidated(String input);
}
