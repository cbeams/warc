package org.iokit.imf;

import org.iokit.core.write.LineWriter;

import org.iokit.core.read.ReaderException;

import java.util.function.BiFunction;

public abstract class StartLineHeader<T> extends Header {

    protected final T startLine;

    public StartLineHeader(T startLine, FieldSet fieldSet) {
        super(fieldSet);
        this.startLine = startLine;
    }

    public abstract static class Reader<S, H extends StartLineHeader<S>> extends org.iokit.core.read.Reader<H> {

        private final org.iokit.core.read.Reader<S> startLineReader;
        private final FieldSet.Reader fieldSetReader;
        private final BiFunction<S, FieldSet, H> headerFactory;

        public Reader(org.iokit.core.read.Reader<S> startLineReader,
                      FieldSet.Reader fieldSetReader,
                      BiFunction<S, FieldSet, H> headerFactory) {
            super(startLineReader.in);
            this.startLineReader = startLineReader;
            this.fieldSetReader = fieldSetReader;
            this.headerFactory = headerFactory;
        }

        @Override
        public H read() throws ReaderException {
            return headerFactory.apply(startLineReader.read(), fieldSetReader.read());
        }
    }


    public abstract static class Writer<S, H extends StartLineHeader<S>> extends org.iokit.core.write.Writer<H> {

        private final org.iokit.core.write.Writer<S> startLineWriter;
        private final FieldSet.Writer fieldSetWriter;
        private final LineWriter lineWriter;

        public Writer(org.iokit.core.write.Writer<S> startLineWriter,
                      FieldSet.Writer fieldSetWriter,
                      LineWriter lineWriter) {
            super(startLineWriter.out);
            this.startLineWriter = startLineWriter;
            this.fieldSetWriter = fieldSetWriter;
            this.lineWriter = lineWriter;
        }

        @Override
        public void write(H header) {
            startLineWriter.write(header.startLine);
            fieldSetWriter.write(header.getFieldSet());
            lineWriter.write();
        }
    }
}
