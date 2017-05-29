package org.iokit.core.read;

import org.iokit.core.IOKitException;
import org.iokit.core.IOKitInputStream;

import java.util.Optional;

public abstract class CountingReader<V> extends OptionalReader<V> {

    public static final int DEFAULT_MINIMUM_READ_COUNT = 0;

    private int minimumReadCount = DEFAULT_MINIMUM_READ_COUNT;

    private long readCount;

    public CountingReader(IOKitInputStream in) {
        super(in);
    }

    public final Optional<V> readOptional() {
        Optional<V> value = readOptionalBeforeCounting();

        if (value.isPresent())
            readCount++;

        else if (readCount < minimumReadCount)
            throw new IOKitException(
                "Expected to read at least %d value(s), but %d were found", minimumReadCount, readCount);

        return value;
    }

    protected abstract Optional<V> readOptionalBeforeCounting();

    public void setMinimumReadCount(int minimumReadCount) {
        this.minimumReadCount = minimumReadCount;
    }

    public long getReadCount() {
        return readCount;
    }
}
