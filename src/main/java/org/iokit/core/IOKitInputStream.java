package org.iokit.core;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public class IOKitInputStream extends InputStream {

    private final FastBufferedInputStream in;
    private final EnumSet<FastBufferedInputStream.LineTerminator> terminators;

    public IOKitInputStream(InputStream in) {
        this(in, LineTerminator.values());
    }

    public IOKitInputStream(InputStream in, LineTerminator... terminators) {
        this.in = requireNonNull(new FastBufferedInputStream(in));

        this.terminators = EnumSet.copyOf(
            Stream.of(terminators)
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

        public static final ThreadLocal<InputStream> ADAPTED_STREAM = new ThreadLocal<>();

        static {
            ServiceLoader.load(Adapter.class).forEach(ADAPTERS::add); /* TODO: ~100ms performance hit is negligible
                                                                         for large files (eg warc), but noticable
                                                                         for small files.
                                                                         See https://stackoverflow.com/a/7237152 */
        }

        public abstract boolean canAdapt(byte[] magic);

        public abstract IOKitInputStream adapt(InputStream in);


        public static IOKitInputStream adaptFrom(InputStream in) {
            return adaptFrom(in, DEFAULT_MAGIC_SIZE);
        }

        public static IOKitInputStream adaptFrom(InputStream in, int size) {
            byte[] magic = new byte[size];
            PushbackInputStream pbin = new PushbackInputStream(in, size);

            ADAPTED_STREAM.set(pbin);

            int len = Try.toCall(() -> pbin.read(magic));
            if (len == -1)
                return new IOKitInputStream(pbin);
            Try.toRun(() -> pbin.unread(magic, 0, len));

            return ADAPTERS.stream()
                .filter(mapper -> mapper.canAdapt(magic))
                .map(mapper -> mapper.adapt(pbin))
                .findFirst()
                .orElse(new IOKitInputStream(pbin));
        }
    }
}
