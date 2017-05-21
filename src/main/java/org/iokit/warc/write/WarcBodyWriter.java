package org.iokit.warc.write;

import org.iokit.warc.WarcBody;

import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class WarcBodyWriter extends Writer<WarcBody> {

    private final OutputStream output;

    public WarcBodyWriter(OutputStream output) {
        this.output = output;
    }

    public void write(WarcBody body) {
        Try.toRun(() -> output.write(body.getData()));
    }
}
