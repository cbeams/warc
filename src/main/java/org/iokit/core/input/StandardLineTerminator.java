package org.iokit.core.input;

public enum StandardLineTerminator implements LineTerminator {

    /** A carriage return (CR, ASCII 13). */
    CR,
    /** A line feed (LF, ASCII 10). */
    LF,
    /** A carriage return followed by a line feed (CR/LF, ASCII 13/10). */
    CR_LF
}
