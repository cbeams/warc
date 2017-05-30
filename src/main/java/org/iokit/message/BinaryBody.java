package org.iokit.message;

import org.iokit.general.BinaryReader;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.IOKitOutputStream;
import org.iokit.core.Try;

public class BinaryBody implements Body<byte[]> {

    protected final byte[] value;

    public BinaryBody(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        return value;
    }


    public abstract static class Reader<H extends Header, B extends BinaryBody> extends Body.Reader<H, B> {

        protected final BinaryReader binaryReader;

        public Reader(IOKitInputStream in) {
            super(in);
            this.binaryReader = new BinaryReader(in);
        }
    }


    public static class Writer<B extends BinaryBody> extends Body.Writer<B> {

        public Writer(IOKitOutputStream out) {
            super(out);
        }

        public void write(B body) {
            Try.toRun(() -> out.write(body.getData()));
        }
    }
}
