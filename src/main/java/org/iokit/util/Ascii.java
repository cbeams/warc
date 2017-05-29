package org.iokit.util;

public class Ascii {

    public static final char TAB = '\t';  // 9
    public static final char LF = '\n';   // 10
    public static final char CR = '\r';   // 13
    public static final char SPACE = ' '; // 32
    public static final char COLON = ':'; // 58
    public static final char COMMA = ',';
    public static final char SEMICOLON = ';';
    public static final char LEFT_PARENTHESIS = '(';
    public static final char RIGHT_PARENTHESIS = ')';
    public static final char AT_SIGN = '@';
    public static final char BACKSLASH = '\\';
    public static final char SLASH = '/';
    public static final char LEFT_SQUARE_BRACKET = '[';
    public static final char RIGHT_SQUARE_BRACKET = ']';
    public static final char QUESTION_MARK = '?';
    public static final char EQUALS_SIGN = '=';
    public static final char LEFT_CURLY_BRACKET = '{';
    public static final char RIGHT_CURLY_BRACKET = '}';

    // TODO: fill out remainder of non-alphanumeric Ascii table per https://en.wikipedia.org/wiki/ASCII

    private Ascii() {
    }

    public static boolean isAsciiChar(int c) {
        return c >= 0 && c <= 127;
    }

    public static boolean isAsciiControlChar(int c) {
        return isAsciiChar(c) && (c <= 31 || c == 127);
    }
}
