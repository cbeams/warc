package org.iokit.imf;

public class StartLineHeader<T> extends Header {

    protected final T startLineValue; // TODO: rename to startLine

    public StartLineHeader(T startLineValue, FieldSet fieldSet) {
        super(fieldSet);
        this.startLineValue = startLineValue;
    }
}
