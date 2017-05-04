package org.iokit.imf;

import java.util.Set;

public class StartLineMessageHeader<T> extends MessageHeader {

    protected final T startLineValue;

    public StartLineMessageHeader(T startLineValue, Set<Field> fields) {
        super(fields);
        this.startLineValue = startLineValue;
    }
}
