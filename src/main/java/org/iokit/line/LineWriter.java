package org.iokit.line;

import org.iokit.core.write.IOKitWriter;

import org.iokit.core.Try;

import java.io.OutputStream;

public class LineWriter extends IOKitWriter<String> {

    private final LineTerminator terminator;

    public LineWriter(OutputStream out) {
        this(out, LineTerminator.systemValue());
    }

    public LineWriter(OutputStream out, LineTerminator terminator) {
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
