package org.iokit.warc.write;

import org.iokit.warc.WarcBody;

import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class WarcBodyWriter extends Writer<WarcBody> {

    public WarcBodyWriter(OutputStream output) {
        super(output);
    }

    public void write(WarcBody body) {
        Try.toRun(() -> output.write(body.getData()));
    }
}
