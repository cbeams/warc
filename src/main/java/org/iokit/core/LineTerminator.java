package org.iokit.core;

public enum LineTerminator {

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
