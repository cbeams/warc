package org.iokit.line;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.IOKitWriter;
import org.iokit.core.Try;

public class LineWriter extends IOKitWriter<String> {

    public LineWriter(IOKitOutputStream out) {
        super(out);
    }

    @Override
    public void write(String value) {
        Try.toRun(() -> out.write(value.getBytes()));
        write();
    }

    public void write() {
        Try.toRun(() -> out.write(out.terminator.bytes));
    }
}
