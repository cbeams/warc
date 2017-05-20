package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import it.unimi.dsi.fastutil.bytes.ByteArrays;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Optional;

public class LineReader extends OptionalReader<String> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected final LineInputStream input;
    protected final Charset charset;

    private byte[] chunk = new byte[1024];

    public LineReader(LineInputStream input) {
        this(input, DEFAULT_CHARSET);
    }

    public LineReader(LineInputStream input, Charset charset) {
        super(input);
        this.input = input;
        this.charset = charset;
    }

    @Override
    public Optional<String> readOptional() throws ReaderException {
        int start = 0, length;

        while ((length = input.readLine(chunk, start, chunk.length - start)) == chunk.length - start) {
            start += length;
            chunk = ByteArrays.grow(chunk, chunk.length + 1);
        }

        return length == -1 ?
            Optional.empty() :
            Optional.of(new String(chunk, 0, length + start, charset));
    }

    @Override
    public LineInputStream getInput() {
        return input;
    }
}
