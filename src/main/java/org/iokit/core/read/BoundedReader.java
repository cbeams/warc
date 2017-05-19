package org.iokit.core.read;

import java.util.Optional;

public class BoundedReader<T> extends CountingReader<T> {

    private int minimumReadCount;

    public BoundedReader(Reader<T> reader, int minimumReadCount) {
        super(reader);
        this.minimumReadCount = minimumReadCount;
    }

    @Override
    public Optional<T> readOptional() throws ReaderException {
        Optional<T> value = super.readOptional();

        if (!value.isPresent())
            if (readCount < minimumReadCount)
                throw new ReaderException(
                    "Expected to read at least %d value(s), but %d were found", minimumReadCount, readCount);

        return value;
    }

    public void setMinimumReadCount(int minimumReadCount) {
        this.minimumReadCount = minimumReadCount;
    }
}
