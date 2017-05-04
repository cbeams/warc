package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;
import org.iokit.warc.WarcRecordVersion;

import org.junit.Test;

import java.util.zip.GZIPInputStream;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.iokit.warc.WarcRecord.Type.*;

public class WarcReaderSpec {

    @Test
    public void readEmptyWarcFile() {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/empty.warc"));
        assertThatThrownBy(reader::read).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void readSingleRecordWarcFile() {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/1152.warc"));

        WarcRecord record = reader.read();
        assertThat(record.getHeader().getVersion()).hasToString(WarcRecordVersion.WARC_1_0);
        assertThat(record.getHeader().getRecordType()).isEqualTo(metadata);
        assertThat(record.getHeader().getContentLength()).isEqualTo(1152);
        assertThat(record.getBody().getData()[record.getBody().getData().length - 1]).isEqualTo((byte) '}');

        assertThat(reader.read()).isEqualTo(null);
    }

    @Test
    public void streamSingleRecordWarcFile() {
        new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/1152.warc"))
            .stream()
            .forEach(record -> {
                assertThat(record.getHeader().getVersion()).hasToString(WarcRecordVersion.WARC_1_0);
                assertThat(record.getHeader().getRecordType()).isEqualTo(metadata);
                assertThat(record.getHeader().getContentLength()).isEqualTo(1152);
                assertThat(record.getBody().getData()[record.getBody().getData().length - 1]).isEqualTo((byte) '}');
            });
    }


    @Test
    public void readMalformedWarcFile() {
        new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/1152.warc"))
            .stream()
            .forEach(record -> {
                assertThat(record.getHeader().getVersion()).hasToString(WarcRecordVersion.WARC_1_0);
                assertThat(record.getHeader().getRecordType()).isEqualTo(metadata);
                assertThat(record.getHeader().getContentLength()).isEqualTo(1152);
                assertThat(record.getBody().getData()[record.getBody().getData().length - 1]).isEqualTo((byte) '}');
            });
    }

    @Test
    public void readTwoRecordWarcFile() {
        new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/1152.warc"))
            .stream()
            .forEach(record -> {
                assertThat(record.getHeader().getVersion()).hasToString(WarcRecordVersion.WARC_1_0);
                assertThat(record.getHeader().getRecordType()).isEqualTo(metadata);
                assertThat(record.getHeader().getContentLength()).isEqualTo(1152);
                assertThat(record.getBody().getData()[record.getBody().getData().length - 1]).isEqualTo((byte) '}');
            });
    }

    @Test
    public void wellFormedWatFile() {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/well-formed.warc.wat"));

        assertThat(reader.read().getHeader().getRecordType()).isEqualTo(warcinfo);
        assertThat(reader.read().getHeader().getRecordType()).isEqualTo(metadata);
        assertThat(reader.read().getHeader().getRecordType()).isEqualTo(metadata);

        assertThat(reader.read()).isNull(); // EOF as expected
    }

    @Test
    public void testFoo() throws IOException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/1434-644.warc"));
        WarcRecord record = reader.read();
        assertThat(record.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.read()).isNull();
    }

    @Test
    public void testFoo2() throws IOException {
        WarcReader reader = new WarcReader(getClass().getResourceAsStream("/org/iokit/warc/1434-644-2.warc"));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.read()).isNull();
    }

    @Test
    public void testFoo3() throws IOException {
        WarcReader reader = new WarcReader(new GZIPInputStream(getClass().getResourceAsStream("/org/iokit/warc/1434-644-2.warc.gz")));
        WarcRecord record1 = reader.read();
        WarcRecord record2 = reader.read();
        WarcRecord record3 = reader.read();
        assertThat(record3.getHeader().getContentLength()).isEqualTo(1434);
        assertThat(reader.read()).isNull();
    }
}
