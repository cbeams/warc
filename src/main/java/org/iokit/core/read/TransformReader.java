package org.iokit.core.read;

import org.iokit.core.input.Input;

public abstract class TransformReader<R extends Reader, T> implements Reader<T> {

    protected final R reader;

    public TransformReader(R reader) {
        this.reader = reader;
    }

    @Override
    public final Input getInput() {
        return reader.getInput();
    }
}
