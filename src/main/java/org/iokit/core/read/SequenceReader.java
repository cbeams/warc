package org.iokit.core.read;

import org.iokit.core.input.Input;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;

public class SequenceReader<T> extends InputReader<Input, T> {

    private final InputReader<?, T> reader;

    public SequenceReader(InputReader<?, T> reader) {
        super(reader.getInput());
        this.reader = reader;
    }

    @Override
    public T read() throws ReaderException {
        return reader.getInput().isComplete() ? null : reader.read();
    }

    public void seek(long offset) {
        getInput().seek(offset);
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
                        return SequenceReader.this.read();
                    }
                }, NONNULL | ORDERED | IMMUTABLE),
            false);
    }
}
