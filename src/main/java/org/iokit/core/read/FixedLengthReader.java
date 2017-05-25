package org.iokit.core.read;

import java.io.InputStream;

public abstract class FixedLengthReader<V> extends Reader<V> {

    public FixedLengthReader(InputStream in) {
        super(in);
    }

    @Override
    @Deprecated
    public V read() {
        throw new UnsupportedOperationException("This reader requires a parameter. Call read(int) instead.");
    }

    public abstract V read(int length);
}
