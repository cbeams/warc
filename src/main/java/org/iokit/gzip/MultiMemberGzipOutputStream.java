package org.iokit.gzip;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.Try;

import java.util.zip.GZIPOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class MultiMemberGzipOutputStream extends IOKitOutputStream {

    public static final int DEFAULT_SIZE = 512; // same as GZIPOutputStream's hard-coded default

    private final int size;

    private GZIPOutputStream member;

    public MultiMemberGzipOutputStream(OutputStream out) throws IOException {
        this(out, DEFAULT_SIZE);
    }

    public MultiMemberGzipOutputStream(OutputStream out, int size) throws IOException {
        super(out);
        this.size = size;
    }

    @Override
    public void write(int b) throws IOException {
        member.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        member.write(b, off, len);
    }

    @Override
    public void startSegment() {
        this.member = Try.toCall(() -> new GZIPOutputStream(out, size));
    }

    @Override
    public void finishSegment() {
        Try.toRun(() -> member.finish());
    }
}
