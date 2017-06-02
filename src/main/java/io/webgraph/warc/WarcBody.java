package io.webgraph.warc;

import org.iokit.message.BinaryBody;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitOutputStream;

public class WarcBody extends BinaryBody {

    public WarcBody(byte[] value) {
        super(value);
    }


    public static class Reader extends BinaryBody.Reader<WarcHeader, WarcBody> {

        public Reader(IOKitInputStream in) {
            super(in);
        }

        @Override
        public WarcBody read(WarcHeader header) {
            return new WarcBody(binaryReader.read(header.getContentLength()));
        }
    }


    public static class Writer extends BinaryBody.Writer<WarcBody> {

        public Writer(IOKitOutputStream out) {
            super(out);
        }
    }
}
