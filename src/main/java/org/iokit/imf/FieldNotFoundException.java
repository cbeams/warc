package org.iokit.imf;

import org.iokit.lang.UncheckedException;

public class FieldNotFoundException extends UncheckedException {

    public FieldNotFoundException(FieldName name) {
        super(name.toString());
    }
}
