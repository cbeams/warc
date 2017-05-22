package org.iokit.core.input;

import org.iokit.lang.Try;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class MappedInputStream extends FilterInputStream {

    public static final int DEFAULT_MAGIC_SIZE = 16;

    private static final Set<InputStreamMapper> MAPPERS = new LinkedHashSet<>();

    static {
        ServiceLoader.load(InputStreamMapper.class).forEach(MAPPERS::add);
    }

    public MappedInputStream(InputStream in) {
        this(in, DEFAULT_MAGIC_SIZE);
    }

    public MappedInputStream(InputStream in, int size) {
        super(map(in, size));
    }

    public static InputStream map(InputStream in, int size) {
        byte[] magic = new byte[size];
        PushbackInputStream pbin = new PushbackInputStream(in, size);

        int len = Try.toCall(() -> pbin.read(magic));
        if (len == -1)
            return pbin;
        Try.toRun(() -> pbin.unread(magic, 0, len));

        return MAPPERS.stream()
            .filter(mapper -> mapper.canMap(magic))
            .map(mapper -> mapper.map(pbin))
            .findFirst()
            .orElse(pbin);
    }
}
