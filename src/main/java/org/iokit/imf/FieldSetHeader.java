package org.iokit.imf;

public class FieldSetHeader<F extends FieldSet> implements Header {

    protected final F fieldSet;

    public FieldSetHeader(F fieldSet) {
        this.fieldSet = fieldSet;
    }
}
