package org.iokit.core.read;

import org.iokit.core.input.LineInput;
import org.iokit.core.input.LineInputStream;
import org.iokit.core.input.Repositionable;

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

public class LineReader implements Repositionable, Reader<String> {

    private final LineInput input;
    private final Charset charset;

    public LineReader(LineInput input) {
        this(input, StandardCharsets.UTF_8);
    }

    public LineReader(LineInput input, Charset charset) {
        this.input = input;
        this.charset = charset;
    }

    public int readByte() {
        try {
            return ((LineInputStream)input).read();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public String read() throws ReaderException, EOFException {
        byte[] chunk = new byte[1024];

        int start = 0, length;
        while ((length = input.readLine(chunk, start, chunk.length - start)) == chunk.length - start) {
            start += length;
            chunk = ByteArrays.grow(chunk, chunk.length + 1);
        }

        int totalLength = length + start;
        if (totalLength == 0)
            return "";

        String line = bytesAsString(chunk, totalLength, charset);

        if(line.isEmpty())
            throw new EOFException();

        return line;
    }

    private static String bytesAsString(byte[] bytes, int length, Charset charset) {
        StringBuilder s = new StringBuilder();

        try (java.io.Reader reader = new InputStreamReader(new FastByteArrayInputStream(bytes), charset)) {
            for (int i = 0; i < length; i++) {
                int c = reader.read();
                s.append((char) c);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return s.toString();
    }

    @Override
    public long getPosition() {
        return input.getPosition();
    }

    @Override
    public void setPosition(long newPosition) {
        input.setPosition(newPosition);
    }
}
