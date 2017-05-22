package org.iokit.core.output.mapping;

import java.io.FilterOutputStream;
import java.io.OutputStream;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class MappedOutputStream extends FilterOutputStream {

    private static final Set<OutputStreamMapper> MAPPERS = new LinkedHashSet<>();

    static {
        ServiceLoader.load(OutputStreamMapper.class).forEach(MAPPERS::add);
    }

    public MappedOutputStream(MappableFileOutputStream out) {
        super(map(out));
    }

    public MappedOutputStream(OutputStream out, Class<? extends OutputStream> toType) {
        super(map(out, toType));
    }

    public static OutputStream map(MappableFileOutputStream out) {

        return MAPPERS.stream()
            .filter(mapper -> mapper.canMap(out.getFile()))
            .map(mapper -> mapper.map(out))
            .findFirst()
            .orElse(out);
    }

    public static OutputStream map(OutputStream out, Class<? extends OutputStream> toType) {

        return MAPPERS.stream()
            .filter(mapper -> mapper.canMap(toType))
            .map(mapper -> mapper.map(out))
            .findFirst()
            .orElse(out);
    }
}
