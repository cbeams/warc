package org.iokit.warc;

import org.iokit.imf.BinaryBody;

import org.iokit.core.read.ByteArrayReader;
import org.iokit.core.read.ParameterizedReader;
import org.iokit.core.read.ReaderException;

import org.iokit.lang.Try;

import java.io.InputStream;
import java.io.OutputStream;

public class WarcBody extends BinaryBody {

    public WarcBody(byte[] value) {
        super(value);
    }


    public static class Reader implements ParameterizedReader<WarcHeader, WarcBody> { // TODO: pull up to BinaryReader

        private final ByteArrayReader byteArrayReader; // TODO: // rename to BinaryReader

        public Reader(InputStream in) {
            this.byteArrayReader = new ByteArrayReader(in);
        }

        @Override
        public WarcBody read(WarcHeader header) throws ReaderException {
            return new WarcBody(byteArrayReader.read(header.getContentLength())); // TODO: call super.read(header.contentLength) on BinaryReader superclass (which just takes a long length to read)
        }
    }


    public static class Writer extends org.iokit.core.write.Writer<WarcBody> {

        public Writer(OutputStream out) {
            super(out);
        }

        public void write(WarcBody body) { // TODO: pull up to imf.BodyWriter / BinaryBodyWriter
            Try.toRun(() -> out.write(body.getData()));
        }
    }
}
