package org.iokit.core.read;

import org.iokit.core.input.Input;

public abstract class InputReader<T> extends CloseableReader<T> {

    protected final Input input;

    public InputReader(Input input) {
        super(input);
        this.input = input;
    }

    @Override
    public Input getInput() {
        return input;
    }
}
