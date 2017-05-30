package org.iokit.core;

import java.io.IOException;
import java.io.OutputStream;

public class IOKitOutputStream extends OutputStream {

    protected final OutputStream out;
    protected final LineTerminator terminator;

    public IOKitOutputStream(OutputStream out) {
        this(out, LineTerminator.systemValue());
    }

    public IOKitOutputStream(OutputStream out, LineTerminator terminator) {
        this.out = out;
        this.terminator = terminator;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    public void writeLine(byte[] b) {
        writeLine(b, 0, b.length);
    }

    public void writeLine(byte[] b, int off, int len) {
        Try.toRun(() -> write(b, off, len));
        writeLine();
    }

    public void writeLine() {
        Try.toRun(() -> write(terminator.bytes));
    }

    public void startSegment() {
    }

    public void finishSegment() {
    }

    @Override
    public void close() {
        Try.toRun(out::close);
    }

}
