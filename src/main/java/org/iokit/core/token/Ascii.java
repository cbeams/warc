package org.iokit.core.token;

public class Ascii {

    private Ascii() {
    }

    public static boolean isAsciiChar(int c) {
        return c >= 0 && c <= 127;
    }

    public static boolean isAsciiControlChar(int c) {
        return isAsciiChar(c) && (c <= 31 || c == 127);
    }
}
