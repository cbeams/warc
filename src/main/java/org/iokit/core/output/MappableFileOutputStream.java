package org.iokit.core.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MappableFileOutputStream extends FileOutputStream {

    private final File file;

    public MappableFileOutputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
