package org.iokit.warc.read;

import org.iokit.core.input.LineInputStream;

import java.io.InputStream;

import static org.iokit.core.token.LineTerminator.CR_LF;

public class WarcInputStream extends LineInputStream {

    public WarcInputStream(InputStream in) {
        super(in, CR_LF);
    }
}
