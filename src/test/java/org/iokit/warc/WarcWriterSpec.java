package org.iokit.warc;

import org.junit.Ignore;
import org.junit.Test;

import java.util.zip.GZIPInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class WarcWriterSpec {

    @Test
    public void writeSingleRecordWarcFile() {
        File oldFile = new File(getClass().getResource("/org/iokit/warc/single.warc").getFile());
        File newFile = new File("/tmp/single.warc");

        try (Warc.Reader reader = new Warc.Reader(oldFile);
             Warc.Writer writer = new Warc.Writer(newFile)) {

            writer.write(reader.read());
        }

        assertThat(newFile).hasSameContentAs(oldFile);
    }

    @Test
    public void writeMultiRecordWarcFile() {
        File oldFile = new File(getClass().getResource("/org/iokit/warc/multi.warc").getFile());
        File newFile = new File("/tmp/multi.warc");

        try (Warc.Reader reader = new Warc.Reader(oldFile);
             Warc.Writer writer = new Warc.Writer(newFile)) {

            reader.stream().forEach(writer::write);
        }

        assertThat(newFile).hasSameContentAs(oldFile);
    }

    @Test
    public void writeGzippedMultiRecordWarcFile() throws IOException {
        File oldFile = new File(getClass().getResource("/org/iokit/warc/multi.warc.gz").getFile());
        File newFile = new File("/tmp/multi.warc.gz");

        try (Warc.Reader reader = new Warc.Reader(oldFile);
             Warc.Writer writer = new Warc.Writer(newFile)) {

            reader.stream().forEach(writer::write);
        }

        try (GZIPInputStream oldInput = new GZIPInputStream(new FileInputStream(oldFile));
             GZIPInputStream newInput = new GZIPInputStream(new FileInputStream(newFile))) {

            assertThat(newInput).hasSameContentAs(oldInput);
        }
    }

    @Test
    public void writeMultiRecordWarcFileWithFoldingLine() throws IOException {
        File oldFile = new File(getClass().getResource("/org/iokit/warc/multi-with-folding.warc").getFile());
        File newFile = new File("/tmp/multi-with-folding.warc");

        try (Warc.Reader reader = new Warc.Reader(oldFile);
             Warc.Writer writer = new Warc.Writer(newFile)) {

            reader.stream().forEach(writer::write);
        }

        assertThat(newFile).hasSameContentAs(oldFile);
    }

    @Test
    @Ignore
    public void copyEntireWarcFile() throws IOException {
        File oldFile = new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00003-ip-10-233-31-227.ec2.internal.warc.wat.gz");
        File newFile = new File("/tmp/00003.warc.gz");

        // 42864 (before parallel)
        // 18797 (after parallel)
        // 68630 (after segmentable)

        try (Warc.Reader reader = new Warc.Reader(oldFile);
             Warc.Writer writer = new Warc.Writer(newFile)) {

            reader.stream().forEach(writer::write);
        }

        //assertThat(newFile).hasSameContentAs(originalFile);
    }
}