package org.iokit.core.validate;

public abstract class Validator {

    public boolean isEnabled() {
        return true;
    }

    public abstract void validate(String input) throws ValidatorException;
}
