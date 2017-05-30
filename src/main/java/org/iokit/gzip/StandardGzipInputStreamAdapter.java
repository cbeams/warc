package org.iokit.gzip;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.Try;

import java.util.zip.GZIPInputStream;

import java.io.InputStream;

public class StandardGzipInputStreamAdapter extends IOKitInputStream.Adapter {

    @Override
    public boolean canAdapt(byte[] magic) {
        return magic.length >= 2 && magic[0] == (byte) 0x1f && magic[1] == (byte) 0x8b;
    }

    @Override
    public IOKitInputStream adapt(InputStream raw) {
        InputStream in =
            (raw instanceof GZIPInputStream) ?
                raw :
                Try.toCall(() -> new GZIPInputStream(raw, 1024 * 1024));

        return new IOKitInputStream(in, raw);
    }
}
