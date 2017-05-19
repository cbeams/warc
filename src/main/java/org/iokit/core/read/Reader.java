package org.iokit.core.read;

import org.iokit.core.input.Input;

import org.iokit.lang.Try;

import java.io.Closeable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;

public abstract class Reader<T> implements Closeable {

    protected final Input input;

    public Reader(Input input) {
        this.input = input;
    }

    public T read() throws ReaderException {
        return readOptional().orElseThrow(EndOfInputException::new);
    }

    public abstract Optional<T> readOptional() throws ReaderException;

    public Stream<T> stream() {
        Iterator<T> iterator = new Iterator<T>() {
            T nextValue = null;

            @Override
            public boolean hasNext() {
                if (nextValue != null)
                    return true;

                Optional<T> result = Reader.this.readOptional();
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

    public Input getInput() {
        return input;
    }

    @Override
    public void close() {
        Try.toRun(input::close);
    }
}
