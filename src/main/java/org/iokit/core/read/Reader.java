package org.iokit.core.read;

import org.iokit.core.input.Input;

import org.iokit.lang.Try;

import java.io.Closeable;

public abstract class Reader<T> implements Closeable {

    protected final Input input;

    public Reader(Input input) {
        this.input = input;
    }

    public abstract T read() throws ReaderException;

    public Input getInput() {
        return input;
    }

    @Override
    public void close() {
        Try.toRun(input::close);
    }
}
