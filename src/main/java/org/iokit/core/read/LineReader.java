package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import it.unimi.dsi.fastutil.bytes.ByteArrays;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LineReader extends CloseableReader<String> {

    private final Charset charset;
    private final LineInputStream input;
    byte[] chunk = new byte[1024];

    public LineReader(LineInputStream input) {
        this(input, StandardCharsets.UTF_8);
    }

    public LineReader(LineInputStream input, Charset charset) {
        super(input);
        this.input = input;
        this.charset = charset;
    }

    public byte peek() {
        return input.peek();
    }

    @Override
    public LineInputStream getInput() {
        return input;
    }

    public String read() throws ReaderException {
        int start = 0, length;
        while ((length = input.readLine(chunk, start, chunk.length - start)) == chunk.length - start) {
            start += length;
            chunk = ByteArrays.grow(chunk, chunk.length + 1);
        }

        if (length == -1)
            throw new EndOfInputException();

        return new String(chunk, 0, length + start, charset);
    }
}
