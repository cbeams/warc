package org.iokit.message;

import org.iokit.core.read.ParameterizedReader;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitOutputStream;
import org.iokit.core.IOKitWriter;

public interface Body<Data> {

    Data getData();


    abstract class Reader<H, B> extends ParameterizedReader<H, B> {

        public Reader(IOKitInputStream in) {
            super(in);
        }

        @Override
        public abstract B read(H header);
    }


    abstract class Writer<B> extends IOKitWriter<B> {

        public Writer(IOKitOutputStream out) {
            super(out);
        }
    }
}
