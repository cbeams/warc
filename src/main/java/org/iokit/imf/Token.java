package org.iokit.imf;

import java.util.Objects;

public class Token {

    private final String value;

    public Token(String value) {
        this.value = value;
    }

    public String getValue() { // TODO: remove in favor of using toString only (confusing for field name to have a value)
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
        Token token = (Token) that;
        return Objects.equals(this.value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
