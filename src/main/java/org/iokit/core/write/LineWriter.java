package org.iokit.core.write;

import org.iokit.lang.Try;

import java.io.OutputStream;

public class LineWriter extends Writer<String> {

    public LineWriter(OutputStream output) {
        super(output);
    }

    @Override
    public void write(String value) {
        Try.toRun(() -> {
            output.write(value.getBytes());
            writeNewLine();
        });
    }

    public void writeNewLine() {
        Try.toRun(() -> output.write("\r\n".getBytes()));
    }
}
