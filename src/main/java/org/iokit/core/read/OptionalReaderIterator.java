package org.iokit.core.read;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

class OptionalReaderIterator<T> implements Iterator<T> {

    private final OptionalReader<T> reader;

    private T nextValue = null;

    public OptionalReaderIterator(OptionalReader<T> reader) {
        this.reader = reader;
    }

    @Override
    public boolean hasNext() {
        if (nextValue != null)
            return true;

        Optional<T> result = reader.readOptional();
        if (result.isPresent()) {
            nextValue = result.get();
            return true;
        }
        return false;
    }

    @Override
    public T next() {
        if (nextValue != null || hasNext()) {
            T value = nextValue;
            nextValue = null;
            return value;
        }
        throw new NoSuchElementException();
    }
}
