package org.iokit.core.read;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;

public class SequenceReader<T> extends InputReader<T> implements OptionalReader<T> {

    private final Reader<T> reader;

    public SequenceReader(Reader<T> reader) {
        super(reader.getInput());
        this.reader = reader;
    }

    @Override
    public T read() throws ReaderException {
        return readOptional().orElseThrow(() -> new ReaderException("TODO"));
    }

    @Override
    public Optional<T> readOptional() throws ReaderException {
        return input.isComplete() ? Optional.empty() : Optional.of(reader.read());
    }

    public Stream<T> stream() {
        Iterator<T> iterator = new Iterator<T>() {
            T nextValue = null;

            @Override
            public boolean hasNext() {
                if (nextValue != null)
                    return true;

                Optional<T> result = SequenceReader.this.readOptional();
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
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, NONNULL | ORDERED | IMMUTABLE),
            false);
    }
}
