package org.iokit.message;

public class FieldSetHeader<FS extends FieldSet> implements Header {

    protected final FS fieldSet;

    public FieldSetHeader(FS fieldSet) {
        this.fieldSet = fieldSet;
    }
}
