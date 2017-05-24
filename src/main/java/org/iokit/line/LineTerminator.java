package org.iokit.line;

import org.iokit.core.Ascii;

public enum LineTerminator {

    // Note that names below (CR, LF, CR_LF) must be exactly
    // the same as the names in fastutil's LineTerminator enum

    CR((byte) Ascii.CR),
    LF((byte) Ascii.LF),
    CR_LF((byte) Ascii.CR, (byte) Ascii.LF);

    public final byte[] bytes;
    private final String value;

    LineTerminator(byte... bytes) {
        this.bytes = bytes;
        this.value = new String(bytes);
    }

    @Override
    public String toString() {
        return value;
    }
}
