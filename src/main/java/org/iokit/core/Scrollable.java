package org.iokit.core;

public interface Scrollable {

    boolean isAtEOF();

    void seek(long position);
}
