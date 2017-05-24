package org.iokit.magic;

import java.io.File;
import java.io.OutputStream;

public interface OutputStreamMapper {

    boolean canMap(File file);

    boolean canMap(Class<? extends OutputStream> type);

    OutputStream map(OutputStream out);
}
