package org.iokit.core.input;

import java.util.zip.GZIPInputStream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UncheckedIOException;

import java.util.ArrayList;
import java.util.List;

public class MagicInputStream extends FilterInputStream {

    public static final int DEFAULT_MAGIC_SIZE = 16;

    private static final List<MagicHandler> magicHandlers = new ArrayList<MagicHandler>() {{
        add(new GZipMagicHandler());
    }};

    public MagicInputStream(InputStream in) throws IOException {
        this(in, DEFAULT_MAGIC_SIZE);
    }

    public MagicInputStream(InputStream in, int size) throws IOException {
        super(map(in, size));
    }

    public static InputStream map(InputStream in, int size) throws IOException {

        PushbackInputStream input = new PushbackInputStream(in, size);

        byte[] magic = new byte[size];

        int len = input.read(magic);
        if (len == -1)
            return input;
        input.unread(magic, 0, len);

        return magicHandlers.stream()
            .filter(h -> h.canHandle(magic))
            .map(h -> h.handle(input))
            .findFirst()
            .orElse(input);
    }
}


interface MagicHandler {

    boolean canHandle(byte[] magic);

    InputStream handle(InputStream in);
}


class GZipMagicHandler implements MagicHandler {

    @Override
    public boolean canHandle(byte[] magic) {
        return magic.length >= 2 && magic[0] == (byte) 0x1f && magic[1] == (byte) 0x8b;
    }

    @Override
    public InputStream handle(InputStream in) {
        if (in instanceof GZIPInputStream)
            return in;

        try {
            return new GZIPInputStream(in);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
