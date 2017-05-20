package org.iokit.core.read;

import org.iokit.core.input.Input;

import java.util.Optional;

public abstract class CountingReader<T> extends OptionalReader<T> {

    public static final int DEFAULT_MINIMUM_READ_COUNT = 0;

    private int minimumReadCount = DEFAULT_MINIMUM_READ_COUNT;

    private long readCount;

    public CountingReader(Input input) {
        super(input);
    }

    public final Optional<T> readOptional() throws ReaderException {
        Optional<T> value = readBeforeCounting();

        if (value.isPresent())
            readCount++;

        else if (readCount < minimumReadCount)
            throw new ReaderException(
                "Expected to read at least %d value(s), but %d were found", minimumReadCount, readCount);

        return value;
    }

    protected abstract Optional<T> readBeforeCounting();

    public void setMinimumReadCount(int minimumReadCount) {
        this.minimumReadCount = minimumReadCount;
    }

    public long getReadCount() {
        return readCount;
    }
}
