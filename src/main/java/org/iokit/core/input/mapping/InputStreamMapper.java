package org.iokit.core.input.mapping;

import java.io.InputStream;

public interface InputStreamMapper {

    boolean canMap(byte[] magic);

    InputStream map(InputStream in);
}
