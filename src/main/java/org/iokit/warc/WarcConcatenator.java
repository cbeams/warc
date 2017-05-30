package org.iokit.warc;

import org.iokit.general.LineReader;
import org.iokit.general.LineWriter;
import org.iokit.general.NewlineReader;
import org.iokit.general.ParameterlessWriter;

import org.iokit.core.IOKitReader;

public class WarcConcatenator {

    public static final int NEWLINE_COUNT = 2;


    public static class Reader extends IOKitReader<Boolean> {

        private final NewlineReader newlineReader;

        public Reader(LineReader lineReader) {
            this(new NewlineReader(lineReader));
        }

        public Reader(NewlineReader newlineReader) {
            super(newlineReader.in);
            this.newlineReader = newlineReader;
        }

        @Override
        public Boolean read() {
            for (int i = 0; i < NEWLINE_COUNT; i++)
                if (!newlineReader.readOptional().isPresent())
                    return false;

            return true;
        }
    }


    public static class Writer extends ParameterlessWriter {

        private final LineWriter lineWriter;

        public Writer(LineWriter lineWriter) {
            super(lineWriter.out);
            this.lineWriter = lineWriter;
        }

        @Override
        public void write() {
            for (int i = 0; i < NEWLINE_COUNT; i++)
                lineWriter.write();
        }
    }
}
