package org.iokit.warc;

import org.iokit.core.write.LineWriter;

import org.iokit.core.read.LineReader;
import org.iokit.core.read.NewlineReader;
import org.iokit.core.read.ReaderException;

public class WarcConcatenator {

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
            for (int i = 0; i < 2; i++) // TODO: extract constant for newline count
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

        public void write(Void value) {
            lineWriter.write(); // TODO: extract Warc(Concatenator?).NEWLINE_COUNT
            lineWriter.write();
        }
    }
}
