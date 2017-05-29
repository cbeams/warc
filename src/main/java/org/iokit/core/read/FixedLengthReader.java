package org.iokit.core.read;

import org.iokit.core.IOKitInputStream;

public abstract class FixedLengthReader<V> extends IOKitReader<V> {

    public FixedLengthReader(IOKitInputStream in) {
        super(in);
    }

    @Override
    @Deprecated
    public V read() {
        throw new UnsupportedOperationException("This reader requires a parameter. Call read(int) instead.");
    }

    public abstract V read(int length);
}
