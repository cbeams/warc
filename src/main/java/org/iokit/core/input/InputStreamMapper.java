package org.iokit.core.input;

import java.io.InputStream;

public interface InputStreamMapper {

    boolean canMap(byte[] magic);

    InputStream map(InputStream in);
}
