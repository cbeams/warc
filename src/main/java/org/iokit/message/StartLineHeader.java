package org.iokit.message;

import org.iokit.core.read.IOKitReader;

import java.util.function.BiFunction;

public abstract class StartLineHeader<SL, FS extends FieldSet> extends FieldSetHeader<FS> {

    protected final SL startLine;

    public StartLineHeader(SL startLine, FS fieldSet) {
        super(fieldSet);
        this.startLine = startLine;
    }


    public abstract static class Reader<SL, FS extends FieldSet, H extends StartLineHeader<SL, FS>>
        extends Header.Reader<H> {

        private final IOKitReader<SL> startLineReader;
        private final FieldSet.Reader<FS> fieldSetReader;
        private final BiFunction<SL, FS, H> headerFactory;

        public Reader(IOKitReader<SL> startLineReader,
                      FieldSet.Reader<FS> fieldSetReader,
                      BiFunction<SL, FS, H> headerFactory) {
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


    public abstract static class Writer<SL, FS extends FieldSet, H extends StartLineHeader<SL, FS>>
        extends Header.Writer<H> {

        private final org.iokit.core.write.Writer<SL> startLineWriter;
        private final FieldSet.Writer<FS> fieldSetWriter;

        public Writer(org.iokit.core.write.Writer<SL> startLineWriter,
                      FieldSet.Writer<FS> fieldSetWriter) {
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
