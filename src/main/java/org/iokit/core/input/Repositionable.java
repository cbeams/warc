package org.iokit.core.input;

public interface Repositionable {

    long setPosition();

    void setPosition(long newPosition);
}
