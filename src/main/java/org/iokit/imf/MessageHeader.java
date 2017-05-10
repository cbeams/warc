package org.iokit.imf;

import java.util.Set;

public class MessageHeader {

    private final Set<Field> fields;

    public MessageHeader(Set<Field> fields) {
        this.fields = fields;
    }

    public Field getField(String name) {
        return fields.stream()
            .filter(f -> f.getName().getValue().equals(name))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }

    public Set<Field> getFields() {
        return fields;
    }
}
