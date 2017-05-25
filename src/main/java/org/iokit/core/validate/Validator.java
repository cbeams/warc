package org.iokit.core.validate;

public interface Validator<T> {

    void validate(T input);
}
