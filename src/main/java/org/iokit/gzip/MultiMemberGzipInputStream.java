package org.iokit.gzip;

import org.iokit.core.IOKitInputStream;
import org.iokit.core.Try;

import java.util.zip.GZIPInputStream;

import java.io.InputStream;

public class MultiMemberGzipInputStream extends IOKitInputStream {

    public MultiMemberGzipInputStream(GZIPInputStream in, InputStream raw) {
        super(in, raw);
    }


    public static class Adapter implements IOKitInputStream.Adapter {

        @Override
        public boolean canAdapt(byte[] magic) {
            return magic.length >= 2 && magic[0] == (byte) 0x1f && magic[1] == (byte) 0x8b;
        }

        @Override
        public MultiMemberGzipInputStream adapt(InputStream raw) {
            GZIPInputStream in =
                (raw instanceof GZIPInputStream) ?
                    (GZIPInputStream) raw :
                    Try.toCall(() -> new GZIPInputStream(raw, 1024 * 1024));

            return new MultiMemberGzipInputStream(in, raw);
        }
    }
}
