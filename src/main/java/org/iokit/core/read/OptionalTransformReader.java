package org.iokit.core.read;

public abstract class OptionalTransformReader<R extends Reader, T> extends TransformReader<R, T> implements OptionalReader<T> {

    public OptionalTransformReader(R reader) {
        super(reader);
    }

    public T read() throws ReaderException {
        return readOptional().orElseThrow(() -> new ReaderException("TODO"));
    }
}
