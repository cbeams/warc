package org.iokit.core.read;

import org.iokit.core.input.LineInputStream;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SequentialReader<T> implements Closeable, Reader<T> {

    private final LineInputStream input;
    private final Reader<T> reader;
    private final Reader<Void> separatorReader;

    public SequentialReader(LineInputStream input, Reader<T> reader, Reader<Void> separatorReader) {
        this.input = input;
        this.reader = reader;
        this.separatorReader = separatorReader;
    }

    public void seek(long offset) throws IOException {
        input.seek(offset);
    }

    public T read() throws ReaderException, EOFException {
        if (input.isComplete())
            return null;

        T value = reader.read();
        separatorReader.read();

        return value;
    }

    public Stream<T> stream() {
        return StreamSupport.stream(new SequentialSpliterator(), false);
    }

    @Override
    public void close() throws IOException {
        input.close();
    }


    private class SequentialSpliterator implements Spliterator<T> {

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (input.isComplete())
                return false;

            try {
                action.accept(SequentialReader.this.read());
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
