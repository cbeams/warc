package org.iokit.line;

import org.iokit.core.LineTerminator;

import org.junit.After;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.iokit.core.LineTerminator.*;

public class LineTerminatorSpec {

    private static final String DEFAULT_SYSTEM_LINE_TERMINATOR_VALUE = System.getProperty(SYSTEM_LINE_TERMINATOR_KEY);

    @Test
    public void parse() {
        assertThat(LineTerminator.parseValue("\r")).isEqualTo(CR);
        assertThat(LineTerminator.parseValue("\n")).isEqualTo(LF);
        assertThat(LineTerminator.parseValue("\r\n")).isEqualTo(CR_LF);
        assertThatThrownBy(() ->
            LineTerminator.parseValue("\t")
        ).hasMessage("No LineTerminator found matching [\t]");
    }

    @Test
    public void system() {
        setSystemLineTerminator("\r");
        assertThat(LineTerminator.systemValue()).isEqualTo(CR);

        setSystemLineTerminator("\r\n");
        assertThat(LineTerminator.systemValue()).isEqualTo(CR_LF);
    }

    private void setSystemLineTerminator(String terminator) {
        System.setProperty(SYSTEM_LINE_TERMINATOR_KEY, terminator);
    }

    @After
    public void cleanUp() {
        setSystemLineTerminator(DEFAULT_SYSTEM_LINE_TERMINATOR_VALUE);
    }
}
