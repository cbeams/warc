package org.iokit.core.input;

import org.iokit.core.token.LineTerminator;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

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

    public byte peek() throws UncheckedIOException {
        try {
            long lastPosition = in.position();
            int next = in.read();
            in.position(lastPosition);
            return (byte) next;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public int read() throws UncheckedIOException {
        try {
            return in.read();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public int readLine(byte[] chunk, int start, int length) throws UncheckedIOException {
        try {
            return in.readLine(chunk, start, length, terminators);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public boolean isComplete() throws UncheckedIOException {
        return peek() == -1;
    }
}
