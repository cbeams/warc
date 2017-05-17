package org.iokit.warc.write;

import org.iokit.warc.WarcVersion;

import java.io.IOException;
import java.io.OutputStream;

public class WarcRecordVersionWriter {

    private final OutputStream output;

    public WarcRecordVersionWriter(OutputStream output) {
        this.output = output;
    }

    public void write(WarcVersion version) throws IOException {
        output.write(version.getValue().getBytes());
    }
}
