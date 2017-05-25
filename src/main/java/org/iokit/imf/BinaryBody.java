package org.iokit.imf;

public class BinaryBody implements Body<byte[]> {

    protected final byte[] value;

    public BinaryBody(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        return value;
    }
}
