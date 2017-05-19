package org.iokit.core.read;

import java.util.Optional;

public class SkipReader extends Reader<Boolean> {

    private final int times;
    private final Reader reader;

    public SkipReader(int times, Reader reader) {
        super(reader.getInput());
        this.times = times;
        this.reader = reader;
    }

    @Override
    public Boolean read() throws ReaderException {
        for (int i = 0; i < times; i++)
            if (!reader.readOptional().isPresent())
                return false;

        return true;
    }

    @Override
    public Optional<Boolean> readOptional() throws ReaderException {
        return Optional.of(read());
    }
}
