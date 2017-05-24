package org.iokit.warc;

import org.iokit.imf.Specials;

public class WarcSpecials implements Specials {

    @Override
    public char[] chars() {
        return new char[] { '(', ')', '@', ',', ';', ':', '\\', '/', '[', ']', '?', '=', '{', '}', ' ', '\t' };
    }
}
