package io.webgraph.warc;

import org.iokit.general.EndOfInputException;

import org.iokit.core.IOKitException;

import org.junit.Test;

import java.util.zip.GZIPInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static io.webgraph.warc.WarcType.metadata;
import static org.assertj.core.api.Assertions.*;

public class WarcReaderSpec {

    @Test
    public void readEmptyWarcFile() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/empty.warc"));
        assertThatThrownBy(reader::read).isInstanceOf(IOKitException.class);
        assertThat(reader.getReadCount()).isZero();
    }

    @Test
    public void streamEmptyWarcFile() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/empty.warc"));
        assertThatThrownBy(reader.stream()::count).isInstanceOf(IOKitException.class);
        assertThat(reader.getReadCount()).isZero();
    }

    @Test
    public void readEmptyWarcFileWithMinimumReadCountSetToZero() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/empty.warc"));
        reader.setMinimumReadCount(0);
        assertThat(reader.readOptional()).isNotPresent();
        assertThat(reader.getReadCount()).isEqualTo(0);
    }

    @Test
    public void readSingleRecordWarcFile() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/single.warc"));

        WarcRecord record = reader.read();
        assertThat(record.getHeader().getVersion()).hasToString(WarcVersion.WARC_1_0);
        assertThat(record.getHeader().getType()).isEqualTo(metadata);
        assertThat(record.getHeader().getContentLength()).isEqualTo(1152);
        assertThat(record.getBody().getData()[record.getBody().getData().length - 1]).isEqualTo((byte) '}');

        assertThat(reader.readOptional()).isNotPresent();
        assertThat(reader.getReadCount()).isEqualTo(1);
    }

    @Test
    public void readMultiRecordWarcFile() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/multi.warc"));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.readOptional()).isNotPresent();
        assertThat(reader.getReadCount()).isEqualTo(3);
    }

    @Test
    public void streamMultiRecordWarcFile() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/multi.warc"));
        assertThat(reader.stream().count()).isEqualTo(3);
        assertThat(reader.getReadCount()).isEqualTo(3);
    }

    @Test
    public void readMultiRecordWarcFileWithMalformedRecord() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/multi-with-malformed-record.warc"));

        // the first record is well-formed
        reader.read();

        // the second record is garbage
        assertThatThrownBy(reader::read)
            .isExactlyInstanceOf(IOKitException.class);

        // we never get to the third record

        // count should be 1, as we only successfully read 1 record
        assertThat(reader.getReadCount()).isEqualTo(1);
    }

    @Test
    public void readMultiRecordWarcFileWithoutTrailingConcatenator() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/multi-without-trailing-concatenator.warc"));

        // the first record is well-formed
        reader.read();

        // the second record is well-formed
        reader.read();
        assertThat(reader.getReadCount()).isEqualTo(2);

        // the third record itself is well-formed but the absence of
        // trailing newlines causes the read to fail
        assertThatThrownBy(reader::read)
            .isInstanceOf(EndOfInputException.class);

        assertThat(reader.getReadCount()).isEqualTo(2);
    }

    @Test
    public void read_MultiRecordWarcFile_WithoutTrailingConcatenator_And_SetExpectTrailingConcatenator_SetToFalse() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/multi-without-trailing-concatenator.warc"));
        reader.setExpectTrailingConcatenator(false);

        // the first record is well-formed
        reader.read();

        // the second record is well-formed
        reader.read();
        assertThat(reader.getReadCount()).isEqualTo(2);

        // the third record is well-formed and the absence of
        // trailing newlines does not cause the read to fail
        reader.read();
        assertThat(reader.getReadCount()).isEqualTo(3);
    }

    @Test
    public void readMultiRecordWarcFileWithGarbageOnLastLine() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/multi-with-garbage-on-last-line.warc"));

        // the first record is well-formed
        reader.read();

        // the second record is well-formed
        reader.read();
        assertThat(reader.getReadCount()).isEqualTo(2);

        // the third record is well-formed but garbage on
        // the last line of what should be a concatenator
        // causes a validation exception
        assertThatThrownBy(reader::read)
            .isInstanceOf(IOKitException.class);

        // read count should still be at 2 since failing
        // to read a concatenator/ means failing to read
        // the record that precedes it
        assertThat(reader.getReadCount()).isEqualTo(2);

        assertThat(reader.in.isAtEOF()).isTrue();
    }

    @Test
    public void readGzippedWarcFile() {
        Warc.Reader reader = new Warc.Reader(getClass().getResourceAsStream("/io/webgraph/warc/multi.warc.gz"));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.readOptional()).isNotPresent();
        assertThat(reader.getReadCount()).isEqualTo(3);
    }

    @Test
    public void readGzippedWarcFileWithUserProvidedGZipInputStream() throws IOException {
        Warc.Reader reader = new Warc.Reader(
            new GZIPInputStream(getClass().getResourceAsStream("/io/webgraph/warc/multi.warc.gz")));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.readOptional()).isNotPresent();
        assertThat(reader.getReadCount()).isEqualTo(3);
    }

    @Test
    public void readGzippedWarcFileAsFile() {
        Warc.Reader reader = new Warc.Reader(new File(getClass().getResource("/io/webgraph/warc/multi.warc.gz").getFile()));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.readOptional()).isNotPresent();
        assertThat(reader.getReadCount()).isEqualTo(3);
    }

    @Test
    public void closeIsWellBehaved() throws FileNotFoundException {
        InputStream input =
            new FileInputStream(
                new File(getClass().getResource("/io/webgraph/warc/multi.warc.gz").getFile()));

        try (Warc.Reader reader = new Warc.Reader(input)) {
            reader.read();
        }

        // any further attempt to read from the stream should now fail
        assertThatThrownBy(input::read)
            .isInstanceOf(IOException.class)
            .hasMessage("Stream Closed");
    }
}
