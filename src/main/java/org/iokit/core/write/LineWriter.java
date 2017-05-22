package org.iokit.core.write;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class LineWriter extends Writer<String> {

    public LineWriter(OutputStream out) {
        super(out);
    }

    @Override
    public void write(String value) {
        Try.toRun(() -> {
            out.write(value.getBytes());
            writeNewLine();
        });
    }

    public void writeNewLine() {
        Try.toRun(() -> out.write("\r\n".getBytes()));
    }
}
