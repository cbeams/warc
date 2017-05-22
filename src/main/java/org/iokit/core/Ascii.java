package org.iokit.core;

public class Ascii {

    public static final char TAB = '\t';  // 9
    public static final char LF = '\n';   // 10
    public static final char CR = '\r';   // 13
    public static final char SPACE = ' '; // 32
    public static final char COLON = ':'; // 58

    private Ascii() {
    }

    public static boolean isAsciiChar(int c) {
        return c >= 0 && c <= 127;
    }

    public static boolean isAsciiControlChar(int c) {
        return isAsciiChar(c) && (c <= 31 || c == 127);
    }

    public static void main(String[] args) {
        System.out.println((int) TAB);
        System.out.println((int) LF);
        System.out.println((int) CR);
        System.out.println((int) SPACE);
        System.out.println((int) COLON);
    }
}
