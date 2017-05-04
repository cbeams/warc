package org.iokit.core.input;

import org.iokit.core.read.LineReader;

import org.iokit.core.parse.ParsingException;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import static org.assertj.core.api.Assertions.*;

public class LineReaderSpec {

    protected LineReader reader(String input) {
        return new LineReader(
            new LineInputStream(
                new ByteArrayInputStream(input.getBytes()),
                StandardLineTerminator.CR_LF));
    }

    @Test
    public void empty() throws EOFException {
        LineReader reader = reader("");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void longLine() throws EOFException, ParsingException {
        LineReader reader = reader("this is a long line that will eventually terminate in a crlf fool\r\n");
        assertThat(reader.read()).isEqualTo("this is a long line that will eventually terminate in a crlf fool");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void singleLineWithoutTerminatorAtEOF() throws EOFException, ParsingException {
        LineReader reader = reader("one");
        assertThat(reader.read()).isEqualTo("one");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void multiLineWithoutTerminatorAtEOF() throws EOFException, ParsingException {
        LineReader reader = reader("one\r\ntwo");
        assertThat(reader.read()).isEqualTo("one");
        assertThat(reader.read()).isEqualTo("two");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void withLinefeedTerminatorAtEOF() throws EOFException, ParsingException {
        LineReader reader = reader("one\n");
        assertThat(reader.read()).isEqualTo("one\n");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void withLinefeedTerminatorsAtEOL() throws EOFException, ParsingException {
        LineReader reader = reader("one\ntwo\n");
        assertThat(reader.read()).isEqualTo("one\ntwo\n");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void withCarriageReturnAtEOL() throws EOFException, ParsingException {
        LineReader reader = reader("one\rtwo\r");
        assertThat(reader.read()).isEqualTo("one\rtwo\r");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void withCRLFAtEOL() throws EOFException, ParsingException {
        LineReader reader = reader("one\r\ntwo\r\n");
        assertThat(reader.read()).isEqualTo("one");
        assertThat(reader.read()).isEqualTo("two");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void withBlankLines() throws EOFException, ParsingException {
        LineReader reader = reader("one\r\n\r\nthree\r\n");
        assertThat(reader.read()).isEqualTo("one");
        assertThat(reader.read()).isEqualTo("");
        assertThat(reader.read()).isEqualTo("three");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }

    @Test
    public void withLFCRAtEOL() throws EOFException, ParsingException {
        LineReader reader = reader("one\n\rtwo\n\r");
        assertThat(reader.read()).isEqualTo("one\n\rtwo\n\r");
        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }
}
