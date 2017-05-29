package org.iokit.message;

import org.iokit.core.IOKitException;

public class FieldNotFoundException extends IOKitException {

    public FieldNotFoundException(FieldName name) {
        super(name.toString());
    }
}
