package org.iokit.core.input;

public interface LineInput extends Input {

    int readLine(byte[] chunk, int start, int length);
}
