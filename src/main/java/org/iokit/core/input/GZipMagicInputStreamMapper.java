package org.iokit.core.input;

import java.util.zip.GZIPInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class GZipMagicInputStreamMapper implements MagicInputStreamMapper {

    @Override
    public boolean canMap(byte[] magic) {
        return magic.length >= 2 && magic[0] == (byte) 0x1f && magic[1] == (byte) 0x8b;
    }

    @Override
    public InputStream map(InputStream in) {
        if (in instanceof GZIPInputStream)
            return in;

        try {
            return new GZIPInputStream(in);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
