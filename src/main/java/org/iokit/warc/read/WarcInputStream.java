package org.iokit.warc.read;

import org.iokit.core.input.CompletionAwareLineInputStream;
import org.iokit.core.input.LineTerminator;

import java.io.InputStream;

import static org.iokit.core.input.StandardLineTerminator.CR_LF;

public class WarcInputStream extends CompletionAwareLineInputStream {

    public WarcInputStream(InputStream in) {
        this(in, CR_LF);
    }

    public WarcInputStream(InputStream in, LineTerminator... terminators) {
        super(in, terminators);
    }
}
