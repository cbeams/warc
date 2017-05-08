package org.iokit.core.token;

import java.util.Objects;

public class SimpleToken implements Token {

    private final String value;

    public SimpleToken(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        SimpleToken token = (SimpleToken) that;
        return Objects.equals(this.value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
