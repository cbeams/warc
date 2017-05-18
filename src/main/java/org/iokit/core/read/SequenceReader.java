package org.iokit.core.read;

import org.iokit.core.input.Input;

import java.io.Closeable;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;

public class SequenceReader<T> implements Closeable, Reader<T> {

    private final Reader<T> reader;

    public SequenceReader(Reader<T> reader) {
        this.reader = reader;
    }

    @Override
    public T read() throws ReaderException {
        return reader.getInput().isComplete() ? null : reader.read();
    }

    public void seek(long offset) {
        getInput().seek(offset);
    }

    @Override
    public void close() {
        getInput().close();
    }

    @Override
    public Input getInput() {
        return reader.getInput();
    }

    public Stream<T> stream() {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return !getInput().isComplete();
                    }

                    @Override
                    public T next() {
                        return SequenceReader.this.read();
                    }
                }, NONNULL | ORDERED | IMMUTABLE),
            false);
    }
}
