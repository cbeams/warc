package org.iokit.core.validate;

public interface Validator<V> {

    void validate(V input);
}
