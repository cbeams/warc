package org.iokit.core.validate;

public interface IOKitValidator<V> {

    void validate(V input);
}
