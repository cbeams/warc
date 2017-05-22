package org.iokit.core.input;

import org.iokit.core.LineTerminator;

import org.iokit.lang.Try;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.IOException;
import java.io.InputStream;

import java.util.EnumSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class LineInputStream extends InputStream {

    private final FastBufferedInputStream in;
    private final EnumSet<FastBufferedInputStream.LineTerminator> terminators;

    public LineInputStream(InputStream in) {
        this(in, LineTerminator.values());
    }

    public LineInputStream(InputStream in, LineTerminator... terminators) {
        if (in == null)
            throw new IllegalArgumentException("input stream must not be null");

        this.in = in instanceof FastBufferedInputStream ?
            (FastBufferedInputStream) in :
            new FastBufferedInputStream(in);

        this.terminators = EnumSet.copyOf(
            Stream.of(terminators)
                .map(terminator -> FastBufferedInputStream.LineTerminator.valueOf(terminator.toString()))
                .collect(toSet()));
    }

    public byte peek() {
        return Try.toCall(() -> {
            long lastPosition = in.position();
            int next = in.read();
            in.position(lastPosition);
            return (byte) next;
        });
    }

    public void seek(long offset) {
        Try.toCall(() -> in.skip(offset - in.position()));
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    public int readLine(byte[] chunk, int start, int length) {
        return Try.toCall(() -> in.readLine(chunk, start, length, terminators));
    }

    public boolean isComplete() {
        return peek() == -1;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
