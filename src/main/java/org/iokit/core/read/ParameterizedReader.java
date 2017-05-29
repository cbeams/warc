package org.iokit.core.read;

import java.io.InputStream;

public abstract class ParameterizedReader<P, T> extends IOKitReader {

    public ParameterizedReader(InputStream in) {
        super(in);
    }

    @Override
    @Deprecated
    public T read() {
        throw new UnsupportedOperationException("This reader requires a parameter. Call read(P) instead.");
    }

    public abstract T read(P param);
}
