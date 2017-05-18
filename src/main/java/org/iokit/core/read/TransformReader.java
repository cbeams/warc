package org.iokit.core.read;

public abstract class TransformReader<R extends Reader, T> extends InputReader<T> {

    protected final R reader;

    public TransformReader(R reader) {
        super(reader.getInput());
        this.reader = reader;
    }
}
