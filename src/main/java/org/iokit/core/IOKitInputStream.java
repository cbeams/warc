package org.iokit.core;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class IOKitInputStream extends InputStream {

    private final InputStream raw;
    private final FastBufferedInputStream in;
    private final EnumSet<FastBufferedInputStream.LineTerminator> terminators;

    public IOKitInputStream(InputStream in) {
        this(in, EnumSet.allOf(LineTerminator.class));
    }

    public IOKitInputStream(InputStream in, EnumSet<LineTerminator> terminators) {
        this(in, in, terminators);
    }

    public IOKitInputStream(InputStream in, InputStream raw) {
        this(in, raw, EnumSet.allOf(LineTerminator.class));
    }

    public IOKitInputStream(InputStream in, InputStream raw, EnumSet<LineTerminator> terminators) {
        this.in = requireNonNull(new FastBufferedInputStream(in));
        this.raw = requireNonNull(raw);

        this.terminators = EnumSet.copyOf(
            terminators.stream()
                .map(terminator -> FastBufferedInputStream.LineTerminator.valueOf(terminator.name()))
                .collect(toSet()));
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    public int readLine(byte[] chunk, int start, int length) {
        return Try.toCall(() -> in.readLine(chunk, start, length, terminators));
    }

    public boolean isAtEOF() {
        return peek() == -1;
    }

    public void skipRaw(long offset) {
        Try.toRun(() -> ByteStreams.skipFully(raw, offset));
    }

    public void seek(long offset) {
        Try.toCall(() -> in.skip(offset - in.position()));
    }

    public byte peek() {
        return Try.toCall(() -> {
            long lastPosition = in.position();
            int next = in.read();
            in.position(lastPosition);
            return (byte) next;
        });
    }

    @Override
    public void close() throws IOException {
        in.close();
    }


    public abstract static class Adapter {

        public static final int DEFAULT_MAGIC_SIZE = 8;

        private static final Set<Adapter> ADAPTERS = new LinkedHashSet<>();

        static {
            ServiceLoader.load(Adapter.class).forEach(ADAPTERS::add); /* TODO: ~100ms performance hit is negligible
                                                                         for large files (eg warc), but noticable
                                                                         for small files.
                                                                         See https://stackoverflow.com/a/7237152 */
        }

        public abstract boolean canAdapt(byte[] magic);

        public abstract IOKitInputStream adapt(InputStream in);


        public static IOKitInputStream adaptFrom(InputStream in, EnumSet<LineTerminator> terminators) {
            return adaptFrom(in, terminators, DEFAULT_MAGIC_SIZE);
        }

        public static IOKitInputStream adaptFrom(InputStream in, EnumSet<LineTerminator> terminators, int size) {
            byte[] magic = new byte[size];
            PushbackInputStream pbin = new PushbackInputStream(in, size);

            int len = Try.toCall(() -> pbin.read(magic));
            if (len == -1)
                return new IOKitInputStream(pbin, terminators);
            Try.toRun(() -> pbin.unread(magic, 0, len));

            return ADAPTERS.stream()
                .filter(mapper -> mapper.canAdapt(magic))
                .map(mapper -> mapper.adapt(pbin))
                .findFirst()
                .orElse(new IOKitInputStream(pbin, terminators));
        }
    }
}
