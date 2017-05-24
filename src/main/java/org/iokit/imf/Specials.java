package org.iokit.imf;

public interface Specials {

    Specials NONE = () -> new char[0];

    char[] chars();

    default boolean isSpecial(int ch) {
        for (int sp : chars())
            if (ch == sp)
                return true;

        return false;
    }
}
