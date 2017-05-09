package org.iokit.warc.read;

import org.iokit.core.input.LineInputStream;

import org.iokit.core.token.LineTerminator;

import java.io.InputStream;

import static org.iokit.core.token.LineTerminator.CR_LF;

public class WarcInputStream extends LineInputStream {

    public WarcInputStream(InputStream in) {
        this(in, CR_LF);
    }

    public WarcInputStream(InputStream in, LineTerminator... terminators) {
        super(in, terminators);
    }
}
