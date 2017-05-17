package org.iokit.core.input;

import java.util.zip.GZIPInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public final class InputStreams {

    private InputStreams() {
    }

    public static InputStream decodeStreamIfNecessary(InputStream input) throws IOException {
        if (input instanceof GZIPInputStream)
            return input;

        PushbackInputStream pb = new PushbackInputStream(input, 2);
        byte[] magic = new byte[2];
        int len = pb.read(magic);

        if (len < magic.length)
            return pb;

        pb.unread(magic, 0, len);

        if (magic[0] == (byte) 0x1f && magic[1] == (byte) 0x8b) // GZip magic number
            return new GZIPInputStream(pb);

        return pb;
    }
}
