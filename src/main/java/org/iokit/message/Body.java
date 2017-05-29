package org.iokit.message;

import org.iokit.core.write.IOKitWriter;

import org.iokit.core.read.ParameterizedReader;

import java.io.InputStream;
import java.io.OutputStream;

public interface Body<Data> {

    Data getData();


    abstract class Reader<H, B> extends ParameterizedReader<H, B> {

        public Reader(InputStream in) {
            super(in);
        }

        @Override
        public abstract B read(H header);
    }


    abstract class Writer<B> extends IOKitWriter<B> {

        public Writer(OutputStream out) {
            super(out);
        }
    }
}
