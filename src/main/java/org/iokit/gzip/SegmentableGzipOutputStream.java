package org.iokit.gzip;

import org.iokit.core.write.Segmentable;

import org.iokit.core.Try;

import java.util.zip.GZIPOutputStream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SegmentableGzipOutputStream extends FilterOutputStream implements Segmentable {

    public static final int DEFAULT_SIZE = 512; // same as GZIPOutputStream's hard-coded default

    private final int size;

    private GZIPOutputStream segment;

    public SegmentableGzipOutputStream(OutputStream out) throws IOException {
        this(out, DEFAULT_SIZE);
    }

    public SegmentableGzipOutputStream(OutputStream out, int size) throws IOException {
        super(out);
        this.size = size;
    }

    @Override
    public void write(int b) throws IOException {
        segment.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        segment.write(b, off, len);
    }

    @Override
    public void startSegment() {
        this.segment = Try.toCall(() -> new GZIPOutputStream(out, size));
    }

    @Override
    public void finishSegment() {
        Try.toRun(() -> segment.finish());
    }
}
