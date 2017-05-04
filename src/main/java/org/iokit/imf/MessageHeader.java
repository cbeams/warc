package org.iokit.imf;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageHeader {

    protected final Map<Field.Name, Field.Value> fields;

    public MessageHeader(Set<Field> fields) {
        this.fields = fields.stream().collect(Collectors.toMap(Field::getName, Field::getValue));
    }

    public Field getField(String name) {
        return fields.entrySet().stream()
            .filter(e -> e.getKey().getValue().equals(name))
            .map(e -> new Field(e.getKey(), e.getValue()))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }
}
