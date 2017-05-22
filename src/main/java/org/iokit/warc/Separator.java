package org.iokit.warc;

public final class Separator { // TODO: rename to 'specials'?

    private Separator() {
    }

    private static final int[] CHARS = new int[]{
        '(', ')', '@', ',', ';', ':', '\\', '/', '[', ']', '?', '=', '{', '}', ' ', '\t' // TODO: Ascii
    };

    private static final CharSequence[] SEQUENCES = new CharSequence[]{
        "&lt;", "&gt;", "&lt;\"&gt"
    };

    public static boolean isSeparatorChar(int c) {
        for (int s : CHARS)
            if (c == s)
                return true;

        return false;
    }
}
