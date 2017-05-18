package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConcatenationReader<T> implements Closeable, Reader<T> {

    private final LineInputStream input;
    private final Reader<T> valueReader;
    private final Reader<?> concatenatorReader;

    public ConcatenationReader(LineInputStream input, Reader<T> valueReader, Reader<?> concatenatorReader) {
        this.input = input;
        this.valueReader = valueReader;
        this.concatenatorReader = concatenatorReader;
    }

    public void seek(long offset) throws IOException {
        input.seek(offset);
    }

    public T read() throws ReaderException {
        if (input.isComplete())
            return null;

        T value = valueReader.read();
        concatenatorReader.read();

        return value;
    }

    public Stream<T> stream() {
        return StreamSupport.stream(new SequentialSpliterator(), false);
    }

    @Override
    public void close() {
        try {
            input.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }


    private class SequentialSpliterator implements Spliterator<T> {

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (input.isComplete())
                return false;

            try {
                action.accept(ConcatenationReader.this.read());
                return true;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        @Override
        public Spliterator<T> trySplit() {
            return null; // signifying this stream is not splittable
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE; // signifying unknown length
        }

        @Override
        public int characteristics() {
            return NONNULL | IMMUTABLE;
        }
    }
}
