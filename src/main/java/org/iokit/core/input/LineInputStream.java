package org.iokit.core.input;

import org.iokit.core.token.LineTerminator;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import java.util.EnumSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class LineInputStream extends InputStream implements LineInput {

    protected final FastBufferedInputStream in;
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

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int readLine(byte[] chunk, int start, int length) {
        try {
            return in.readLine(chunk, start, length, terminators);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public long getPosition() throws UncheckedIOException {
        try {
            return in.position();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void setPosition(long newPosition) throws UncheckedIOException {
        try {
            in.position(newPosition);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
