package org.iokit.imf;

import java.util.Set;

public class StartLineHeader<T> extends Header {

    protected final T startLineValue;

    public StartLineHeader(T startLineValue, Set<Field> fields) {
        super(fields);
        this.startLineValue = startLineValue;
    }
}
