package org.iokit.core.read;

import java.io.InputStream;

public abstract class FixedLengthReader<T> extends Reader<T> {

    public FixedLengthReader(InputStream in) {
        super(in);
    }

    @Override
    @Deprecated
    public T read() {
        throw new UnsupportedOperationException("This reader requires a parameter. Call read(int) instead.");
    }

    public abstract T read(int length);
}
