package org.iokit.core.read;

import org.iokit.core.input.Input;

import java.io.Closeable;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;

public abstract class AbstractReader<T> implements Closeable, Reader<T> {

    protected final Input input;

    public AbstractReader(Input input) {
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

    public void seek(long offset) {
        input.seek(offset);
    }

    @Override
    public void close() {
        input.close();
    }
}
