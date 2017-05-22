package org.iokit.imf;

public class Header {

    private final FieldSet fieldSet;

    public Header(FieldSet fieldSet) {
        this.fieldSet = fieldSet;
    }

    public Field getField(Field.Type field) {
        return getField(field.getName().toString());
    }

    public Field getField(String name) {
        return fieldSet.stream()
            .filter(f -> f.getName().getValue().equals(name))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }

    public FieldSet getFieldSet() {
        return fieldSet;
    }
}
