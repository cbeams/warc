package org.iokit.core.read;

import java.io.InputStream;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliteratorUnknownSize;

public abstract class OptionalReader<V> extends IOKitReader<V> implements Iterable<V> {

    public OptionalReader(InputStream in) {
        super(in);
    }

    public final V read() {
        return readOptional().orElseThrow(EndOfInputException::new);
    }

    public abstract Optional<V> readOptional();

    public Stream<V> stream() {
        return StreamSupport.stream(spliteratorUnknownSize(iterator(), NONNULL | ORDERED | IMMUTABLE), false);
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {

            private V nextValue = null;

            @Override
            public boolean hasNext() {
                if (nextValue != null)
                    return true;

                Optional<V> result = OptionalReader.this.readOptional();
                if (result.isPresent()) {
                    nextValue = result.get();
                    return true;
                }
                return false;
            }

            @Override
            public V next() {
                if (nextValue != null || hasNext()) {
                    V value = nextValue;
                    nextValue = null;
                    return value;
                }
                throw new NoSuchElementException();
            }
        };
    }
}
