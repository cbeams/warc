package org.iokit.gzip;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.LineTerminator;
import org.iokit.core.Try;

import java.util.zip.GZIPOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MultiMemberGzipOutputStream extends IOKitOutputStream {

    private final int size;

    private GZIPOutputStream member;

    public MultiMemberGzipOutputStream(OutputStream out, LineTerminator terminator, int size) throws IOException {
        super(out, terminator);
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


    public static class Adapter implements IOKitOutputStream.Adapter {

        public static final String GZIP_FILE_SUFFIX = ".gz";

        @Override
        public boolean canAdapt(File file) {
            return file.getName().endsWith(GZIP_FILE_SUFFIX);
        }

        @Override
        public MultiMemberGzipOutputStream adapt(File file, LineTerminator terminator) {
            return Try.toCall(() -> new MultiMemberGzipOutputStream(new FileOutputStream(file), terminator, 1024 * 1024));
        }
    }
}
