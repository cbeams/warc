package org.iokit.core.write;

import java.io.OutputStream;

public abstract class ParameterlessWriter extends IOKitWriter<Void> {

    public ParameterlessWriter(OutputStream out) {
        super(out);
    }

    @Override
    @Deprecated
    public final void write(Void unsupported) {
        throw new UnsupportedOperationException("This writer does not accept a parameter. Call write() instead.");
    }

    public abstract void write();
}
