package org.iokit.core;

public enum LineTerminator {

    // Note that names below (CR, LF, CR_LF) must be exactly
    // the same as the names in fastutil's LineTerminator enum

    CR("\r"),
    LF("\n"),
    CR_LF("\r\n");

    private final String value;
    public final byte[] bytes;

    LineTerminator(String value) {
        this.value = value;
        this.bytes = value.getBytes();
    }

    @Override
    public String toString() {
        return value;
    }
}
