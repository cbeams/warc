package org.iokit.message;

import org.iokit.core.write.IOKitWriter;

import org.iokit.core.read.IOKitReader;

import org.iokit.core.IOKitInputStream;

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
