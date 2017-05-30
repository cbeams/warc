package org.iokit.general;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitReader;

public abstract class ParameterizedReader<P, T> extends IOKitReader {

    public ParameterizedReader(IOKitInputStream in) {
        super(in);
    }

    @Override
    @Deprecated
    public T read() {
        throw new UnsupportedOperationException("This reader requires a parameter. Call read(P) instead.");
    }

    public abstract T read(P param);
}
