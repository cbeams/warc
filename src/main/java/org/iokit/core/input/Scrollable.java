package org.iokit.core.input;

public interface Scrollable {

    boolean isAtEOF();

    void seek(long position);
}
