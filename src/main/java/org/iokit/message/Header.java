package org.iokit.message;

import java.io.InputStream;
import java.io.OutputStream;

public interface Header {


    abstract class Reader<H extends Header> extends org.iokit.core.read.Reader<H> {

        public Reader(InputStream in) {
            super(in);
        }
    }


    abstract class Writer<H extends Header> extends org.iokit.core.write.Writer<H> {

        public Writer(OutputStream out) {
            super(out);
        }
    }
}
