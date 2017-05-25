package org.iokit.imf;

public class FieldSetHeader<FS extends FieldSet> implements Header {

    protected final FS fieldSet;

    public FieldSetHeader(FS fieldSet) {
        this.fieldSet = fieldSet;
    }
}
