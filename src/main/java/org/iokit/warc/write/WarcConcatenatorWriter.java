package org.iokit.warc.write;

import org.iokit.core.write.LineWriter;
import org.iokit.core.write.Writer;

public class WarcConcatenatorWriter extends Writer<Void> {

    private final LineWriter lineWriter;

    public WarcConcatenatorWriter(LineWriter lineWriter) {
        super(lineWriter.out);
        this.lineWriter = lineWriter;
    }

    public void write(Void value) {
        lineWriter.write();
        lineWriter.write();
    }
}
