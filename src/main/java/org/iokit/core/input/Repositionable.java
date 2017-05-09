package org.iokit.core.input;

public interface Repositionable {

    long getPosition();

    void setPosition(long newPosition);
}
