package org.iokit.core.read;

import java.io.InputStream;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliteratorUnknownSize;

public abstract class OptionalReader<T> extends Reader<T> {

    public OptionalReader(InputStream in) {
        super(in);
    }

    public final T read() throws ReaderException {
        return readOptional().orElseThrow(EndOfInputException::new);
    }

    public abstract Optional<T> readOptional() throws ReaderException;

    public Stream<T> stream() {
        return StreamSupport.stream(
            spliteratorUnknownSize(new OptionalReaderIterator<>(this), NONNULL | ORDERED | IMMUTABLE),
            false);
    }
}
