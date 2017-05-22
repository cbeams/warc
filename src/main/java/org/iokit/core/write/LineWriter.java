package org.iokit.core.write;

import org.iokit.core.LineTerminator;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class LineWriter extends Writer<String> {

    private final LineTerminator terminator;

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
