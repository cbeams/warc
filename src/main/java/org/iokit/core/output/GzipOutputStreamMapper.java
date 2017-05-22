package org.iokit.core.output;

import org.iokit.lang.Try;

import org.anarres.parallelgzip.ParallelGZIPOutputStream;

import java.util.zip.GZIPOutputStream;

import java.io.File;
import java.io.OutputStream;

public class GzipOutputStreamMapper implements OutputStreamMapper {

    public static final String GZIP_FILE_SUFFIX = ".gz";

    @Override
    public boolean canMap(File file) {
        return file.getName().endsWith(GZIP_FILE_SUFFIX);
    }

    @Override
    public boolean canMap(Class<? extends OutputStream> type) {
        return GZIPOutputStream.class.isAssignableFrom(type)
            || ParallelGZIPOutputStream.class.isAssignableFrom(type);
    }

    @Override
    public OutputStream map(OutputStream out) {
        return out instanceof GZIPOutputStream
            || out instanceof ParallelGZIPOutputStream ? out : Try.toCall(() -> new ParallelGZIPOutputStream(out));
    }
}
