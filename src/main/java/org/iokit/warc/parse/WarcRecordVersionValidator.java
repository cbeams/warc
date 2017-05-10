package org.iokit.warc.parse;

import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import static org.iokit.warc.WarcRecordVersion.*;

public class WarcRecordVersionValidator extends Validator {

    @Override
    public void validate(String input) throws ValidatorException {
        if (!this.isEnabled())
            return;

        if (!WARC_1_0.equals(input) && !WARC_1_1.equals(input))
            throw new ValidatorException("[%s] is an unsupported or otherwise malformed WARC record version", input);
    }
}
