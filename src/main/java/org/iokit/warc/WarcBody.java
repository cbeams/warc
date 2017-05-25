package org.iokit.warc;

import org.iokit.imf.BinaryBody;

import org.iokit.core.read.ReaderException;

import java.io.InputStream;
import java.io.OutputStream;

public class WarcBody extends BinaryBody {

    public WarcBody(byte[] value) {
        super(value);
    }


    public static class Reader extends BinaryBody.Reader<WarcHeader, WarcBody> {

        public Reader(InputStream in) {
            super(in);
        }

        @Override
        public WarcBody read(WarcHeader header) throws ReaderException {
            return new WarcBody(binaryReader.read(header.getContentLength()));
        }
    }


    public static class Writer extends BinaryBody.Writer<WarcBody> {

        public Writer(OutputStream out) {
            super(out);
        }
    }
}
