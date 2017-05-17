package org.iokit.warc.write;

import org.iokit.warc.WarcRecord;
import org.iokit.warc.read.WarcReader;

import org.iokit.core.read.ReaderException;

import org.iokit.core.input.CrlfLineInputStream;

import org.junit.Ignore;
import org.junit.Test;

import java.util.zip.GZIPInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;




import org.anarres.parallelgzip.ParallelGZIPOutputStream;

public class WarcWriterSpec {

    @Test
    public void writeSingleRecordWarcFile() throws IOException, ReaderException {
        File originalFile = new File(getClass().getResource("/org/iokit/warc/single.warc").getFile());
        WarcReader reader = new WarcReader(originalFile);

        WarcRecord record = reader.read();

        File newFile = new File("/tmp/single.warc");
        try (WarcWriter writer = new WarcWriter(new FileOutputStream(newFile))) {
            writer.write(record);
        }

        assertThat(newFile).hasSameContentAs(originalFile);
    }

    @Test
    public void writeMultiRecordWarcFile() throws IOException {
        File originalFile = new File(getClass().getResource("/org/iokit/warc/multi.warc").getFile());
        File newFile = new File("/tmp/multi.warc");

        try (WarcReader reader = new WarcReader(originalFile);
             WarcWriter writer = new WarcWriter(new FileOutputStream(newFile))) {

            reader.stream().forEach(record -> {
                try {
                    writer.write(record);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        assertThat(newFile).hasSameContentAs(originalFile);
    }

    @Test
    public void writeMultiRecordWarcFileWithFoldingLine() throws IOException {
        File originalFile = new File(getClass().getResource("/org/iokit/warc/multi-with-folding.warc").getFile());
        File newFile = new File("/tmp/multi-with-folding.warc");

        try (WarcReader reader = new WarcReader(originalFile);
             WarcWriter writer = new WarcWriter(new FileOutputStream(newFile))) {

            reader.stream().forEach(record -> {
                try {
                    writer.write(record);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        assertThat(newFile).hasSameContentAs(originalFile);
    }

    @Test
    @Ignore
    public void copyEntireWarcFile() throws IOException {
        File originalFile = new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz");
        File newFile = new File("/tmp/entire.warc.gz");

        // 42864 (before parallel)
        // 18797 (after parallel)

        try (WarcReader reader = new WarcReader(new CrlfLineInputStream(new GZIPInputStream(new FileInputStream(originalFile), 1024*1024)));
             WarcWriter writer = new WarcWriter(new ParallelGZIPOutputStream(new FileOutputStream(newFile)))) {

            reader.stream().forEach(record -> {
                try {
                    writer.write(record);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        //assertThat(newFile).hasSameContentAs(originalFile);
    }
}
