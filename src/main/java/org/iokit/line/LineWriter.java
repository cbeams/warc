package org.iokit.line;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.IOKitWriter;

public class LineWriter extends IOKitWriter<String> {

    public LineWriter(IOKitOutputStream out) {
        super(out);
    }

    @Override
    public void write(String value) {
        out.writeLine(value.getBytes());
    }

    public void write() {
        out.writeLine();
    }
}
