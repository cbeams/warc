package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import it.unimi.dsi.fastutil.bytes.ByteArrays;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LineReader implements Reader<String> {

    private final LineInputStream input;
    private final Charset charset;

    public LineReader(LineInputStream input) {
        this(input, StandardCharsets.UTF_8);
    }

    public LineReader(LineInputStream input, Charset charset) {
        this.input = input;
        this.charset = charset;
    }

    public byte peek() {
        return input.peek();
    }

    byte[] chunk = new byte[1024];

    public String read() throws ReaderException {

        int start = 0, length;
        while ((length = input.readLine(chunk, start, chunk.length - start)) == chunk.length - start) {
            start += length;
            chunk = ByteArrays.grow(chunk, chunk.length + 1);
        }

        if (length == -1)
            throw new EndOfInputException();

        int total = length + start;

        return new String(chunk, 0, total, charset);
    }

    byte[] testchunk = new byte[32*1024];

    public byte[] fastRead() throws ReaderException {

        int start = 0, length;
        while ((length = input.readLine(testchunk, start, testchunk.length - start)) == testchunk.length - start) {
            start += length;
            testchunk = ByteArrays.grow(testchunk, testchunk.length + 1);
        }

        return length == -1 ? null : testchunk;
    }

    public String fastStringRead() throws ReaderException {

        int start = 0, length;
        while ((length = input.readLine(testchunk, start, testchunk.length - start)) == testchunk.length - start) {
            start += length;
            testchunk = ByteArrays.grow(testchunk, testchunk.length + 1);
        }

        return length == -1 ? null : length > 1024 ? "ignore body" : new String(testchunk, 0, length, charset);
    }
}
