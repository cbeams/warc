package org.iokit.core.input;

import org.iokit.core.read.EndOfInputException;
import org.iokit.core.read.LineReader;

import org.iokit.core.token.LineTerminator;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.*;

public class LineReaderSpec {

    protected LineReader reader(String input) {
        return
            new LineReader(
                new LineInputStream(
                    new ByteArrayInputStream(input.getBytes()), LineTerminator.CR_LF));
    }

    @Test
    public void empty() {
        LineReader reader = reader("");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void longLine() {
        LineReader reader = reader("this is a long line that will eventually terminate in a crlf fool\r\n");
        assertThat(reader.read()).isEqualTo("this is a long line that will eventually terminate in a crlf fool");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void singleLineWithoutTerminatorAtEOF() {
        LineReader reader = reader("one");
        assertThat(reader.read()).isEqualTo("one");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void multiLineWithoutTerminatorAtEOF() {
        LineReader reader = reader("one\r\ntwo");
        assertThat(reader.read()).isEqualTo("one");
        assertThat(reader.read()).isEqualTo("two");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void withLinefeedTerminatorAtEOF() {
        LineReader reader = reader("one\n");
        assertThat(reader.read()).isEqualTo("one\n");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void withLinefeedTerminatorsAtEOL() {
        LineReader reader = reader("one\ntwo\n");
        assertThat(reader.read()).isEqualTo("one\ntwo\n");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void withCarriageReturnAtEOL() {
        LineReader reader = reader("one\rtwo\r");
        assertThat(reader.read()).isEqualTo("one\rtwo\r");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void withCRLFAtEOL() {
        LineReader reader = reader("one\r\ntwo\r\n");
        assertThat(reader.read()).isEqualTo("one");
        assertThat(reader.read()).isEqualTo("two");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void withBlankLines() {
        LineReader reader = reader("one\r\n\r\nthree\r\n");
        assertThat(reader.read()).isEqualTo("one");
        assertThat(reader.read()).isEqualTo("");
        assertThat(reader.read()).isEqualTo("three");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void withLFCRAtEOL() {
        LineReader reader = reader("one\n\rtwo\n\r");
        assertThat(reader.read()).isEqualTo("one\n\rtwo\n\r");
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }

    @Test
    public void bug() {
        String input = "WARC-Target-URI: http://spravochnik.framar.bg/%D0%BD%D0%BE%D1%80%D0%BC%D0%B0%D1%82%D0%B8%D0%B2%D0%BD%D0%B8-%D0%B0%D0%BA%D1%82%D0%BE%D0%B2%D0%B5/%D0%BD%D0%B0%D1%80%D0%B5%D0%B4%D0%B1%D0%B0-%D0%B7%D0%B0-%D0%B8%D0%B7%D0%BC%D0%B5%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-%D0%B8-%D0%B4%D0%BE%D0%BF%D1%8A%D0%BB%D0%BD%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BD%D0%B0-%D0%BD%D0%B0%D1%80%D0%B5%D0%B4%D0%B1%D0%B0-28-%D0%BE%D1%82-2008-%D0%B3-%D0%B7%D0%B0-%D1%83%D1%81%D1%82%D1%80%D0%BE%D0%B9%D1%81%D1%82%D0%B2%D0%BE%D1%82%D0%BE-%D1%80%D0%B5%D0%B4%D0%B0-%D0%B8-%D0%BE%D1%80%D0%B3%D0%B0%D0%BD%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F%D1%82%D0%B0-%D0%BD%D0%B0-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D1%82%D0%B0-%D0%BD%D0%B0-%D0%B0%D0%BF%D1%82%D0%B5%D0%BA%D0%B8%D1%82%D0%B5-%D0%B8-%D0%BD%D0%BE%D0%BC%D0%B5%D0%BD%D0%BA%D0%BB%D0%B0%D1%82%D1%83%D1%80%D0%B0%D1%82%D0%B0-%D0%BD%D0%B0-%D0%BB%D0%B5%D0%BA%D0%B0%D1%80%D1%81%D1%82%D0%B2%D0%B5%D0%BD%D0%B8%D1%82%D0%B5-%D0%BF%D1%80%D0%BE%D0%B4%D1%83%D0%BA%D1%82%D0%B8-%D0%BE%D0%B1%D0%BD-%D0%B4%D0%B2-%D0%B1%D1%80-109-%D0%BE%D1%82-2008-%D0%B3-%D0%B8%D0%B7%D0%BC-%D0%B8-%D0%B4%D0%BE%D0%BF-%D0%B1%D1%80-67-%D0%BE%D1%82-2010-%D0%B3-%D0%B8-%D0%B1%D1%80-2-%D0%BE%D1%82-2012-%D0%B3";
        LineReader reader = reader(input + "\r\n");
        assertThat(reader.read()).isEqualTo(input);
        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }
}
