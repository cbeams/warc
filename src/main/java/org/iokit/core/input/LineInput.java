package org.iokit.core.input;

public interface LineInput extends Input {

    byte peek();

    int readLine(byte[] chunk, int start, int length);

    boolean isComplete();
}
