package org.iokit.core.read;

import java.io.InputStream;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliteratorUnknownSize;

public abstract class OptionalReader<T> extends Reader<T> implements Iterable<T> {

    public OptionalReader(InputStream in) {
        super(in);
    }

    public final T read() throws ReaderException {
        return readOptional().orElseThrow(EndOfInputException::new);
    }

    public abstract Optional<T> readOptional() throws ReaderException;

    public Stream<T> stream() {
        return StreamSupport.stream(spliteratorUnknownSize(iterator(), NONNULL | ORDERED | IMMUTABLE), false);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private T nextValue = null;

            @Override
            public boolean hasNext() {
                if (nextValue != null)
                    return true;

                Optional<T> result = OptionalReader.this.readOptional();
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
        };
    }
}
