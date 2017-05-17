package org.iokit.warc;

import org.iokit.imf.Body;

public class WarcBody implements Body<byte[]> {

    private final byte[] value;

    public WarcBody(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        return value;
    }
}
