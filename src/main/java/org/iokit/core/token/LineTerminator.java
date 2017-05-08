package org.iokit.core.token;

public enum LineTerminator implements Token {

    /** A carriage return (CR, ASCII 13). */
    CR("\r"),

    /** A line feed (LF, ASCII 10). */
    LF("\n"),

    /** A carriage return followed by a line feed (CR/LF, ASCII 13/10). */
    CR_LF("\r\n");

    private final String value;

    LineTerminator(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
