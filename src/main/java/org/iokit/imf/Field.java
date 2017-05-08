package org.iokit.imf;

import org.iokit.core.token.SimpleToken;

import java.util.Objects;

public class Field {

    public static final char SEPARATOR = ':';

    private final Name name;
    private final Value value;

    public Field(Name name, Value value) {
        this.name = name;
        this.value = value;
    }

    public Name getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s%c %s", name.toString(), SEPARATOR, value);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Field field = (Field) that;
        return Objects.equals(name, field.name) &&
            Objects.equals(value, field.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }


    /**
     * Field names are case-insensitive.
     */
    public static class Name extends SimpleToken {

        public Name(String value) {
            super(value);
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) return true;
            if (that == null || getClass() != that.getClass()) return false;
            Name name = (Name) that;
            return Objects.equals(this.getValue().toLowerCase(), name.getValue().toLowerCase());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getValue().toLowerCase());
        }
    }


    public static class Value {

        private final String value;

        public Value(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) return true;
            if (that == null || getClass() != that.getClass()) return false;
            Value value = (Value) that;
            return Objects.equals(this.value, value.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
