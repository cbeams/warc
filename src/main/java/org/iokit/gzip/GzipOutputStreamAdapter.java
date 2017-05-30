package org.iokit.gzip;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.Try;

import java.io.File;
import java.io.OutputStream;

public class GzipOutputStreamAdapter extends IOKitOutputStream.Adapter {

    public static final String GZIP_FILE_SUFFIX = ".gz";

    @Override
    public boolean canAdapt(File file) {
        return file.getName().endsWith(GZIP_FILE_SUFFIX);
    }

    @Override
    public boolean canAdapt(Class<? extends OutputStream> type) {
        return SegmentableGzipOutputStream.class.isAssignableFrom(type);
    }

    @Override
    public SegmentableGzipOutputStream adapt(OutputStream out) {
        return out instanceof SegmentableGzipOutputStream ?
            (SegmentableGzipOutputStream) out :
            Try.toCall(() -> new SegmentableGzipOutputStream(out, 1024 * 1024));
    }
}
