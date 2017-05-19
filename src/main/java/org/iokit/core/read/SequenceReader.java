package org.iokit.core.read;

import java.util.Iterator;
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