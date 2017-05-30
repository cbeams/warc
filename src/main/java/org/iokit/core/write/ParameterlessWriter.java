package org.iokit.core.write;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.IOKitWriter;

public abstract class ParameterlessWriter extends IOKitWriter<Void> {

    public ParameterlessWriter(IOKitOutputStream out) {
        super(out);
    }

    @Override
    @Deprecated
    public final void write(Void unsupported) {
        throw new UnsupportedOperationException("This writer does not accept a parameter. Call write() instead.");
    }

    public abstract void write();
}
