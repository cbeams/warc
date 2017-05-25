package org.iokit.imf;

import java.util.function.BiFunction;

public abstract class StartLineHeader<S, F extends FieldSet> extends FieldSetHeader<F> { // TODO: SL, FS vs S F for clarity. Do it everywhere

    protected final S startLine;

    public StartLineHeader(S startLine, F fieldSet) {
        super(fieldSet);
        this.startLine = startLine;
    }

    public abstract static class Reader<S, F extends FieldSet, H extends StartLineHeader<S, F>> extends org.iokit.core.read.Reader<H> {

        private final org.iokit.core.read.Reader<S> startLineReader;
        private final FieldSet.Reader<F> fieldSetReader;
        private final BiFunction<S, F, H> headerFactory;

        public Reader(org.iokit.core.read.Reader<S> startLineReader,
                      FieldSet.Reader<F> fieldSetReader,
                      BiFunction<S, F, H> headerFactory) {
            super(startLineReader.in);
            this.startLineReader = startLineReader;
            this.fieldSetReader = fieldSetReader;
            this.headerFactory = headerFactory;
        }

        @Override
        public H read() {
            return headerFactory.apply(startLineReader.read(), fieldSetReader.read());
        }
    }


    public abstract static class Writer<S, F extends FieldSet, H extends StartLineHeader<S, F>> extends org.iokit.core.write.Writer<H> {

        private final org.iokit.core.write.Writer<S> startLineWriter;
        private final FieldSet.Writer fieldSetWriter;

        public Writer(org.iokit.core.write.Writer<S> startLineWriter,
                      FieldSet.Writer fieldSetWriter) {
            super(startLineWriter.out);
            this.startLineWriter = startLineWriter;
            this.fieldSetWriter = fieldSetWriter;
        }

        @Override
        public void write(H header) {
            startLineWriter.write(header.startLine);
            fieldSetWriter.write(header.fieldSet);
        }
    }
}
