package org.iokit.warc;

import org.iokit.line.LineReader;
import org.iokit.line.LineWriter;
import org.iokit.line.NewlineReader;

import org.iokit.core.read.ReaderException;

public class WarcConcatenator {

    public static final int NEWLINE_COUNT = 2;


    public static class Reader extends org.iokit.core.read.Reader<Boolean> {

        private final NewlineReader newlineReader;

        public Reader(LineReader lineReader) {
            this(new NewlineReader(lineReader));
        }

        public Reader(NewlineReader newlineReader) {
            super(newlineReader.in);
            this.newlineReader = newlineReader;
        }

        @Override
        public Boolean read() throws ReaderException {
            for (int i = 0; i < NEWLINE_COUNT; i++)
                if (!newlineReader.readOptional().isPresent())
                    return false;

            return true;
        }
    }


    public static class Writer extends org.iokit.core.write.Writer<Void> {

        private final LineWriter lineWriter;

        public Writer(LineWriter lineWriter) {
            super(lineWriter.out);
            this.lineWriter = lineWriter;
        }

        public void write(Void value) { // TODO: write() variant
            for (int i = 0; i < NEWLINE_COUNT; i++)
                lineWriter.write();
        }
    }
}
