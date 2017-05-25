package org.iokit.warc;

import org.iokit.message.Specials;

import static org.iokit.coding.Ascii.*;

public class WarcSpecials implements Specials {

    public static final char[] CHARS = new char[]{
        LEFT_PARENTHESIS, RIGHT_PARENTHESIS, AT_SIGN, COMMA, SEMICOLON, COLON, BACKSLASH, SLASH, LEFT_SQUARE_BRACKET,
        RIGHT_SQUARE_BRACKET, QUESTION_MARK, EQUALS_SIGN, LEFT_CURLY_BRACKET, RIGHT_CURLY_BRACKET, SPACE, TAB
    };

    @Override
    public char[] chars() {
        return CHARS;
    }
}
