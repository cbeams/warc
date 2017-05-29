package org.iokit.magic;

import java.io.File;
import java.io.OutputStream;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class OutputStreamMapper {

    private static final Set<OutputStreamMapper> MAPPERS = new LinkedHashSet<>();

    static {
        ServiceLoader.load(OutputStreamMapper.class).forEach(MAPPERS::add);
    }

    public abstract boolean canMap(File file);

    public abstract boolean canMap(Class<? extends OutputStream> type);

    public abstract OutputStream map(OutputStream out);

    public static OutputStream mapFrom(MappableFileOutputStream out) {
        ServiceLoader.load(OutputStreamMapper.class).forEach(OutputStreamMapper.MAPPERS::add);
        return MAPPERS.stream()
            .filter(mapper -> mapper.canMap(out.getFile()))
            .map(mapper -> mapper.map(out))
            .findFirst()
            .orElse(out);
    }

    public static OutputStream mapFrom(OutputStream out, Class<? extends OutputStream> toType) {
        ServiceLoader.load(OutputStreamMapper.class).forEach(OutputStreamMapper.MAPPERS::add);
        return OutputStreamMapper.MAPPERS.stream()
            .filter(mapper -> mapper.canMap(toType))
            .map(mapper -> mapper.map(out))
            .findFirst()
            .orElse(out);
    }
}
