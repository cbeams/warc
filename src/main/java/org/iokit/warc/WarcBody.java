package org.iokit.warc;

import org.iokit.imf.Body;

public class WarcBody implements Body<byte[]> { // TODO: pull up to ByteArrayBody?

    private final byte[] value;

    public WarcBody(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        return value;
    }
}
