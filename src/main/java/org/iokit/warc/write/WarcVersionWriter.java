package org.iokit.warc.write;

import org.iokit.warc.WarcVersion;

import org.iokit.core.write.Writer;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class WarcVersionWriter extends Writer<WarcVersion> {

    private final OutputStream output;

    public WarcVersionWriter(OutputStream output) {
        this.output = output;
    }

    public void write(WarcVersion version) {
        Try.toRun(() -> output.write(version.getValue().getBytes()));
    }
}
