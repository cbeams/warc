package org.iokit.core.read;

import org.iokit.core.input.Input;

import java.io.Closeable;

public abstract class InputReader<T> implements Closeable, Reader<T> {

    protected final Input input;

    public InputReader(Input input) {
        this.input = input;
    }

    public Input getInput() {
        return input;
    }

    @Override
    public void close() {
        input.close();
    }
}
