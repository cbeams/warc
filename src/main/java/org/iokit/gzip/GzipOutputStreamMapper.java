package org.iokit.gzip;

import org.iokit.magic.OutputStreamMapper;

import org.iokit.core.Try;

import java.io.File;
import java.io.OutputStream;

public class GzipOutputStreamMapper extends OutputStreamMapper {

    public static final String GZIP_FILE_SUFFIX = ".gz";

    @Override
    public boolean canMap(File file) {
        return file.getName().endsWith(GZIP_FILE_SUFFIX);
    }

    @Override
    public boolean canMap(Class<? extends OutputStream> type) {
        return SegmentableGzipOutputStream.class.isAssignableFrom(type);
    }

    @Override
    public SegmentableGzipOutputStream map(OutputStream out) {
        return out instanceof SegmentableGzipOutputStream ?
            (SegmentableGzipOutputStream) out :
            Try.toCall(() -> new SegmentableGzipOutputStream(out, 1024 * 1024));
    }
}
