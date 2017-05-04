package org.iokit.warc;

import org.iokit.imf.MessageBody;

public class WarcRecordBody implements MessageBody<byte[]> {

    private final byte[] value;

    public WarcRecordBody(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        return value;
    }
}
