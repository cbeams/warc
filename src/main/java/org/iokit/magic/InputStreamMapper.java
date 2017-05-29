package org.iokit.magic;

import org.iokit.core.Try;

import java.io.InputStream;
import java.io.PushbackInputStream;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class InputStreamMapper {

    public static final int DEFAULT_MAGIC_SIZE = 8;

    private static final Set<InputStreamMapper> MAPPERS = new LinkedHashSet<>();

    public static final ThreadLocal<InputStream> MAPPED_STREAM = new ThreadLocal<>();

    static {
        ServiceLoader.load(InputStreamMapper.class).forEach(MAPPERS::add); /* TODO: ~100ms performance hit is negligible
                                                                              for large files (eg warc), but noticable
                                                                              for small files.
                                                                              See https://stackoverflow.com/a/7237152 */
    }

    public abstract boolean canMap(byte[] magic);

    public abstract InputStream map(InputStream in);


    public static InputStream mapFrom(InputStream in) {
        return mapFrom(in, DEFAULT_MAGIC_SIZE);
    }

    public static InputStream mapFrom(InputStream in, int size) {
        byte[] magic = new byte[size];
        PushbackInputStream pbin = new PushbackInputStream(in, size);

        MAPPED_STREAM.set(pbin);

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
