package org.iokit.core.input;

import java.io.InputStream;

import static org.iokit.core.token.LineTerminator.CR_LF;

public class CrlfLineInputStream extends LineInputStream {

    public CrlfLineInputStream(InputStream in) {
        super(in, CR_LF);
    }
}
