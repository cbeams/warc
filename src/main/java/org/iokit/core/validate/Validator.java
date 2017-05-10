package org.iokit.core.validate;

public abstract class Validator {

    private boolean enabled = true;

    public abstract void validate(String input) throws ValidatorException;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
