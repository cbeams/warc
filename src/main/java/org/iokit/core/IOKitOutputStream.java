package org.iokit.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Arrays;

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


    public interface Adapter {

        boolean canAdapt(File file);

        IOKitOutputStream adapt(File file, LineTerminator terminator);
    }


    public static IOKitOutputStream adapt(File file, LineTerminator terminator, Adapter... adapters) {
        return Arrays.stream(adapters)
            .filter(adapter -> adapter.canAdapt(file))
            .map(adapter -> adapter.adapt(file, terminator))
            .findFirst()
            .orElse(new IOKitOutputStream(Try.toCall(() -> new FileOutputStream(file)), terminator));
    }
}
