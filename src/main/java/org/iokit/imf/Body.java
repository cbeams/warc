package org.iokit.imf;

import org.iokit.core.read.ParameterizedReader;

import java.io.OutputStream;

public interface Body<Data> {

    Data getData();


    abstract class Reader<H, B> implements ParameterizedReader<H, B> {

        @Override
        public abstract B read(H header);
    }


    abstract class Writer<B> extends org.iokit.core.write.Writer<B> {

        public Writer(OutputStream out) {
            super(out);
        }
    }
}
