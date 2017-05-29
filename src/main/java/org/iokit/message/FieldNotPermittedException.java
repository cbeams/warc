package org.iokit.message;

import org.iokit.core.IOKitException;

public class FieldNotPermittedException extends IOKitException {

    public FieldNotPermittedException(Field field, FieldSet fieldSet) {
        super("Field %s is not permitted in FieldSet of type %s", field, fieldSet.getClass().getSimpleName());
    }
}
