package org.iokit.core.write;

import org.iokit.core.LineTerminator;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class LineWriter extends Writer<String> {

    public static final LineTerminator DEFAULT_LINE_TERMINATOR = LineTerminator.CR_LF;

    private final LineTerminator terminator;

    public LineWriter(OutputStream out) {
        this(out, DEFAULT_LINE_TERMINATOR);
    }

    public LineWriter(OutputStream out, LineTerminator terminator) {
        super(out);
        this.terminator = terminator;
    }

    @Override
    public void write(String value) {
        Try.toRun(() -> {
            out.write(value.getBytes());
            writeNewline();
        });
    }

    public void writeNewline() { // TODO: rename to write()
        Try.toRun(() -> out.write(terminator.bytes));
    }
}
