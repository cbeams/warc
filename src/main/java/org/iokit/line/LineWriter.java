package org.iokit.line;

import org.iokit.core.IOKitOutputStream;
import org.iokit.core.IOKitWriter;
import org.iokit.core.LineTerminator;
import org.iokit.core.Try;

public class LineWriter extends IOKitWriter<String> {

    private final LineTerminator terminator;

    public LineWriter(IOKitOutputStream out) {
        this(out, LineTerminator.systemValue());
    }

    public LineWriter(IOKitOutputStream out, LineTerminator terminator) {
        super(out);
        this.terminator = terminator;
    }

    @Override
    public void write(String value) {
        Try.toRun(() -> out.write(value.getBytes()));
        write();
    }

    public void write() {
        Try.toRun(() -> out.write(terminator.bytes));
    }
}
