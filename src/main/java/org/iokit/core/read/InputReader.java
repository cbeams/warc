package org.iokit.core.read;

import org.iokit.core.input.Input;

import java.io.Closeable;

public abstract class InputReader<I extends Input, T> implements Closeable, Reader<T> {

    protected final I input;

    public InputReader(I input) {
        this.input = input;
    }

    public I getInput() {
        return input;
    }

    @Override
    public void close() {
        input.close();
    }
}
