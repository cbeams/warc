package org.iokit.core;

import org.iokit.util.Ascii;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.IOException;
import java.io.InputStream;

import java.util.EnumSet;
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


    public enum LineTerminator {

        // Note that names below (CR, LF, CR_LF) must be exactly
        // the same as the names in fastutil's LineTerminator enum

        CR((byte) Ascii.CR),
        LF((byte) Ascii.LF),
        CR_LF((byte) Ascii.CR, (byte) Ascii.LF);

        public static final String SYSTEM_LINE_TERMINATOR_KEY = "line.separator";

        public final byte[] bytes;
        private final String value;

        LineTerminator(byte... bytes) {
            this.bytes = bytes;
            this.value = new String(bytes);
        }

        @Override
        public String toString() {
            return value;
        }

        public static LineTerminator systemValue() {
            return parseValue(System.getProperty(SYSTEM_LINE_TERMINATOR_KEY));
        }

        public static LineTerminator parseValue(String value) {
            for (LineTerminator terminator : LineTerminator.values())
                if (terminator.value.equals(value))
                    return terminator;

            throw new IllegalArgumentException("No LineTerminator found matching [" + value + "]");
        }
    }
}
