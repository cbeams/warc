package org.iokit.line;

import org.iokit.core.IOKitOutputStream;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.iokit.core.IOKitInputStream.LineTerminator.*;

public class LineWriterSpec {

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Test
    public void writeLineTerminatedByCrlf() {
        new LineWriter(new IOKitOutputStream(out), CR_LF).write();
        assertThat(out.toByteArray()).isEqualTo(CR_LF.bytes);
    }

    @Test
    public void writeLineTerminatedByLf() {
        new LineWriter(new IOKitOutputStream(out), LF).write();
        assertThat(out.toByteArray()).isEqualTo(LF.bytes);
    }
}
