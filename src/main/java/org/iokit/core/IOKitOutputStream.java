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

    public void writeLine(String string) {
        writeLine(string.getBytes());
    }

    public void writeLine(byte[] bytes) {
        Try.toRun(() -> write(bytes));
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
