package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;
import org.iokit.warc.WarcVersion;

import org.iokit.core.read.ReaderException;

import org.iokit.core.validate.Validator;
import org.iokit.core.validate.ValidatorException;

import org.junit.Test;

import java.util.zip.GZIPInputStream;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;
import static org.iokit.warc.WarcRecord.Type.metadata;




import org.iokit.core.config.Reflector;

public class WarcReaderSpec {

    @Test
    public void readEmptyWarcFile() throws IOException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/empty.warc"));
        assertThatThrownBy(reader::read).isInstanceOf(RuntimeException.class);
        assertThat(reader.getCurrentCount()).isZero();
    }

    @Test
    public void readSingleRecordWarcFile() throws IOException, ReaderException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/single.warc"));

        WarcRecord record = reader.read();
        assertThat(record.getHeader().getVersion()).hasToString(WarcVersion.WARC_1_0);
        assertThat(record.getHeader().getRecordType()).isEqualTo(metadata);
        assertThat(record.getHeader().getContentLength()).isEqualTo(1152);
        assertThat(record.getBody().getData()[record.getBody().getData().length - 1]).isEqualTo((byte) '}');

        assertThat(reader.read()).isEqualTo(null);
        assertThat(reader.getCurrentCount()).isEqualTo(1);
    }

    @Test
    public void readMultiRecordWarcFile() throws IOException, ReaderException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/multi.warc"));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.read()).isNull();
        assertThat(reader.getCurrentCount()).isEqualTo(3);
    }

    @Test
    public void streamMultiRecordWarcFile() throws IOException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/multi.warc"));
        assertThat(reader.stream().count()).isEqualTo(3);
        assertThat(reader.getCurrentCount()).isEqualTo(3);
    }

    @Test
    public void readMultiRecordWarcFileWithMalformedRecord() throws IOException, ReaderException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/multi-with-malformed-record.warc"));

        // the first record is well-formed
        reader.read();

        // the second record is garbage
        assertThatThrownBy(reader::read)
            .hasRootCauseInstanceOf(ValidatorException.class);

        // we never get to the third record

        // count should be 1, as we only successfully read 1 record
        assertThat(reader.getCurrentCount()).isEqualTo(1);
    }

    @Test
    public void readMultiRecordWarcFileWithMalformedEndOfFile() throws IOException, ReaderException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/multi-with-malformed-eof.warc"));

        // the first record is well-formed
        reader.read();

        // the second record is well-formed
        reader.read();
        assertThat(reader.getCurrentCount()).isEqualTo(2);

        // the third record itself is well-formed but the absence of
        // trailing newlines causes the read to fail
        assertThatThrownBy(reader::read)
            .isInstanceOf(EOFException.class);

        // arguably the count should now be 3, as we did actually read the record
        // but it remains at 2 to avoid making the implementation more complex and
        // because it would be impossible to actually do anything with the record
        // in practice
        assertThat(reader.getCurrentCount()).isEqualTo(2);
    }

    @Test
    public void readGzippedWarcFile() throws IOException, ReaderException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/multi.warc.gz"));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.read()).isNull();
        assertThat(reader.getCurrentCount()).isEqualTo(3);
    }

    @Test
    public void readGzippedWarcFileWithUserProvidedGZipInputStream() throws IOException, ReaderException {
        WarcReader reader = new WarcReader(
            new GZIPInputStream(getClass().getResourceAsStream("/org/iokit/warc/multi.warc.gz")));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.read()).isNull();
        assertThat(reader.getCurrentCount()).isEqualTo(3);
    }

    @Test
    public void readGzippedWarcFileAsFile() throws IOException, ReaderException {
        WarcReader reader = new WarcReader(new File(getClass().getResource("/org/iokit/warc/multi.warc.gz").getFile()));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.read()).isNull();
        assertThat(reader.getCurrentCount()).isEqualTo(3);
    }

    @Test
    public void closeIsWellBehaved() throws IOException, ReaderException {
        InputStream input =
            new FileInputStream(
                new File(getClass().getResource("/org/iokit/warc/multi.warc.gz").getFile()));

        try (WarcReader reader = new WarcReader(input)) {
            reader.read();
        }

        // any further attempt to read from the stream should now fail
        assertThatThrownBy(input::read)
            .isInstanceOf(IOException.class)
            .hasMessage("Stream Closed");
    }

    @Test
    public void disableValidators() throws Exception {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/multi.warc"));

        new Reflector().configure(reader, Validator.class, v -> v.setEnabled(false));

        assertThat(reader.stream().count()).isEqualTo(3);
    }
}
