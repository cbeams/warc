package org.iokit.core;

import java.io.IOException;
import java.io.OutputStream;

public class IOKitOutputStream extends OutputStream {

    protected final OutputStream out;

    public IOKitOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void close() {
        Try.toRun(out::close);
    }
}
