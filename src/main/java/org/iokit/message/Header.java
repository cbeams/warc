package org.iokit.message;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitReader;
import org.iokit.core.IOKitWriter;

import java.io.OutputStream;

public interface Header {


    abstract class Reader<H extends Header> extends IOKitReader<H> {

        public Reader(IOKitInputStream in) {
            super(in);
        }
    }


    abstract class Writer<H extends Header> extends IOKitWriter<H> {

        public Writer(OutputStream out) {
            super(out);
        }
    }
}
