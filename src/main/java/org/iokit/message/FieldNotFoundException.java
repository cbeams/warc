package org.iokit.message;

import org.iokit.lang.UncheckedException;

public class FieldNotFoundException extends UncheckedException {

    public FieldNotFoundException(FieldName name) {
        super(name.toString());
    }
}
