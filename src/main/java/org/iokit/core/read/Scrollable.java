package org.iokit.core.read;

public interface Scrollable {

    boolean isAtEOF();

    void seek(long position);
}
