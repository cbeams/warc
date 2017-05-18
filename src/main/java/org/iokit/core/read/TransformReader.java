package org.iokit.core.read;

import org.iokit.core.input.Input;

public abstract class TransformReader<R extends InputReader, T> extends InputReader<Input, T> {

    protected final R reader;

    public TransformReader(R reader) {
        super(reader.getInput());
        this.reader = reader;
    }
}
