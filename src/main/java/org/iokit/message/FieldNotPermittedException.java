package org.iokit.message;

import org.iokit.lang.UncheckedException;

public class FieldNotPermittedException extends UncheckedException {

    public FieldNotPermittedException(Field field, FieldSet fieldSet) {
        super("Field %s is not permitted in FieldSet of type %s", field, fieldSet.getClass().getSimpleName());
    }
}
