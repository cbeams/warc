package org.iokit.core.read;

import org.iokit.core.input.Input;

public abstract class AutoCloseableReader<T, I extends Input> implements AutoCloseable, Reader<T> {

    protected final I input;

    public AutoCloseableReader(I input) {
        this.input = input;
    }

    public void close() throws Exception {
        if (input instanceof AutoCloseable)
            ((AutoCloseable) input).close();
    }
}
