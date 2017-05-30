package org.iokit.gzip;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.Try;

import java.io.File;
import java.io.OutputStream;

public class MultiMemberGzipOutputStreamAdapter extends IOKitOutputStream.Adapter {

    public static final String GZIP_FILE_SUFFIX = ".gz";

    @Override
    public boolean canAdapt(File file) {
        return file.getName().endsWith(GZIP_FILE_SUFFIX);
    }

    @Override
    public boolean canAdapt(Class<? extends OutputStream> type) {
        return MultiMemberGzipOutputStream.class.isAssignableFrom(type);
    }

    @Override
    public MultiMemberGzipOutputStream adapt(OutputStream out) {
        return out instanceof MultiMemberGzipOutputStream ?
            (MultiMemberGzipOutputStream) out :
            Try.toCall(() -> new MultiMemberGzipOutputStream(out, 1024 * 1024));
    }
}
