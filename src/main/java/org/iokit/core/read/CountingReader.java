package org.iokit.core.read;

import java.util.Optional;

public class CountingReader<T> extends SequenceReader<T> {

    protected long readCount = 0;

    public CountingReader(Reader<T> reader) {
        super(reader);
    }

    @Override
    public Optional<T> readOptional() throws ReaderException {
        Optional<T> value = super.readOptional();
        value.ifPresent(v -> readCount++);
        return value;
    }

    public long getReadCount() {
        return readCount;
    }
}
