package org.iokit.imf;

import org.iokit.core.read.BinaryReader;
import org.iokit.core.read.ParameterizedReader;

import org.iokit.lang.Try;

import java.io.InputStream;
import java.io.OutputStream;

public class BinaryBody implements Body<byte[]> {

    protected final byte[] value;

    public BinaryBody(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        return value;
    }


    public abstract static class Reader<H extends Header, B extends BinaryBody> implements ParameterizedReader<H, B> {

        protected final BinaryReader binaryReader;

        public Reader(InputStream in) {
            this.binaryReader = new BinaryReader(in);
        }
    }


    public static class Writer<B extends BinaryBody> extends org.iokit.core.write.Writer<B> {

        public Writer(OutputStream out) {
            super(out);
        }

        public void write(B body) {
            Try.toRun(() -> out.write(body.getData()));
        }
    }
}
