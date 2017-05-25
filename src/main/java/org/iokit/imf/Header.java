package org.iokit.imf;

import java.util.Optional;
import java.util.function.Function;

public class Header {

    private final FieldSet fieldSet;

    public Header(FieldSet fieldSet) {
        this.fieldSet = fieldSet;
    }

    public Optional<String> getFieldValue(String name) {
        return getFieldValue(new FieldName(name));
    }

    public Optional<String> getFieldValue(DefinedField definedField) {
        return getFieldValue(definedField.fieldName());
    }

    public Optional<String> getFieldValue(FieldName name) {
        Optional<Field> field = getField(name);
        return field.isPresent() ?
            Optional.of(field.get().getValue()).map(FieldValue::toString) :
            Optional.empty();
    }

    public Optional<Field> getField(String name) {
        return getField(new FieldName(name));
    }

    public Optional<Field> getField(FieldName name) {
        return fieldSet.stream()
            .filter(field -> field.getName().equals(name))
            .findFirst();
    }

    public String getRequiredFieldValue(DefinedField field) {
        return getRequiredFieldValue(field, Function.identity());
    }

    public <T> T getRequiredFieldValue(DefinedField field, Function<String, T> mapper) {
        return getFieldValue(field).map(mapper).orElseThrow(() -> new FieldNotFoundException(field.fieldName()));
    }

    public FieldSet getFieldSet() {
        return fieldSet;
    }
}
