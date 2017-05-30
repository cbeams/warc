package org.iokit.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

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


    public abstract static class Adapter {

        private static final Set<Adapter> ADAPTERS = new LinkedHashSet<>();

        static {
            ServiceLoader.load(Adapter.class).forEach(ADAPTERS::add);
        }

        public abstract boolean canAdapt(File file);

        public abstract IOKitOutputStream adapt(OutputStream out);

        public static IOKitOutputStream adaptFrom(AdaptableFileOutputStream out) {
            ServiceLoader.load(Adapter.class).forEach(Adapter.ADAPTERS::add);
            return ADAPTERS.stream()
                .filter(mapper -> mapper.canAdapt(out.getFile()))
                .map(mapper -> mapper.adapt(out))
                .findFirst()
                .orElse(new IOKitOutputStream(out));
        }
    }


    public static class AdaptableFileOutputStream extends FileOutputStream {

        private final File file;

        public AdaptableFileOutputStream(File file) throws FileNotFoundException {
            super(file);
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }
}
