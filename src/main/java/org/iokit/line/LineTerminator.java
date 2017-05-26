package org.iokit.line;

import org.iokit.coding.Ascii;

public enum LineTerminator {

    // Note that names below (CR, LF, CR_LF) must be exactly
    // the same as the names in fastutil's LineTerminator enum

    CR((byte) Ascii.CR),
    LF((byte) Ascii.LF),
    CR_LF((byte) Ascii.CR, (byte) Ascii.LF);

    public static final String SYSTEM_LINE_TERMINATOR_KEY = "line.separator";

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

    public static LineTerminator systemValue() {
        return parseValue(System.getProperty(SYSTEM_LINE_TERMINATOR_KEY));
    }

    public static LineTerminator parseValue(String value) {
        for (LineTerminator terminator : values())
            if (terminator.value.equals(value))
                return terminator;

        throw new IllegalArgumentException("No LineTerminator found matching [" + value + "]");
    }
}
