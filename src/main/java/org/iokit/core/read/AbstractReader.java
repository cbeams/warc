package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;

public abstract class AbstractReader<T> implements Closeable, Reader<T> {

    private final LineInputStream input;

    public AbstractReader(LineInputStream input) {
        this.input = input;
    }

    public Stream<T> stream() {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return !input.isComplete();
                    }

                    @Override
                    public T next() {
                        return AbstractReader.this.read();
                    }
                }, NONNULL | ORDERED | IMMUTABLE),
            false);
    }

    @Override
    public void close() {
        try {
            input.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
