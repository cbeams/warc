package org.iokit.core.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UncheckedIOException;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class MagicInputStream extends FilterInputStream {

    public static final int DEFAULT_MAGIC_SIZE = 16;

    private static final Set<MagicInputStreamMapper> MAPPERS = new LinkedHashSet<>();

    static {
        ServiceLoader.load(MagicInputStreamMapper.class).forEach(MAPPERS::add);
    }

    public MagicInputStream(InputStream in) {
        this(in, DEFAULT_MAGIC_SIZE);
    }

    public MagicInputStream(InputStream in, int size) {
        super(map(in, size));
    }

    public static InputStream map(InputStream in, int size) {
        byte[] magic = new byte[size];
        PushbackInputStream input = new PushbackInputStream(in, size);

        try {
            int len = input.read(magic);
            if (len == -1)
                return input;
            input.unread(magic, 0, len);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return MAPPERS.stream()
            .filter(mapper -> mapper.canMap(magic))
            .map(mapper -> mapper.map(input))
            .findFirst()
            .orElse(input);
    }
}
