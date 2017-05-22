package org.iokit.imf;

public class StartLineHeader<T> extends Header {

    protected final T startLineValue;

    public StartLineHeader(T startLineValue, FieldSet fieldSet) {
        super(fieldSet);
        this.startLineValue = startLineValue;
    }
}
