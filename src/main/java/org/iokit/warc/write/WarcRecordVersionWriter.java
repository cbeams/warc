package org.iokit.warc.write;

import org.iokit.warc.WarcRecordVersion;

import java.io.IOException;
import java.io.OutputStream;

public class WarcRecordVersionWriter {

    private final OutputStream output;

    public WarcRecordVersionWriter(OutputStream output) {
        this.output = output;
    }

    public void write(WarcRecordVersion version) throws IOException {
        output.write(version.getValue().getBytes());
    }
}
