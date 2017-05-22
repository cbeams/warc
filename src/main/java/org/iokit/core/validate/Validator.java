package org.iokit.core.validate;

public abstract class Validator<T> {

    private boolean enabled = true;

    public abstract void validate(T input) throws ValidatorException;

    public boolean isEnabled() { // TODO: remove for now
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
